package com.example.akashscrapper.pdfActivity

import com.example.akashscrapper.utils.BaseViewModel

class PdfViewModel(
    private val repo: PdfRepository
) : BaseViewModel() {

    fun getFileById(id: Int) = repo.getFileById(id)

}