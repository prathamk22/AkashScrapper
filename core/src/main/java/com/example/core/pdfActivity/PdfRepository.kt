package com.example.core.pdfActivity

import com.example.data.database.dao.FileDownloadsDao

class PdfRepository(
    private val fileDownloadsDao: FileDownloadsDao
) {

    fun getFileById(id: Int) = fileDownloadsDao.getFile(id)

    fun deleteFile(id: Int) = fileDownloadsDao.deleteFile(id)

    suspend fun updateTime(id: Int) = fileDownloadsDao.updateTime(id, System.currentTimeMillis())
}