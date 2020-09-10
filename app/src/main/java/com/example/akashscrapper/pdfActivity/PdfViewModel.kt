package com.example.akashscrapper.pdfActivity

import com.example.akashscrapper.utils.BaseViewModel
import com.example.akashscrapper.utils.runIO

class PdfViewModel(
    private val repo: PdfRepository
) : BaseViewModel() {

    fun getFileById(id: Int) = repo.getFileById(id)

    fun deleteFile(id: Int) {
        runIO {
            repo.deleteFile(id)
        }
    }

}