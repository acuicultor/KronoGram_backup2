package com.example.data.repository

import com.example.data.local.ChatDao
import com.example.data.local.MessageDao
import com.example.data.model.ChatEntity
import com.example.data.model.MessageEntity
import com.example.engine.TelegramEngine
import kotlinx.coroutines.flow.Flow

class ChatRepository(
    private val chatDao: ChatDao,
    private val messageDao: MessageDao,
    private val engine: TelegramEngine,
    private val preferencesRepository: KronoPreferencesRepository
) {
    val allChats: Flow<List<ChatEntity>> = chatDao.getAllChats()

    fun getMessagesForChat(chatId: Long): Flow<List<MessageEntity>> {
        return messageDao.getMessagesForChat(chatId)
    }

    fun getChatByIdFlow(chatId: Long): Flow<ChatEntity?> {
        return chatDao.getChatByIdFlow(chatId)
    }

    suspend fun getChatById(chatId: Long): ChatEntity? {
        return chatDao.getChatById(chatId)
    }

    suspend fun markChatAsRead(chatId: Long) {
        val isGhostMode = preferencesRepository.settingsState.value.isGhostMode
        // In Ghost Mode, we do not send read receipts to the remote server, but we can clear local badge
        chatDao.markChatAsRead(chatId)
    }

    suspend fun sendMessage(
        chatId: Long,
        text: String,
        isEncrypted: Boolean = false,
        scheduledTimestamp: Long? = null,
        autoDestructSeconds: Int? = null
    ) {
        engine.sendMessage(
            chatId = chatId,
            text = text,
            isEncrypted = isEncrypted,
            scheduledTimestamp = scheduledTimestamp,
            autoDestructSeconds = autoDestructSeconds
        )
    }

    suspend fun createNewChat(title: String, username: String, type: String): Long {
        val newId = System.currentTimeMillis()
        val chat = ChatEntity(
            id = newId,
            title = title,
            username = username,
            type = type,
            avatarUrl = "",
            lastMessage = "Conversación iniciada en KronoGram",
            lastMessageTimestamp = System.currentTimeMillis(),
            unreadCount = 0,
            isPinned = false,
            isOnline = true,
            isSecretChat = (type == "SECRET")
        )
        chatDao.insertOrUpdate(chat)
        return newId
    }
}
