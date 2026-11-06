package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chats")
data class ChatEntity(
    @PrimaryKey
    val id: Long,
    val title: String,
    val username: String = "",
    val type: String = "USER", // "USER", "GROUP", "CHANNEL", "BOT", "SECRET"
    val avatarUrl: String = "",
    val lastMessage: String = "",
    val lastMessageTimestamp: Long = System.currentTimeMillis(),
    val unreadCount: Int = 0,
    val isPinned: Boolean = false,
    val isMuted: Boolean = false,
    val isOnline: Boolean = false,
    val isSecretChat: Boolean = false,
    val draftMessage: String? = null
)
