package com.example.akashscrapper.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

@Dao
interface FileDownloadsDao : BaseDao<FileDownloadModel> {

    @Query("SELECT * FROM FileDownloadModel WHERE fileId = :id")
    fun getFile(id: Int): LiveData<FileDownloadModel>

    @Query("UPDATE FileDownloadModel SET isDownloaded = :downloaded WHERE fileId = :id")
    suspend fun fileDownloaded(downloaded: Boolean, id: Int)

    @Query("DELETE FROM FileDownloadModel WHERE fileId = :id")
    fun deleteFile(id: Int)

}