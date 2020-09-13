package com.example.akashscrapper.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.akashscrapper.database.RemoteKeys

@Dao
interface RemoteKeysDao : BaseDao<RemoteKeys> {

    @Query("SELECT * FROM RemoteKeys WHERE fileId = :fileId")
    suspend fun remoteKeysRepoId(fileId: Int): RemoteKeys?

    @Query("DELETE FROM RemoteKeys")
    suspend fun clearRemoteKeys()

}
