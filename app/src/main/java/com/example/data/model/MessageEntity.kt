package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val chatId: Long,
    val senderId: Long,
    val senderName: String,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isOutgoing: Boolean = true,
    val deliveryStatus: String = "SENT", // "SENDING", "SENT", "READ"
    val isEncrypted: Boolean = false,
    val scheduledTimestamp: Long? = null,
    val autoDestructSeconds: Int? = null,
    val isDestructed: Boolean = false
)
