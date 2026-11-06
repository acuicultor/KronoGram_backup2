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
    val accountPhone: String = "",
    val accountUsername: String = "chronouser",
    val accountDisplayName: String = "Krono Pilot",
    val accountBio: String = "Mastering time across the Telegram network"
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
            accountPhone = prefs.getString("account_phone", "+34 600 000 000") ?: "",
            accountUsername = prefs.getString("account_username", "chronouser") ?: "chronouser",
            accountDisplayName = prefs.getString("account_display_name", "Krono Pilot") ?: "Krono Pilot",
            accountBio = prefs.getString("account_bio", "Mastering time across Telegram") ?: ""
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

    fun setDefaultDestructSeconds(seconds: Int) {
        prefs.edit().putInt("destruct_seconds", seconds).apply()
        _settingsState.value = _settingsState.value.copy(defaultDestructSeconds = seconds)
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
}
