package com.example.akashscrapper.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.example.akashscrapper.database.FileData

@Dao
interface FileDataDao : BaseDao<FileData> {

    @Query("SELECT * FROM FileData WHERE document_title = :key")
    fun getFiles(key: String): PagingSource<Int, FileData>

    @Query("DELETE FROM FileData")
    suspend fun clearTable()

}