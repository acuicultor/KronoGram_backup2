package com.example.data.repository

import com.example.data.local.FolderDao
import com.example.data.model.FolderTabEntity
import kotlinx.coroutines.flow.Flow

class FolderRepository(private val folderDao: FolderDao) {
    val activeFolders: Flow<List<FolderTabEntity>> = folderDao.getActiveFolders()
    val allFolders: Flow<List<FolderTabEntity>> = folderDao.getAllFolders()

    suspend fun initializeDefaultFolders(customSelection: List<String>? = null) {
        val existingCount = folderDao.getFolderCount()
        if (existingCount > 0 && customSelection == null) return

        val defaultTabs = listOf(
            FolderTabEntity(
                id = "all",
                title = "Todos",
                iconKey = "all",
                orderIndex = 0,
                isEnabled = customSelection?.contains("all") ?: true,
                filterType = "ALL"
            ),
            FolderTabEntity(
                id = "unread",
                title = "No leídos",
                iconKey = "unread",
                orderIndex = 1,
                isEnabled = customSelection?.contains("unread") ?: true,
                filterType = "UNREAD"
            ),
            FolderTabEntity(
                id = "personal",
                title = "Personales",
                iconKey = "personal",
                orderIndex = 2,
                isEnabled = customSelection?.contains("personal") ?: true,
                filterType = "PERSONAL"
            ),
            FolderTabEntity(
                id = "groups",
                title = "Grupos",
                iconKey = "groups",
                orderIndex = 3,
                isEnabled = customSelection?.contains("groups") ?: true,
                filterType = "GROUPS"
            ),
            FolderTabEntity(
                id = "channels",
                title = "Canales",
                iconKey = "channels",
                orderIndex = 4,
                isEnabled = customSelection?.contains("channels") ?: true,
                filterType = "CHANNELS"
            ),
            FolderTabEntity(
                id = "bots",
                title = "Bots",
                iconKey = "bots",
                orderIndex = 5,
                isEnabled = customSelection?.contains("bots") ?: true,
                filterType = "BOTS"
            )
        )

        folderDao.insertAll(defaultTabs)
    }

    suspend fun toggleFolder(id: String, enabled: Boolean) {
        folderDao.toggleFolder(id, enabled)
    }

    suspend fun addCustomFolder(title: String, filterType: String) {
        val count = folderDao.getFolderCount()
        val newFolder = FolderTabEntity(
            id = "custom_${System.currentTimeMillis()}",
            title = title,
            iconKey = "custom",
            orderIndex = count,
            isEnabled = true,
            isCustom = true,
            filterType = filterType
        )
        folderDao.insertFolder(newFolder)
    }

    suspend fun deleteFolder(id: String) {
        folderDao.deleteFolder(id)
    }
}
