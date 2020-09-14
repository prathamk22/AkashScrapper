package com.example.akashscrapper.database.dao

import androidx.lifecycle.LiveData
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

    @Query("SELECT * FROM FileData WHERE isWishlisted = :wishlist")
    fun getWishlisted(wishlist: Boolean = true): LiveData<List<FileData>>

    @Query("UPDATE FileData SET isWishlisted = :wishlist WHERE document_id = :id")
    suspend fun setWishlist(wishlist: Boolean, id: Int)

    @Query("SELECT * FROM FileData WHERE document_id = :id")
    suspend fun getWishlist(id: Int): FileData

    @Query("UPDATE FileData SET isDownloaded = :downloaded WHERE document_id = :id")
    suspend fun fileDownloaded(downloaded: Boolean, id: Int)

    @Query("UPDATE FileData SET isDownloaded = 0 WHERE document_id = :id")
    fun deleteFile(id: Int)

    @Query("SELECT * FROM FileData WHERE document_id = :id")
    fun getFile(id: Int): LiveData<FileData>

    @Query("SELECT * FROM FileData WHERE isDownloaded = 1 LIMIT 5")
    fun getDownloaded(): LiveData<List<FileData>>

    @Query("SELECT * FROM FileData WHERE isWishlisted = 1 LIMIT 5")
    fun getAllWishlisted(): LiveData<List<FileData>>

}