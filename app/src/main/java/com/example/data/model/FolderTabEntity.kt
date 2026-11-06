package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "folder_tabs")
data class FolderTabEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val iconKey: String, // "all", "unread", "personal", "groups", "channels", "bots", "custom"
    val orderIndex: Int,
    val isEnabled: Boolean = true,
    val isCustom: Boolean = false,
    val filterType: String = "ALL", // ALL, UNREAD, PERSONAL, GROUPS, CHANNELS, BOTS, CUSTOM
    val includedChatIds: String = "" // comma-separated chat IDs for custom folders
)
