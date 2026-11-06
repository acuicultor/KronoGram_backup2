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
import com.example.ui.screens.settings.telegram.ChatSettingsScreen
import com.example.ui.screens.settings.telegram.DataStorageSettingsScreen
import com.example.ui.screens.settings.telegram.DevicesSettingsScreen
import com.example.ui.screens.settings.telegram.LanguageSettingsScreen
import com.example.ui.screens.settings.telegram.NotificationsSettingsScreen
import com.example.ui.screens.settings.telegram.PrivacySettingsScreen
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
                                themeMode = settings.themeMode,
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
                                themeMode = settings.themeMode,
                                onLoginSuccess = { phone ->
                                    viewModel.preferencesRepo.setOnboardingCompleted(true)
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
                                            onSelectTheme = { theme ->
                                                viewModel.setThemeMode(theme)
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
                                onNavigateToNotifications = {
                                    viewModel.navigateTo(ScreenDestination.NotificationsSettings)
                                },
                                onNavigateToPrivacy = {
                                    viewModel.navigateTo(ScreenDestination.PrivacySettings)
                                },
                                onNavigateToDataStorage = {
                                    viewModel.navigateTo(ScreenDestination.DataStorageSettings)
                                },
                                onNavigateToChatSettings = {
                                    viewModel.navigateTo(ScreenDestination.ChatSettings)
                                },
                                onNavigateToDevices = {
                                    viewModel.navigateTo(ScreenDestination.DevicesSettings)
                                },
                                onNavigateToLanguage = {
                                    viewModel.navigateTo(ScreenDestination.LanguageSettings)
                                },
                                onLogout = {
                                    viewModel.engine.logout()
                                    viewModel.navigateTo(ScreenDestination.Auth)
                                }
                            )
                        }

                        is ScreenDestination.NotificationsSettings -> {
                            BackHandler { viewModel.navigateBack() }
                            NotificationsSettingsScreen(
                                settings = settings,
                                onBack = { viewModel.navigateBack() },
                                onToggleSetting = { type, enabled ->
                                    viewModel.setNotificationSetting(type, enabled)
                                },
                                onSetBadgeType = { type ->
                                    viewModel.setNotificationBadgeType(type)
                                }
                            )
                        }

                        is ScreenDestination.PrivacySettings -> {
                            BackHandler { viewModel.navigateBack() }
                            PrivacySettingsScreen(
                                settings = settings,
                                onBack = { viewModel.navigateBack() },
                                onSetPrivacy = { key, value ->
                                    viewModel.setPrivacySetting(key, value)
                                }
                            )
                        }

                        is ScreenDestination.DataStorageSettings -> {
                            BackHandler { viewModel.navigateBack() }
                            DataStorageSettingsScreen(
                                settings = settings,
                                onBack = { viewModel.navigateBack() },
                                onToggleAutoDownload = { type, enabled ->
                                    viewModel.setAutoDownload(type, enabled)
                                },
                                onSetStorageOptions = { dur, max ->
                                    viewModel.setDataStorageOptions(dur, max)
                                },
                                onClearCache = {
                                    viewModel.clearCache()
                                }
                            )
                        }

                        is ScreenDestination.ChatSettings -> {
                            BackHandler { viewModel.navigateBack() }
                            ChatSettingsScreen(
                                settings = settings,
                                onBack = { viewModel.navigateBack() },
                                onSetThemeMode = { mode ->
                                    viewModel.setThemeMode(mode)
                                },
                                onSetAppearance = { fontSize, cornerRadius ->
                                    viewModel.setChatAppearance(fontSize, cornerRadius)
                                },
                                onSetChatSetting = { key, value ->
                                    viewModel.setChatSetting(key, value)
                                }
                            )
                        }

                        is ScreenDestination.DevicesSettings -> {
                            BackHandler { viewModel.navigateBack() }
                            DevicesSettingsScreen(
                                settings = settings,
                                onBack = { viewModel.navigateBack() }
                            )
                        }

                        is ScreenDestination.LanguageSettings -> {
                            BackHandler { viewModel.navigateBack() }
                            LanguageSettingsScreen(
                                settings = settings,
                                onBack = { viewModel.navigateBack() },
                                onSelectLanguage = { lang ->
                                    viewModel.setLanguage(lang)
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
                                onSetKronoFeature = { key, value -> viewModel.setKronoFeature(key, value) },
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
