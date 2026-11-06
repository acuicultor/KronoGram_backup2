package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.ui.theme.KronoThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class KronoSettingsState(
    val isOnboardingCompleted: Boolean = false,
    val isGhostMode: Boolean = true,
    val isHideOnlineStatus: Boolean = true,
    val themeMode: KronoThemeMode = KronoThemeMode.AMOLED_PURE,
    val isBiometricLockEnabled: Boolean = false,
    val securityPin: String? = null,
    val isAppLocked: Boolean = false,
    val defaultDestructSeconds: Int = 0,
    val accountPhone: String = "+34 612 345 678",
    val accountUsername: String = "rcuenca",
    val accountDisplayName: String = "Ramon Cuenca",
    val accountBio: String = "Mastering time across the Telegram network",
    // Telegram Native Settings
    val notificationsPrivateChats: Boolean = true,
    val notificationsGroups: Boolean = true,
    val notificationsChannels: Boolean = true,
    val inAppSounds: Boolean = true,
    val inAppVibrate: Boolean = true,
    val inAppPreview: Boolean = true,
    val lastSeenPrivacy: String = "Todos", // "Todos", "Mis contactos", "Nadie"
    val phoneNumberPrivacy: String = "Mis contactos",
    val autoDownloadMobile: Boolean = false,
    val autoDownloadWifi: Boolean = true,
    val autoDownloadRoaming: Boolean = false,
    val chatFontSize: Int = 16,
    val bubbleCornerRadius: Int = 16,
    val language: String = "Español",
    val cachedStorageMb: Int = 348
)

class KronoPreferencesRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("kronogram_prefs", Context.MODE_PRIVATE)

    private val _settingsState = MutableStateFlow(loadSettings())
    val settingsState: StateFlow<KronoSettingsState> = _settingsState.asStateFlow()

    private fun loadSettings(): KronoSettingsState {
        val themeModeStr = prefs.getString("theme_mode", KronoThemeMode.AMOLED_PURE.name) ?: KronoThemeMode.AMOLED_PURE.name
        val themeMode = try {
            KronoThemeMode.valueOf(themeModeStr)
        } catch (e: Exception) {
            KronoThemeMode.AMOLED_PURE
        }
        val hasPin = prefs.getString("security_pin", null)
        val biometric = prefs.getBoolean("biometric_lock", false)

        return KronoSettingsState(
            isOnboardingCompleted = prefs.getBoolean("onboarding_completed", false),
            isGhostMode = prefs.getBoolean("ghost_mode", true),
            isHideOnlineStatus = prefs.getBoolean("hide_online", true),
            themeMode = themeMode,
            isBiometricLockEnabled = biometric,
            securityPin = hasPin,
            isAppLocked = (biometric || !hasPin.isNullOrEmpty()),
            defaultDestructSeconds = prefs.getInt("destruct_seconds", 0),
            accountPhone = prefs.getString("account_phone", "+34 612 345 678") ?: "+34 612 345 678",
            accountUsername = prefs.getString("account_username", "rcuenca") ?: "rcuenca",
            accountDisplayName = prefs.getString("account_display_name", "Ramon Cuenca") ?: "Ramon Cuenca",
            accountBio = prefs.getString("account_bio", "Mastering time across the Telegram network") ?: "",
            notificationsPrivateChats = prefs.getBoolean("notif_private", true),
            notificationsGroups = prefs.getBoolean("notif_groups", true),
            notificationsChannels = prefs.getBoolean("notif_channels", true),
            inAppSounds = prefs.getBoolean("in_app_sounds", true),
            inAppVibrate = prefs.getBoolean("in_app_vibrate", true),
            inAppPreview = prefs.getBoolean("in_app_preview", true),
            lastSeenPrivacy = prefs.getString("last_seen_privacy", "Todos") ?: "Todos",
            phoneNumberPrivacy = prefs.getString("phone_privacy", "Mis contactos") ?: "Mis contactos",
            autoDownloadMobile = prefs.getBoolean("auto_mobile", false),
            autoDownloadWifi = prefs.getBoolean("auto_wifi", true),
            autoDownloadRoaming = prefs.getBoolean("auto_roaming", false),
            chatFontSize = prefs.getInt("chat_font_size", 16),
            bubbleCornerRadius = prefs.getInt("bubble_corner_radius", 16),
            language = prefs.getString("app_language", "Español") ?: "Español",
            cachedStorageMb = prefs.getInt("cached_storage_mb", 348)
        )
    }

    fun setOnboardingCompleted(completed: Boolean) {
        prefs.edit().putBoolean("onboarding_completed", completed).apply()
        _settingsState.value = _settingsState.value.copy(isOnboardingCompleted = completed)
    }

    fun setGhostMode(enabled: Boolean) {
        prefs.edit().putBoolean("ghost_mode", enabled).apply()
        _settingsState.value = _settingsState.value.copy(isGhostMode = enabled)
    }

    fun setHideOnlineStatus(enabled: Boolean) {
        prefs.edit().putBoolean("hide_online", enabled).apply()
        _settingsState.value = _settingsState.value.copy(isHideOnlineStatus = enabled)
    }

    fun setThemeMode(mode: KronoThemeMode) {
        prefs.edit().putString("theme_mode", mode.name).apply()
        _settingsState.value = _settingsState.value.copy(themeMode = mode)
    }

    fun setBiometricLock(enabled: Boolean) {
        prefs.edit().putBoolean("biometric_lock", enabled).apply()
        _settingsState.value = _settingsState.value.copy(isBiometricLockEnabled = enabled)
    }

    fun setSecurityPin(pin: String?) {
        prefs.edit().putString("security_pin", pin).apply()
        _settingsState.value = _settingsState.value.copy(securityPin = pin)
    }

    fun unlockApp() {
        _settingsState.value = _settingsState.value.copy(isAppLocked = false)
    }

    fun lockApp() {
        if (_settingsState.value.isBiometricLockEnabled || !_settingsState.value.securityPin.isNullOrEmpty()) {
            _settingsState.value = _settingsState.value.copy(isAppLocked = true)
        }
    }

    fun saveAccountInfo(phone: String, username: String, displayName: String) {
        prefs.edit()
            .putString("account_phone", phone)
            .putString("account_username", username)
            .putString("account_display_name", displayName)
            .apply()
        _settingsState.value = _settingsState.value.copy(
            accountPhone = phone,
            accountUsername = username,
            accountDisplayName = displayName
        )
    }

    fun setNotificationSetting(type: String, enabled: Boolean) {
        val editor = prefs.edit()
        when (type) {
            "private" -> { editor.putBoolean("notif_private", enabled); _settingsState.value = _settingsState.value.copy(notificationsPrivateChats = enabled) }
            "groups" -> { editor.putBoolean("notif_groups", enabled); _settingsState.value = _settingsState.value.copy(notificationsGroups = enabled) }
            "channels" -> { editor.putBoolean("notif_channels", enabled); _settingsState.value = _settingsState.value.copy(notificationsChannels = enabled) }
            "sounds" -> { editor.putBoolean("in_app_sounds", enabled); _settingsState.value = _settingsState.value.copy(inAppSounds = enabled) }
            "vibrate" -> { editor.putBoolean("in_app_vibrate", enabled); _settingsState.value = _settingsState.value.copy(inAppVibrate = enabled) }
            "preview" -> { editor.putBoolean("in_app_preview", enabled); _settingsState.value = _settingsState.value.copy(inAppPreview = enabled) }
        }
        editor.apply()
    }

    fun setPrivacySetting(setting: String, value: String) {
        val editor = prefs.edit()
        when (setting) {
            "lastSeen" -> { editor.putString("last_seen_privacy", value); _settingsState.value = _settingsState.value.copy(lastSeenPrivacy = value) }
            "phone" -> { editor.putString("phone_privacy", value); _settingsState.value = _settingsState.value.copy(phoneNumberPrivacy = value) }
        }
        editor.apply()
    }

    fun setAutoDownload(type: String, enabled: Boolean) {
        val editor = prefs.edit()
        when (type) {
            "mobile" -> { editor.putBoolean("auto_mobile", enabled); _settingsState.value = _settingsState.value.copy(autoDownloadMobile = enabled) }
            "wifi" -> { editor.putBoolean("auto_wifi", enabled); _settingsState.value = _settingsState.value.copy(autoDownloadWifi = enabled) }
            "roaming" -> { editor.putBoolean("auto_roaming", enabled); _settingsState.value = _settingsState.value.copy(autoDownloadRoaming = enabled) }
        }
        editor.apply()
    }

    fun setChatAppearance(fontSize: Int, cornerRadius: Int) {
        prefs.edit()
            .putInt("chat_font_size", fontSize)
            .putInt("bubble_corner_radius", cornerRadius)
            .apply()
        _settingsState.value = _settingsState.value.copy(
            chatFontSize = fontSize,
            bubbleCornerRadius = cornerRadius
        )
    }

    fun setLanguage(lang: String) {
        prefs.edit().putString("app_language", lang).apply()
        _settingsState.value = _settingsState.value.copy(language = lang)
    }

    fun clearCache() {
        prefs.edit().putInt("cached_storage_mb", 12).apply()
        _settingsState.value = _settingsState.value.copy(cachedStorageMb = 12)
    }
}
