package com.example.core.pdfActivity

import com.example.core.utils.BaseViewModel
import com.example.core.utils.runIO

class PdfViewModel(
    private val repo: PdfRepository
) : BaseViewModel() {

    fun getFileById(id: Int) = repo.getFileById(id)

    fun deleteFile(id: Int) {
        runIO {
            repo.deleteFile(id)
        }
    }

    fun updateTime(id: Int) {
        runIO {
            repo.updateTime(id)
        }
    }

}