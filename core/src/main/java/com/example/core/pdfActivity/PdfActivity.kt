package com.example.core.pdfActivity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.observe
import com.example.core.R
import com.example.core.utils.*
import com.google.android.material.snackbar.Snackbar
import es.voghdev.pdfviewpager.library.PDFViewPager
import kotlinx.android.synthetic.main.activity_pdf.*
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import java.io.File

class PdfActivity : AppCompatActivity() {

    companion object {
        fun getInstance(
            context: Context,
            fileUrl: String,
            fileId: Int,
            fileName: String,
            subjectName: String
        ): Intent {
            return Intent(context, PdfActivity::class.java).apply {
                putExtra(FILE_URL, fileUrl)
                putExtra(FILE_NAME, fileName)
                putExtra(FILE_ID, fileId)
                putExtra(SUBJECT_NAME, subjectName)
            }
        }
    }

    val vm: PdfViewModel by stateViewModel()
    lateinit var pdfViewPager: PDFViewPager

    lateinit var fileName: String
    lateinit var subjectName: String
    lateinit var fileUrl: String
    var tempFile: File? = null
    var fileId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf)

        if (savedInstanceState == null) {
            fileName = "${intent.getStringExtra(FILE_NAME)}.pdf"
            fileUrl = intent.getStringExtra(FILE_URL)
            fileId = intent.getIntExtra(FILE_ID, 0)
            subjectName = intent.getStringExtra(SUBJECT_NAME)
        }

        vm.getFileById(fileId).observe(this) {
            if (it != null) {
                if (it.isDownloaded == true) {
                    val file =
                        File("${applicationContext.getDirectoryName()}/${it.fileName.getEncryptedName()}")

                    if (file.exists()) {
                        vm.updateTime(fileId)
                        val tempFile =
                            file.decryptFile(applicationContext, it.fileName.getEncryptedName())
                        this.tempFile = tempFile
                        if (tempFile?.exists() == true) {
                            pdfViewPager = PDFViewPager(this, tempFile.absolutePath)
                            parentView.addView(
                                pdfViewPager,
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT
                            )
                        } else {
                            parentView.showSnackbar(
                                "Error reading File. Download again?",
                                length = Snackbar.LENGTH_INDEFINITE,
                                actionText = "Download",
                                action = true
                            ) {
                                file.delete()
                                vm.deleteFile(fileId)
                            }
                        }
                    } else {
                        parentView.showSnackbar(
                            "Error reading File. Download again?",
                            length = Snackbar.LENGTH_INDEFINITE,
                            actionText = "Download",
                            action = true
                        ) {
                            file.delete()
                            vm.deleteFile(fileId)
                        }
                    }
                } else if (it.isDownloaded == false) {
                    if (!applicationContext.isMyServiceRunning(DownloadPdfService::class.java)) {
                        downloadFileAndShow()
                    } else {
                        //Add to download list and queue it
                        //make DownloadPdfService compatible to download multiple files through one service
                    }
                }
            } else {
                if (!applicationContext.isMyServiceRunning(DownloadPdfService::class.java)) {
                    downloadFileAndShow()
                } else {

                    //Add to download list and queue it
                    //make DownloadPdfService compatible to download multiple files through one service
                }
            }
        }
    }

    fun downloadFileAndShow() {
        if (!applicationContext.checkPermission()) {
            //TODO() = Handle Storage Permission properly
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                STORAGE_PERMISSION
            )
            parentView.showSnackbar("Storage Permission not provided", action = false)
        } else {
            DownloadPdfService.startService(
                applicationContext,
                fileUrl,
                fileId,
                fileName,
                subjectName
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == STORAGE_PERMISSION &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            if (!applicationContext.isMyServiceRunning(DownloadPdfService::class.java)) {
                downloadFileAndShow()
            } else {
                //Add to download list and queue it
                //make DownloadPdfService compatible to download multiple files through one service
            }
        }
    }

    override fun onDestroy() {
        if (tempFile != null) {
            tempFile?.delete()
        }
        super.onDestroy()
    }
}