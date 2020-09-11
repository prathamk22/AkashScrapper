package com.example.akashscrapper.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.akashscrapper.database.FileDownloadModel

@Dao
interface FileDownloadsDao : BaseDao<FileDownloadModel> {

    @Query("SELECT * FROM FileDownloadModel WHERE fileId = :id")
    fun getFile(id: Int): LiveData<FileDownloadModel>

    @Query("UPDATE FileDownloadModel SET isDownloaded = :downloaded WHERE fileId = :id")
    suspend fun fileDownloaded(downloaded: Boolean, id: Int)

    @Query("DELETE FROM FileDownloadModel WHERE fileId = :id")
    fun deleteFile(id: Int)

    @Query("SELECT * FROM FileDownloadModel WHERE isWishlisted = :wishlist")
    fun getWishlisted(wishlist: Boolean = true): LiveData<List<FileDownloadModel>>

    @Query("UPDATE FileDownloadModel SET isWishlisted = :wishlist WHERE fileId = :id")
    suspend fun setWishlist(wishlist: Boolean, id: Int)

    @Query("SELECT * FROM FileDownloadModel WHERE fileId = :id")
    suspend fun getWishlist(id: Int): FileDownloadModel

}