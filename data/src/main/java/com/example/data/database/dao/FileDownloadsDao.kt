package com.example.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.data.database.FileDownloadModel

@Dao
interface FileDownloadsDao :
    BaseDao<FileDownloadModel> {

    @Query("SELECT * FROM FileDownloadModel WHERE fileId = :id")
    fun getFile(id: Int): LiveData<FileDownloadModel?>

    @Query("UPDATE FileDownloadModel SET isDownloaded = :downloaded WHERE fileId = :id")
    suspend fun fileDownloaded(downloaded: Boolean, id: Int)

    @Query("DELETE FROM FileDownloadModel WHERE fileId = :id")
    fun deleteFile(id: Int)

    @Query("SELECT * FROM FileDownloadModel WHERE isDownloaded = 1 LIMIT 5")
    fun getDownloaded(): LiveData<List<FileDownloadModel>>

    @Query("SELECT * FROM FileDownloadModel WHERE isWishlisted = 1 LIMIT 5")
    fun getAllWishlisted(): LiveData<List<FileDownloadModel>>

    @Query("SELECT * FROM FileDownloadModel WHERE fileId = :id")
    suspend fun getWishlist(id: Int): FileDownloadModel?

    @Query("UPDATE FileDownloadModel SET isWishlisted = :wishlist WHERE fileId = :id")
    suspend fun setWishlist(wishlist: Boolean, id: Int)

    @Query("UPDATE FileDownloadModel SET lastVisited = :currentTimeMillis WHERE fileId = :id")
    suspend fun updateTime(id: Int, currentTimeMillis: Long)

}