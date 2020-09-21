package com.example.akashscrapper.pdfActivity

import com.example.data.database.dao.FileDownloadsDao

class PdfRepository(
    private val fileDownloadsDao: com.example.data.database.dao.FileDownloadsDao
) {

    fun getFileById(id: Int) = fileDownloadsDao.getFile(id)

    fun deleteFile(id: Int) = fileDownloadsDao.deleteFile(id)

    suspend fun updateTime(id: Int) = fileDownloadsDao.updateTime(id, System.currentTimeMillis())
}