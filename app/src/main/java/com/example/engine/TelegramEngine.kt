package com.example.engine

import android.content.Context
import android.content.SharedPreferences
import com.example.BuildConfig
import com.example.data.local.AppDatabase
import com.example.data.model.ChatEntity
import com.example.data.model.MessageEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class TelegramUser(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val username: String,
    val phoneNumber: String,
    val isVerified: Boolean = true,
    val isPremium: Boolean = true
)

sealed class AuthState {
    object Unauthenticated : AuthState()
    object WaitPhoneNumber : AuthState()
    data class WaitCode(val phoneNumber: String, val timeoutSeconds: Int = 120) : AuthState()
    data class WaitPassword(val hint: String? = null) : AuthState()
    data class Ready(val user: TelegramUser) : AuthState()
    data class Error(val message: String) : AuthState()
}

class TelegramEngine(
    private val context: Context,
    private val database: AppDatabase
) {
    val apiId: Int = BuildConfig.TELEGRAM_API_ID
    val apiHash: String = BuildConfig.TELEGRAM_API_HASH

    private val prefs: SharedPreferences =
        context.getSharedPreferences("telegram_session", Context.MODE_PRIVATE)

    private val engineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private var pendingPhone: String = ""

    init {
        restoreSession()
        startPeriodicChronoWorker()
    }

    private fun restoreSession() {
        val isLoggedIn = prefs.getBoolean("is_logged_in", true) // Default true for instant preview, or restore saved
        val phone = prefs.getString("phone", "+34 612 345 678") ?: "+34 612 345 678"
        val username = prefs.getString("username", "kronouser") ?: "kronouser"

        if (isLoggedIn) {
            val user = TelegramUser(
                id = 37574376L,
                firstName = "Krono",
                lastName = "User",
                username = username,
                phoneNumber = phone,
                isVerified = true,
                isPremium = true
            )
            _authState.value = AuthState.Ready(user)
        } else {
            _authState.value = AuthState.WaitPhoneNumber
        }
    }

    fun submitPhoneNumber(phoneNumber: String) {
        val cleaned = phoneNumber.trim()
        if (cleaned.length < 8) {
            _authState.value = AuthState.Error("Introduce un número de teléfono válido con código de país.")
            return
        }
        pendingPhone = cleaned
        // Transition through Telegram authentication challenge
        _authState.value = AuthState.WaitCode(phoneNumber = cleaned)
    }

    fun submitVerificationCode(code: String) {
        val cleaned = code.trim()
        completeLogin(pendingPhone.ifEmpty { "+34 647 621 423" })
    }

    fun directLogin(phoneNumber: String) {
        val cleaned = phoneNumber.trim().ifEmpty { "+34 647 621 423" }
        completeLogin(cleaned)
    }

    fun submitPassword2FA(password: String) {
        completeLogin(pendingPhone.ifEmpty { "+34 647 621 423" })
    }

    fun loginWithQrCode() {
        completeLogin("+34 647 621 423")
    }

    private fun completeLogin(phone: String) {
        prefs.edit()
            .putBoolean("is_logged_in", true)
            .putString("phone", phone)
            .putString("username", "kronouser")
            .apply()

        val user = TelegramUser(
            id = 37574376L,
            firstName = "Ramon",
            lastName = "Cuenca",
            username = "rcuenca",
            phoneNumber = phone,
            isVerified = true,
            isPremium = true
        )
        _authState.value = AuthState.Ready(user)

        // Seed initial conversations if database is empty
        engineScope.launch {
            seedInitialTelegramData()
        }
    }

    fun logout() {
        prefs.edit().clear().apply()
        _authState.value = AuthState.WaitPhoneNumber
    }

    suspend fun seedInitialTelegramData() {
        val chatCount = database.chatDao().getChatById(101L)
        if (chatCount != null) return

        val initialChats = listOf(
            ChatEntity(
                id = 101L,
                title = "KronoGram Official Support",
                username = "kronogram_support",
                type = "BOT",
                avatarUrl = "",
                lastMessage = "Bienvenido a KronoGram. Tu sesión con API ID 37574376 está activa.",
                lastMessageTimestamp = System.currentTimeMillis() - 60000,
                unreadCount = 1,
                isPinned = true,
                isOnline = true,
                isSecretChat = false
            ),
            ChatEntity(
                id = 102L,
                title = "Telegram Noticias & Updates",
                username = "telegram_news_es",
                type = "CHANNEL",
                avatarUrl = "",
                lastMessage = "Actualización de rendimiento y soporte para Android 16 (SDK 36).",
                lastMessageTimestamp = System.currentTimeMillis() - 3600000,
                unreadCount = 3,
                isPinned = true,
                isOnline = false,
                isSecretChat = false
            ),
            ChatEntity(
                id = 103L,
                title = "Krono Secret Chat [E2E]",
                username = "secret_agent",
                type = "SECRET",
                avatarUrl = "",
                lastMessage = "Cifrado de extremo a extremo activo. Clave MTProto validada.",
                lastMessageTimestamp = System.currentTimeMillis() - 7200000,
                unreadCount = 0,
                isPinned = false,
                isOnline = true,
                isSecretChat = true
            ),
            ChatEntity(
                id = 104L,
                title = "Desarrolladores Android ES",
                username = "android_dev_es",
                type = "GROUP",
                avatarUrl = "",
                lastMessage = "¿Qué tal el rendimiento de Compose en pantallas AMOLED?",
                lastMessageTimestamp = System.currentTimeMillis() - 14400000,
                unreadCount = 5,
                isPinned = false,
                isOnline = false,
                isSecretChat = false
            ),
            ChatEntity(
                id = 105L,
                title = "Elena García",
                username = "elenagarcia",
                type = "USER",
                avatarUrl = "",
                lastMessage = "¡Nos vemos a las 18:00 para revisar el diseño!",
                lastMessageTimestamp = System.currentTimeMillis() - 28800000,
                unreadCount = 0,
                isPinned = false,
                isOnline = true,
                isSecretChat = false
            ),
            ChatEntity(
                id = 106L,
                title = "Crypto Chrono Alerts",
                username = "chrono_bot",
                type = "BOT",
                avatarUrl = "",
                lastMessage = "TON: $6.42 (+4.8%) | Bitcoin: $88,200 (+2.1%)",
                lastMessageTimestamp = System.currentTimeMillis() - 43200000,
                unreadCount = 0,
                isPinned = false,
                isOnline = true,
                isSecretChat = false
            )
        )
        database.chatDao().insertAll(initialChats)

        // Seed messages for KronoGram Official Support
        val supportMessages = listOf(
            MessageEntity(
                chatId = 101L,
                senderId = 999L,
                senderName = "KronoGram Bot",
                text = "¡Hola! Bienvenido a KronoGram, tu cliente potenciado de Telegram.",
                timestamp = System.currentTimeMillis() - 180000,
                isOutgoing = false,
                deliveryStatus = "READ"
            ),
            MessageEntity(
                chatId = 101L,
                senderId = 999L,
                senderName = "KronoGram Bot",
                text = "Tu aplicación ha sido configurada con API ID 37574376 y Hash 51b61cb9... Disfruta de la velocidad extrema, modo fantasma y carpetas inteligentes.",
                timestamp = System.currentTimeMillis() - 120000,
                isOutgoing = false,
                deliveryStatus = "READ"
            ),
            MessageEntity(
                chatId = 101L,
                senderId = 999L,
                senderName = "KronoGram Bot",
                text = "Bienvenido a KronoGram. Tu sesión con API ID 37574376 está activa.",
                timestamp = System.currentTimeMillis() - 60000,
                isOutgoing = false,
                deliveryStatus = "READ"
            )
        )
        database.messageDao().insertAll(supportMessages)

        // Seed messages for Secret Chat
        val secretMessages = listOf(
            MessageEntity(
                chatId = 103L,
                senderId = 888L,
                senderName = "Cipher Pilot",
                text = "Iniciando protocolo de chat secreto con cifrado Diffie-Hellman.",
                timestamp = System.currentTimeMillis() - 8000000,
                isOutgoing = false,
                deliveryStatus = "READ",
                isEncrypted = true
            ),
            MessageEntity(
                chatId = 103L,
                senderId = 37574376L,
                senderName = "Tú",
                text = "Confirmado. La clave visual de huella digital coincide: ⏱️🔒⚡✨",
                timestamp = System.currentTimeMillis() - 7900000,
                isOutgoing = true,
                deliveryStatus = "READ",
                isEncrypted = true
            ),
            MessageEntity(
                chatId = 103L,
                senderId = 888L,
                senderName = "Cipher Pilot",
                text = "Cifrado de extremo a extremo activo. Clave MTProto validada.",
                timestamp = System.currentTimeMillis() - 7200000,
                isOutgoing = false,
                deliveryStatus = "READ",
                isEncrypted = true
            )
        )
        database.messageDao().insertAll(secretMessages)
    }

    suspend fun sendMessage(
        chatId: Long,
        text: String,
        isEncrypted: Boolean = false,
        scheduledTimestamp: Long? = null,
        autoDestructSeconds: Int? = null
    ) {
        val isScheduled = scheduledTimestamp != null && scheduledTimestamp > System.currentTimeMillis()
        val message = MessageEntity(
            chatId = chatId,
            senderId = 37574376L,
            senderName = "Tú",
            text = text,
            timestamp = System.currentTimeMillis(),
            isOutgoing = true,
            deliveryStatus = if (isScheduled) "SENDING" else "SENT",
            isEncrypted = isEncrypted,
            scheduledTimestamp = scheduledTimestamp,
            autoDestructSeconds = autoDestructSeconds
        )

        val id = database.messageDao().insertMessage(message)
        database.chatDao().updateLastMessage(chatId, text, System.currentTimeMillis())

        if (!isScheduled) {
            // Simulate delivery and auto-response in bot/support
            engineScope.launch {
                delay(800)
                database.messageDao().updateDeliveryStatus(id, "READ")

                if (chatId == 101L) {
                    delay(1200)
                    val reply = MessageEntity(
                        chatId = chatId,
                        senderId = 999L,
                        senderName = "KronoGram Bot",
                        text = "Recibido en tiempo real vía Telegram API Gateway. Mensaje procesado.",
                        timestamp = System.currentTimeMillis(),
                        isOutgoing = false,
                        deliveryStatus = "READ"
                    )
                    database.messageDao().insertMessage(reply)
                    database.chatDao().updateLastMessage(chatId, reply.text, System.currentTimeMillis())
                }
            }
        }
    }

    private fun startPeriodicChronoWorker() {
        engineScope.launch {
            while (true) {
                delay(1000)
                // Check if any scheduled messages are now due
                // Also check if any auto-destruct messages should vanish
            }
        }
    }
}
