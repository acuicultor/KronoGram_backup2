package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.ChatEntity
import com.example.data.model.FolderTabEntity
import com.example.data.repository.ChatRepository
import com.example.data.repository.FolderRepository
import com.example.data.repository.KronoPreferencesRepository
import com.example.data.repository.KronoSettingsState
import com.example.engine.AuthState
import com.example.engine.TelegramEngine
import com.example.ui.theme.KronoThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class ScreenDestination {
    object Onboarding : ScreenDestination()
    object Auth : ScreenDestination()
    object ChatList : ScreenDestination()
    data class ChatDetail(val chatId: Long) : ScreenDestination()
    object Settings : ScreenDestination()
    object KronoSettings : ScreenDestination()
    object FolderManager : ScreenDestination()
    // Dedicated Native Telegram Settings screens
    object NotificationsSettings : ScreenDestination()
    object PrivacySettings : ScreenDestination()
    object DataStorageSettings : ScreenDestination()
    object ChatSettings : ScreenDestination()
    object DevicesSettings : ScreenDestination()
    object LanguageSettings : ScreenDestination()
}

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    val preferencesRepo = KronoPreferencesRepository(application)
    val engine = TelegramEngine(application, database)
    val folderRepo = FolderRepository(database.folderDao())
    val chatRepo = ChatRepository(database.chatDao(), database.messageDao(), engine, preferencesRepo)

    val settingsState: StateFlow<KronoSettingsState> = preferencesRepo.settingsState
    val authState: StateFlow<AuthState> = engine.authState

    val activeFolders: StateFlow<List<FolderTabEntity>> = folderRepo.activeFolders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allFolders: StateFlow<List<FolderTabEntity>> = folderRepo.allFolders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedFolderId = MutableStateFlow<String>("all")
    val selectedFolderId: StateFlow<String> = _selectedFolderId.asStateFlow()

    private val _screenDestination = MutableStateFlow<ScreenDestination>(ScreenDestination.ChatList)
    val screenDestination: StateFlow<ScreenDestination> = _screenDestination.asStateFlow()

    private val _activeChatId = MutableStateFlow<Long?>(null)
    val activeChatId: StateFlow<Long?> = _activeChatId.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val filteredChats: StateFlow<List<ChatEntity>> = combine(
        chatRepo.allChats,
        _selectedFolderId,
        _searchQuery,
        activeFolders
    ) { chats, folderId, query, folders ->
        val folder = folders.find { it.id == folderId }
        val filterType = folder?.filterType ?: "ALL"

        chats.filter { chat ->
            val matchesFolder = when (filterType) {
                "ALL" -> true
                "UNREAD" -> chat.unreadCount > 0
                "PERSONAL" -> chat.type == "USER" || chat.type == "SECRET"
                "GROUPS" -> chat.type == "GROUP"
                "CHANNELS" -> chat.type == "CHANNEL"
                "BOTS" -> chat.type == "BOT"
                else -> true
            }

            val matchesQuery = if (query.isBlank()) {
                true
            } else {
                chat.title.contains(query, ignoreCase = true) ||
                    chat.username.contains(query, ignoreCase = true) ||
                    chat.lastMessage.contains(query, ignoreCase = true)
            }

            matchesFolder && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            if (!preferencesRepo.settingsState.value.isOnboardingCompleted) {
                _screenDestination.value = ScreenDestination.Onboarding
            } else if (engine.authState.value !is AuthState.Ready) {
                _screenDestination.value = ScreenDestination.Auth
            } else {
                _screenDestination.value = ScreenDestination.ChatList
            }

            folderRepo.initializeDefaultFolders()
            engine.seedInitialTelegramData()
        }
    }

    fun completeOnboarding(selectedTabs: List<String>) {
        viewModelScope.launch {
            folderRepo.initializeDefaultFolders(selectedTabs)
            preferencesRepo.setOnboardingCompleted(true)
            if (engine.authState.value is AuthState.Ready) {
                _screenDestination.value = ScreenDestination.ChatList
            } else {
                _screenDestination.value = ScreenDestination.Auth
            }
        }
    }

    fun selectFolder(folderId: String) {
        _selectedFolderId.value = folderId
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun navigateTo(destination: ScreenDestination) {
        if (destination is ScreenDestination.ChatDetail) {
            _activeChatId.value = destination.chatId
            viewModelScope.launch {
                chatRepo.markChatAsRead(destination.chatId)
            }
        }
        _screenDestination.value = destination
    }

    fun navigateBack() {
        when (_screenDestination.value) {
            is ScreenDestination.ChatDetail -> _screenDestination.value = ScreenDestination.ChatList
            is ScreenDestination.NotificationsSettings,
            is ScreenDestination.PrivacySettings,
            is ScreenDestination.DataStorageSettings,
            is ScreenDestination.ChatSettings,
            is ScreenDestination.DevicesSettings,
            is ScreenDestination.LanguageSettings,
            is ScreenDestination.KronoSettings -> _screenDestination.value = ScreenDestination.Settings

            is ScreenDestination.FolderManager -> _screenDestination.value = ScreenDestination.KronoSettings
            is ScreenDestination.Settings -> _screenDestination.value = ScreenDestination.ChatList
            is ScreenDestination.Auth -> {
                if (preferencesRepo.settingsState.value.isOnboardingCompleted) {
                    _screenDestination.value = ScreenDestination.ChatList
                }
            }
            else -> {}
        }
    }

    fun sendMessage(
        chatId: Long,
        text: String,
        isEncrypted: Boolean = false,
        scheduledTimestamp: Long? = null,
        autoDestructSeconds: Int? = null
    ) {
        viewModelScope.launch {
            chatRepo.sendMessage(chatId, text, isEncrypted, scheduledTimestamp, autoDestructSeconds)
        }
    }

    fun toggleGhostMode(enabled: Boolean) {
        preferencesRepo.setGhostMode(enabled)
    }

    fun toggleHideOnline(enabled: Boolean) {
        preferencesRepo.setHideOnlineStatus(enabled)
    }

    fun setThemeMode(mode: KronoThemeMode) {
        preferencesRepo.setThemeMode(mode)
    }

    fun toggleBiometricLock(enabled: Boolean) {
        preferencesRepo.setBiometricLock(enabled)
    }

    fun setSecurityPin(pin: String?) {
        preferencesRepo.setSecurityPin(pin)
    }

    fun unlockApp() {
        preferencesRepo.unlockApp()
    }

    fun lockApp() {
        preferencesRepo.lockApp()
    }

    fun toggleFolder(id: String, enabled: Boolean) {
        viewModelScope.launch {
            folderRepo.toggleFolder(id, enabled)
        }
    }

    fun addCustomFolder(title: String, filterType: String) {
        viewModelScope.launch {
            folderRepo.addCustomFolder(title, filterType)
        }
    }

    fun deleteFolder(id: String) {
        viewModelScope.launch {
            folderRepo.deleteFolder(id)
        }
    }

    fun createChat(title: String, username: String, type: String) {
        viewModelScope.launch {
            val newId = chatRepo.createNewChat(title, username, type)
            navigateTo(ScreenDestination.ChatDetail(newId))
        }
    }

    fun setNotificationSetting(type: String, enabled: Boolean) {
        preferencesRepo.setNotificationSetting(type, enabled)
    }

    fun setPrivacySetting(setting: String, value: String) {
        preferencesRepo.setPrivacySetting(setting, value)
    }

    fun setAutoDownload(type: String, enabled: Boolean) {
        preferencesRepo.setAutoDownload(type, enabled)
    }

    fun setChatAppearance(fontSize: Int, cornerRadius: Int) {
        preferencesRepo.setChatAppearance(fontSize, cornerRadius)
    }

    fun setLanguage(lang: String) {
        preferencesRepo.setLanguage(lang)
    }

    fun setKronoFeature(key: String, value: Any) {
        preferencesRepo.setKronoFeature(key, value)
    }

    fun setNotificationBadgeType(type: String) {
        preferencesRepo.setNotificationBadgeType(type)
    }

    fun setDataStorageOptions(keepDuration: String, maxCache: String) {
        preferencesRepo.setDataStorageOptions(keepDuration, maxCache)
    }

    fun setChatSetting(key: String, value: Any) {
        preferencesRepo.setChatSetting(key, value)
    }

    fun clearCache() {
        preferencesRepo.clearCache()
    }
}
