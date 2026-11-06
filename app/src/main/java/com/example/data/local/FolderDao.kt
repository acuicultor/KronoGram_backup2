package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.FolderTabEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FolderDao {
    @Query("SELECT * FROM folder_tabs ORDER BY orderIndex ASC")
    fun getAllFolders(): Flow<List<FolderTabEntity>>

    @Query("SELECT * FROM folder_tabs WHERE isEnabled = 1 ORDER BY orderIndex ASC")
    fun getActiveFolders(): Flow<List<FolderTabEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFolder(folder: FolderTabEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(folders: List<FolderTabEntity>)

    @Update
    suspend fun updateFolder(folder: FolderTabEntity)

    @Query("UPDATE folder_tabs SET isEnabled = :isEnabled WHERE id = :id")
    suspend fun toggleFolder(id: String, isEnabled: Boolean)

    @Query("DELETE FROM folder_tabs WHERE id = :id")
    suspend fun deleteFolder(id: String)

    @Query("SELECT COUNT(*) FROM folder_tabs")
    suspend fun getFolderCount(): Int
}
