package com.example.akashscrapper.pdfActivity

import com.example.akashscrapper.database.dao.FileDownloadsDao

class PdfRepository(
    private val filesDao: FileDownloadsDao
) {

    fun getFileById(id: Int) = filesDao.getFile(id)

    fun deleteFile(id: Int) = filesDao.deleteFile(id)

}