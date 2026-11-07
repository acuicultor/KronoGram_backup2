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

    // Exclusive Krono Features (Inspired by advanced clients, zero external branding)
    val translationProvider: String = "Google Translate", // "Google Translate", "DeepL", "Motor Local"
    val translationTargetLang: String = "Español",
    val enableChatTranslation: Boolean = true,
    val translateBeforeSend: Boolean = false,
    val forwardWithoutQuote: Boolean = true,
    val oneTapForward: Boolean = false,
    val doubleTapAction: String = "Responder", // "Responder", "Traducir", "Copiar", "Reaccionar"
    val voiceToTextEnabled: Boolean = true,
    val useSystemEmojis: Boolean = false,
    val useSystemFonts: Boolean = false,
    val transparentStatusBar: Boolean = true,
    val hideKeyboardOnScroll: Boolean = true,
    val hidePhoneInDrawer: Boolean = false,
    val avatarDrawerBackground: Boolean = false,
    val stickerSize: String = "Normal", // "Pequeño", "Normal", "Grande"
    val autoPauseVideos: Boolean = true,
    val unlimitedFavoriteStickers: Boolean = true,
    val confirmBeforeCall: Boolean = true,
    val ignoreBlockedUsersInGroups: Boolean = true,
    val localEditHistory: Boolean = true,
    val useMonetColors: Boolean = false,

    // Telegram Native Settings Expanded
    val notificationsPrivateChats: Boolean = true,
    val notificationsGroups: Boolean = true,
    val notificationsChannels: Boolean = true,
    val inAppSounds: Boolean = true,
    val inAppVibrate: Boolean = true,
    val inAppPreview: Boolean = true,
    val badgeCounterType: String = "Mensajes no leídos", // "Mensajes no leídos", "Chats no leídos"
    val badgeIncludeMuted: Boolean = false,
    val inAppSoundsEffects: Boolean = true,

    val lastSeenPrivacy: String = "Todos", // "Todos", "Mis contactos", "Nadie"
    val phoneNumberPrivacy: String = "Mis contactos",
    val groupInvitePrivacy: String = "Todos",
    val profilePhotoPrivacy: String = "Todos",
    val forwardPrivacy: String = "Todos",
    val accountSelfDestructPeriod: String = "6 meses", // "1 mes", "3 meses", "6 meses", "1 año"

    val autoDownloadMobile: Boolean = false,
    val autoDownloadWifi: Boolean = true,
    val autoDownloadRoaming: Boolean = false,
    val keepMediaDuration: String = "1 mes", // "3 días", "1 semana", "1 mes", "Para siempre"
    val maxCacheSize: String = "10 GB", // "5 GB", "10 GB", "20 GB", "Sin límite"
    val autoPlayGifs: Boolean = true,
    val autoPlayVideos: Boolean = true,
    val cachedStorageMb: Int = 348,

    val chatFontSize: Int = 16,
    val bubbleCornerRadius: Int = 16,
    val inAppBrowser: Boolean = true,
    val loopAnimatedStickers: Boolean = true,
    val saveToGalleryPrivate: Boolean = false,
    val saveToGalleryGroups: Boolean = false,
    val saveToGalleryChannels: Boolean = false,
    val autoNightMode: String = "Desactivado", // "Desactivado", "Programado", "Adaptativo"
    val language: String = "Español"
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

            // Krono Exclusive
            translationProvider = prefs.getString("trans_provider", "Google Translate") ?: "Google Translate",
            translationTargetLang = prefs.getString("trans_target_lang", "Español") ?: "Español",
            enableChatTranslation = prefs.getBoolean("trans_enable_chat", true),
            translateBeforeSend = prefs.getBoolean("trans_before_send", false),
            forwardWithoutQuote = prefs.getBoolean("fwd_no_quote", true),
            oneTapForward = prefs.getBoolean("fwd_one_tap", false),
            doubleTapAction = prefs.getString("double_tap_action", "Responder") ?: "Responder",
            voiceToTextEnabled = prefs.getBoolean("voice_to_text", true),
            useSystemEmojis = prefs.getBoolean("use_system_emojis", false),
            useSystemFonts = prefs.getBoolean("use_system_fonts", false),
            transparentStatusBar = prefs.getBoolean("transparent_status_bar", true),
            hideKeyboardOnScroll = prefs.getBoolean("hide_keyboard_scroll", true),
            hidePhoneInDrawer = prefs.getBoolean("hide_phone_drawer", false),
            avatarDrawerBackground = prefs.getBoolean("avatar_drawer_bg", false),
            stickerSize = prefs.getString("sticker_size", "Normal") ?: "Normal",
            autoPauseVideos = prefs.getBoolean("auto_pause_videos", true),
            unlimitedFavoriteStickers = prefs.getBoolean("unlimited_fav_stickers", true),
            confirmBeforeCall = prefs.getBoolean("confirm_call", true),
            ignoreBlockedUsersInGroups = prefs.getBoolean("ignore_blocked_groups", true),
            localEditHistory = prefs.getBoolean("local_edit_history", true),
            useMonetColors = prefs.getBoolean("use_monet_colors", false),

            // Telegram Native
            notificationsPrivateChats = prefs.getBoolean("notif_private", true),
            notificationsGroups = prefs.getBoolean("notif_groups", true),
            notificationsChannels = prefs.getBoolean("notif_channels", true),
            inAppSounds = prefs.getBoolean("in_app_sounds", true),
            inAppVibrate = prefs.getBoolean("in_app_vibrate", true),
            inAppPreview = prefs.getBoolean("in_app_preview", true),
            badgeCounterType = prefs.getString("badge_counter_type", "Mensajes no leídos") ?: "Mensajes no leídos",
            badgeIncludeMuted = prefs.getBoolean("badge_include_muted", false),
            inAppSoundsEffects = prefs.getBoolean("in_app_sound_effects", true),

            lastSeenPrivacy = prefs.getString("last_seen_privacy", "Todos") ?: "Todos",
            phoneNumberPrivacy = prefs.getString("phone_privacy", "Mis contactos") ?: "Mis contactos",
            groupInvitePrivacy = prefs.getString("group_invite_privacy", "Todos") ?: "Todos",
            profilePhotoPrivacy = prefs.getString("photo_privacy", "Todos") ?: "Todos",
            forwardPrivacy = prefs.getString("forward_privacy", "Todos") ?: "Todos",
            accountSelfDestructPeriod = prefs.getString("self_destruct_period", "6 meses") ?: "6 meses",

            autoDownloadMobile = prefs.getBoolean("auto_mobile", false),
            autoDownloadWifi = prefs.getBoolean("auto_wifi", true),
            autoDownloadRoaming = prefs.getBoolean("auto_roaming", false),
            keepMediaDuration = prefs.getString("keep_media_duration", "1 mes") ?: "1 mes",
            maxCacheSize = prefs.getString("max_cache_size", "10 GB") ?: "10 GB",
            autoPlayGifs = prefs.getBoolean("auto_play_gifs", true),
            autoPlayVideos = prefs.getBoolean("auto_play_videos", true),
            cachedStorageMb = prefs.getInt("cached_storage_mb", 348),

            chatFontSize = prefs.getInt("chat_font_size", 16),
            bubbleCornerRadius = prefs.getInt("bubble_corner_radius", 16),
            inAppBrowser = prefs.getBoolean("in_app_browser", true),
            loopAnimatedStickers = prefs.getBoolean("loop_animated_stickers", true),
            saveToGalleryPrivate = prefs.getBoolean("save_gallery_private", false),
            saveToGalleryGroups = prefs.getBoolean("save_gallery_groups", false),
            saveToGalleryChannels = prefs.getBoolean("save_gallery_channels", false),
            autoNightMode = prefs.getString("auto_night_mode", "Desactivado") ?: "Desactivado",
            language = prefs.getString("app_language", "Español") ?: "Español"
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

    fun updateAccountInfo(phone: String, username: String, displayName: String) {
        saveAccountInfo(phone, username, displayName)
    }

    // Krono Exclusive Setters
    fun setKronoFeature(key: String, value: Any) {
        val editor = prefs.edit()
        when (key) {
            "trans_provider" -> { editor.putString("trans_provider", value as String); _settingsState.value = _settingsState.value.copy(translationProvider = value) }
            "trans_target_lang" -> { editor.putString("trans_target_lang", value as String); _settingsState.value = _settingsState.value.copy(translationTargetLang = value) }
            "trans_enable_chat" -> { editor.putBoolean("trans_enable_chat", value as Boolean); _settingsState.value = _settingsState.value.copy(enableChatTranslation = value) }
            "trans_before_send" -> { editor.putBoolean("trans_before_send", value as Boolean); _settingsState.value = _settingsState.value.copy(translateBeforeSend = value) }
            "fwd_no_quote" -> { editor.putBoolean("fwd_no_quote", value as Boolean); _settingsState.value = _settingsState.value.copy(forwardWithoutQuote = value) }
            "fwd_one_tap" -> { editor.putBoolean("fwd_one_tap", value as Boolean); _settingsState.value = _settingsState.value.copy(oneTapForward = value) }
            "double_tap_action" -> { editor.putString("double_tap_action", value as String); _settingsState.value = _settingsState.value.copy(doubleTapAction = value) }
            "voice_to_text" -> { editor.putBoolean("voice_to_text", value as Boolean); _settingsState.value = _settingsState.value.copy(voiceToTextEnabled = value) }
            "use_system_emojis" -> { editor.putBoolean("use_system_emojis", value as Boolean); _settingsState.value = _settingsState.value.copy(useSystemEmojis = value) }
            "use_system_fonts" -> { editor.putBoolean("use_system_fonts", value as Boolean); _settingsState.value = _settingsState.value.copy(useSystemFonts = value) }
            "transparent_status_bar" -> { editor.putBoolean("transparent_status_bar", value as Boolean); _settingsState.value = _settingsState.value.copy(transparentStatusBar = value) }
            "hide_keyboard_scroll" -> { editor.putBoolean("hide_keyboard_scroll", value as Boolean); _settingsState.value = _settingsState.value.copy(hideKeyboardOnScroll = value) }
            "hide_phone_drawer" -> { editor.putBoolean("hide_phone_drawer", value as Boolean); _settingsState.value = _settingsState.value.copy(hidePhoneInDrawer = value) }
            "avatar_drawer_bg" -> { editor.putBoolean("avatar_drawer_bg", value as Boolean); _settingsState.value = _settingsState.value.copy(avatarDrawerBackground = value) }
            "sticker_size" -> { editor.putString("sticker_size", value as String); _settingsState.value = _settingsState.value.copy(stickerSize = value) }
            "auto_pause_videos" -> { editor.putBoolean("auto_pause_videos", value as Boolean); _settingsState.value = _settingsState.value.copy(autoPauseVideos = value) }
            "unlimited_fav_stickers" -> { editor.putBoolean("unlimited_fav_stickers", value as Boolean); _settingsState.value = _settingsState.value.copy(unlimitedFavoriteStickers = value) }
            "confirm_call" -> { editor.putBoolean("confirm_call", value as Boolean); _settingsState.value = _settingsState.value.copy(confirmBeforeCall = value) }
            "ignore_blocked_groups" -> { editor.putBoolean("ignore_blocked_groups", value as Boolean); _settingsState.value = _settingsState.value.copy(ignoreBlockedUsersInGroups = value) }
            "local_edit_history" -> { editor.putBoolean("local_edit_history", value as Boolean); _settingsState.value = _settingsState.value.copy(localEditHistory = value) }
            "use_monet", "use_monet_colors" -> { editor.putBoolean("use_monet_colors", value as Boolean); _settingsState.value = _settingsState.value.copy(useMonetColors = value) }
        }
        editor.apply()
    }

    fun toggleMonetColors(enabled: Boolean) {
        prefs.edit().putBoolean("use_monet_colors", enabled).apply()
        _settingsState.value = _settingsState.value.copy(useMonetColors = enabled)
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
            "badgeMuted" -> { editor.putBoolean("badge_include_muted", enabled); _settingsState.value = _settingsState.value.copy(badgeIncludeMuted = enabled) }
            "soundEffects" -> { editor.putBoolean("in_app_sound_effects", enabled); _settingsState.value = _settingsState.value.copy(inAppSoundsEffects = enabled) }
        }
        editor.apply()
    }

    fun setNotificationBadgeType(type: String) {
        prefs.edit().putString("badge_counter_type", type).apply()
        _settingsState.value = _settingsState.value.copy(badgeCounterType = type)
    }

    fun setPrivacySetting(setting: String, value: String) {
        val editor = prefs.edit()
        when (setting) {
            "lastSeen" -> { editor.putString("last_seen_privacy", value); _settingsState.value = _settingsState.value.copy(lastSeenPrivacy = value) }
            "phone" -> { editor.putString("phone_privacy", value); _settingsState.value = _settingsState.value.copy(phoneNumberPrivacy = value) }
            "groupInvite" -> { editor.putString("group_invite_privacy", value); _settingsState.value = _settingsState.value.copy(groupInvitePrivacy = value) }
            "profilePhoto" -> { editor.putString("photo_privacy", value); _settingsState.value = _settingsState.value.copy(profilePhotoPrivacy = value) }
            "forward" -> { editor.putString("forward_privacy", value); _settingsState.value = _settingsState.value.copy(forwardPrivacy = value) }
            "selfDestruct" -> { editor.putString("self_destruct_period", value); _settingsState.value = _settingsState.value.copy(accountSelfDestructPeriod = value) }
        }
        editor.apply()
    }

    fun setAutoDownload(type: String, enabled: Boolean) {
        val editor = prefs.edit()
        when (type) {
            "mobile" -> { editor.putBoolean("auto_mobile", enabled); _settingsState.value = _settingsState.value.copy(autoDownloadMobile = enabled) }
            "wifi" -> { editor.putBoolean("auto_wifi", enabled); _settingsState.value = _settingsState.value.copy(autoDownloadWifi = enabled) }
            "roaming" -> { editor.putBoolean("auto_roaming", enabled); _settingsState.value = _settingsState.value.copy(autoDownloadRoaming = enabled) }
            "gifs" -> { editor.putBoolean("auto_play_gifs", enabled); _settingsState.value = _settingsState.value.copy(autoPlayGifs = enabled) }
            "videos" -> { editor.putBoolean("auto_play_videos", enabled); _settingsState.value = _settingsState.value.copy(autoPlayVideos = enabled) }
        }
        editor.apply()
    }

    fun setDataStorageOptions(keepDuration: String, maxCache: String) {
        prefs.edit()
            .putString("keep_media_duration", keepDuration)
            .putString("max_cache_size", maxCache)
            .apply()
        _settingsState.value = _settingsState.value.copy(
            keepMediaDuration = keepDuration,
            maxCacheSize = maxCache
        )
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

    fun setChatSetting(key: String, value: Any) {
        val editor = prefs.edit()
        when (key) {
            "in_app_browser" -> { editor.putBoolean("in_app_browser", value as Boolean); _settingsState.value = _settingsState.value.copy(inAppBrowser = value) }
            "loop_animated_stickers" -> { editor.putBoolean("loop_animated_stickers", value as Boolean); _settingsState.value = _settingsState.value.copy(loopAnimatedStickers = value) }
            "save_gallery_private" -> { editor.putBoolean("save_gallery_private", value as Boolean); _settingsState.value = _settingsState.value.copy(saveToGalleryPrivate = value) }
            "save_gallery_groups" -> { editor.putBoolean("save_gallery_groups", value as Boolean); _settingsState.value = _settingsState.value.copy(saveToGalleryGroups = value) }
            "save_gallery_channels" -> { editor.putBoolean("save_gallery_channels", value as Boolean); _settingsState.value = _settingsState.value.copy(saveToGalleryChannels = value) }
            "auto_night_mode" -> { editor.putString("auto_night_mode", value as String); _settingsState.value = _settingsState.value.copy(autoNightMode = value) }
        }
        editor.apply()
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
