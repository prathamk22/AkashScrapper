package com.example.akashscrapper.pdfActivity

import com.example.akashscrapper.database.dao.FileDataDao

class PdfRepository(
    private val fileDataDao: FileDataDao
) {

    fun getFileById(id: Int) = fileDataDao.getFile(id)

    fun deleteFile(id: Int) = fileDataDao.deleteFile(id)
}