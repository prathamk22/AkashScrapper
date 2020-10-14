package com.example.data.database.dao

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.example.data.database.FileData

@Dao
interface FileDataDao : BaseDao<FileData> {

    @Query("SELECT * FROM FileData WHERE subject_id = :subjectId")
    fun getFiles(subjectId: Int): PagingSource<Int, FileData>

    @Query("DELETE FROM FileData")
    suspend fun clearTable()

    @Query("SELECT * FROM FileData WHERE document_id = :id")
    suspend fun getWishlist(id: Int): FileData

    @Query("SELECT * FROM FileData WHERE document_id = :id")
    fun getFile(id: Int): LiveData<FileData>

    @Query("SELECT * FROM FileData WHERE document_id = :id")
    fun isAvailable(id: Int): FileData?

}