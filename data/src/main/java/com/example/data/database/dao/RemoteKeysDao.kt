package com.example.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.data.database.RemoteKeys

@Dao
interface RemoteKeysDao : BaseDao<RemoteKeys> {

    @Query("SELECT * FROM RemoteKeys WHERE fileId = :fileId")
    suspend fun remoteKeysRepoId(fileId: Int): RemoteKeys?

    @Query("SELECT * FROM RemoteKeys WHERE subjectKey = :key ORDER BY nextKey DESC")
    suspend fun getRedditKeys(key: String): List<RemoteKeys>

    @Query("DELETE FROM RemoteKeys WHERE subjectKey = :key")
    suspend fun nukeTable(key: String)

}