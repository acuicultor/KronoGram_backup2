package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.ui.MainViewModel
import com.example.ui.ScreenDestination
import com.example.ui.components.KronoDrawer
import com.example.ui.components.LockScreenOverlay
import com.example.ui.screens.auth.AuthScreen
import com.example.ui.screens.chatdetail.ChatDetailScreen
import com.example.ui.screens.chatlist.ChatListScreen
import com.example.ui.screens.onboarding.OnboardingWizardScreen
import com.example.ui.screens.settings.FolderManagerScreen
import com.example.ui.screens.settings.KronoSettingsScreen
import com.example.ui.screens.settings.SettingsScreen
import com.example.ui.theme.KronoGramTheme
import com.example.ui.theme.KronoThemeMode
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val settings by viewModel.settingsState.collectAsState()
            val authState by viewModel.authState.collectAsState()
            val screenDestination by viewModel.screenDestination.collectAsState()
            val activeFolders by viewModel.activeFolders.collectAsState()
            val allFolders by viewModel.allFolders.collectAsState()
            val selectedFolderId by viewModel.selectedFolderId.collectAsState()
            val filteredChats by viewModel.filteredChats.collectAsState()
            val activeChatId by viewModel.activeChatId.collectAsState()
            val searchQuery by viewModel.searchQuery.collectAsState()

            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val scope = rememberCoroutineScope()

            KronoGramTheme(themeMode = settings.themeMode) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .navigationBarsPadding()
                ) {
                    when (val dest = screenDestination) {
                        is ScreenDestination.Onboarding -> {
                            OnboardingWizardScreen(
                                onFinished = { selectedTabs ->
                                    viewModel.completeOnboarding(selectedTabs)
                                }
                            )
                        }

                        is ScreenDestination.Auth -> {
                            BackHandler {
                                viewModel.navigateBack()
                            }
                            AuthScreen(
                                engine = viewModel.engine,
                                authState = authState,
                                onLoginSuccess = {
                                    viewModel.navigateTo(ScreenDestination.ChatList)
                                }
                            )
                        }

                        is ScreenDestination.ChatList -> {
                            ModalNavigationDrawer(
                                drawerState = drawerState,
                                drawerContent = {
                                    ModalDrawerSheet {
                                        KronoDrawer(
                                            settings = settings,
                                            onNavigateToSettings = {
                                                scope.launch { drawerState.close() }
                                                viewModel.navigateTo(ScreenDestination.Settings)
                                            },
                                            onNavigateToKronoSettings = {
                                                scope.launch { drawerState.close() }
                                                viewModel.navigateTo(ScreenDestination.KronoSettings)
                                            },
                                            onNewChatClick = { type ->
                                                scope.launch { drawerState.close() }
                                                viewModel.createChat(
                                                    title = when (type) {
                                                        "SECRET" -> "Chat Secreto MTProto"
                                                        "GROUP" -> "Nuevo Grupo Krono"
                                                        "CHANNEL" -> "Canal KronoGram"
                                                        else -> "Nuevo Contacto"
                                                    },
                                                    username = "user_${System.currentTimeMillis() % 1000}",
                                                    type = type
                                                )
                                            },
                                            onToggleAmoled = { checked ->
                                                viewModel.setThemeMode(
                                                    if (checked) KronoThemeMode.AMOLED_PURE else KronoThemeMode.DARK_NIGHT
                                                )
                                            },
                                            onToggleGhostMode = { checked ->
                                                viewModel.toggleGhostMode(checked)
                                            }
                                        )
                                    }
                                }
                            ) {
                                ChatListScreen(
                                    chats = filteredChats,
                                    folders = activeFolders,
                                    selectedFolderId = selectedFolderId,
                                    searchQuery = searchQuery,
                                    settings = settings,
                                    onSelectFolder = { viewModel.selectFolder(it) },
                                    onSearchQueryChange = { viewModel.setSearchQuery(it) },
                                    onChatClick = { chatId ->
                                        viewModel.navigateTo(ScreenDestination.ChatDetail(chatId))
                                    },
                                    onOpenDrawer = {
                                        scope.launch { drawerState.open() }
                                    },
                                    onOpenFolderManager = {
                                        viewModel.navigateTo(ScreenDestination.FolderManager)
                                    },
                                    onOpenKronoSettings = {
                                        viewModel.navigateTo(ScreenDestination.KronoSettings)
                                    },
                                    onLockApp = {
                                        viewModel.lockApp()
                                    },
                                    onCreateChat = { title, username, type ->
                                        viewModel.createChat(title, username, type)
                                    }
                                )
                            }
                        }

                        is ScreenDestination.ChatDetail -> {
                            BackHandler {
                                viewModel.navigateBack()
                            }
                            val activeChat = filteredChats.find { it.id == dest.chatId }
                                ?: filteredChats.firstOrNull()

                            val messages by viewModel.chatRepo.getMessagesForChat(dest.chatId)
                                .collectAsState(initial = emptyList())

                            ChatDetailScreen(
                                chat = activeChat,
                                messages = messages,
                                settings = settings,
                                onBack = { viewModel.navigateBack() },
                                onSendMessage = { text, isEncrypted, scheduledTime, autoDestruct ->
                                    viewModel.sendMessage(
                                        chatId = dest.chatId,
                                        text = text,
                                        isEncrypted = isEncrypted,
                                        scheduledTimestamp = scheduledTime,
                                        autoDestructSeconds = autoDestruct
                                    )
                                }
                            )
                        }

                        is ScreenDestination.Settings -> {
                            BackHandler {
                                viewModel.navigateBack()
                            }
                            SettingsScreen(
                                settings = settings,
                                onBack = { viewModel.navigateBack() },
                                onNavigateToKronoSettings = {
                                    viewModel.navigateTo(ScreenDestination.KronoSettings)
                                },
                                onLogout = {
                                    viewModel.engine.logout()
                                    viewModel.navigateTo(ScreenDestination.Auth)
                                }
                            )
                        }

                        is ScreenDestination.KronoSettings -> {
                            BackHandler {
                                viewModel.navigateBack()
                            }
                            KronoSettingsScreen(
                                settings = settings,
                                onBack = { viewModel.navigateBack() },
                                onToggleGhostMode = { viewModel.toggleGhostMode(it) },
                                onToggleHideOnline = { viewModel.toggleHideOnline(it) },
                                onSetThemeMode = { viewModel.setThemeMode(it) },
                                onToggleBiometric = { viewModel.toggleBiometricLock(it) },
                                onSetSecurityPin = { viewModel.setSecurityPin(it) },
                                onOpenFolderManager = { viewModel.navigateTo(ScreenDestination.FolderManager) }
                            )
                        }

                        is ScreenDestination.FolderManager -> {
                            BackHandler {
                                viewModel.navigateBack()
                            }
                            FolderManagerScreen(
                                folders = allFolders,
                                onBack = { viewModel.navigateBack() },
                                onToggleFolder = { id, enabled -> viewModel.toggleFolder(id, enabled) },
                                onAddCustomFolder = { title, filterType -> viewModel.addCustomFolder(title, filterType) },
                                onDeleteFolder = { id -> viewModel.deleteFolder(id) }
                            )
                        }
                    }

                    // Floating PIN / Biometric Lock Screen if locked
                    if (settings.isAppLocked) {
                        LockScreenOverlay(
                            expectedPin = settings.securityPin,
                            isBiometricAvailable = settings.isBiometricLockEnabled,
                            onUnlocked = {
                                viewModel.unlockApp()
                            }
                        )
                    }
                }
            }
        }
    }
}
