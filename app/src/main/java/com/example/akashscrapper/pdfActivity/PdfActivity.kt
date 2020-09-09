package com.example.akashscrapper.pdfActivity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.akashscrapper.R
import com.example.akashscrapper.utils.*
import kotlinx.android.synthetic.main.activity_pdf.*
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class PdfActivity : AppCompatActivity() {

    companion object {
        fun getInstance(context: Context, fileUrl: String, fileId: Int, fileName: String): Intent {
            return Intent(context, PdfActivity::class.java).apply {
                putExtra(FILE_URL, fileUrl)
                putExtra(FILE_NAME, fileName)
                putExtra(FILE_ID, fileId)
            }
        }
    }

    val vm: PdfViewModel by stateViewModel()

    lateinit var fileName: String
    lateinit var fileUrl: String
    var fileId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf)

        if (savedInstanceState == null) {
            fileName = "${intent.getStringExtra(FILE_NAME)}.pdf"
            fileUrl = intent.getStringExtra(FILE_URL)
            fileId = intent.getIntExtra(FILE_ID, 0)
        }

        vm.getFileById(fileId).observe(this) {
            if (it != null) {
                Log.e("TAG", "onCreate: Already Downloaded ${it.fileName}")
            } else {
                downloadFileAndShow()
            }
        }

        downloadFileAndShow()

    }

    fun downloadFileAndShow() {
        if (!applicationContext.checkPermission()) {
            //TODO() = Handle Storage Permission properly
            parentView.showSnackbar("Storage Permission not provided")
            return
        }

        DownloadPdfService.startService(
            applicationContext,
            fileUrl,
            fileId,
            fileName
        )
    }

}