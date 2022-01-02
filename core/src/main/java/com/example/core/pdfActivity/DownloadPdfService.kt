package com.example.core.pdfActivity

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.IBinder
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.core.R
import com.example.core.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.io.File

//TODO() Make it accessible for mutlifile downloads
class DownloadPdfService : Service() {

    companion object {
        fun startService(
            context: Context,
            fileUrl: String,
            fileId: Int,
            fileName: String,
            subjectName: String
        ) {
            val intent = Intent(context, DownloadPdfService::class.java).apply {
                putExtra(FILE_URL, fileUrl)
                putExtra(FILE_NAME, fileName)
                putExtra(FILE_ID, fileId)
                putExtra(SUBJECT_NAME, subjectName)
            }
            ContextCompat.startForegroundService(context, intent)
        }

        const val NOTIFICATION_ID = 101
        const val ACTION_STOP = "ACTION_STOP_FOREGROUND_SERVICE"
        var notificationId = 1
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    private val fileData: com.example.data.database.dao.FileDownloadsDao by inject()
    lateinit var receiver: BroadcastReceiver
    lateinit var intentFilter: IntentFilter

    private val notificationManager by lazy {
        applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (intent.action == ACTION_STOP) {
            stopServiceManually()
            notificationManager.cancel(NOTIFICATION_ID)
        } else {
            val fileName = intent.getStringExtra(FILE_NAME)
            val subjectName = intent.getStringExtra(SUBJECT_NAME)
            val fileNameWithExt = "$fileName.pdf"
            val url = intent.getStringExtra(FILE_URL)
            val id = intent.getIntExtra(FILE_ID, 0)

            GlobalScope.launch {
                if (fileData.getWishlist(id) == null) {
                    fileData.insert(
                        com.example.data.database.FileDownloadModel(
                            id,
                            url ?: "",
                            fileNameWithExt,
                            subjectName,
                            lastVisited = System.currentTimeMillis(),
                            isDownloaded = false,
                            isWishlisted = false
                        )
                    )
                }
            }

            if (fileName?.isNotEmpty() == true && url?.isNotEmpty() == true) {
                startForeground()

                intentFilter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
                val downloadManager =
                    applicationContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

                receiver = object : BroadcastReceiver() {
                    override fun onReceive(context: Context, intent: Intent) {
                        val file =
                            File("${applicationContext.getDirectoryName()}/$fileNameWithExt")

                        downloadManager.addCompletedDownload(
                            fileName,
                            " ",
                            false,
                            "application/pdf",
                            applicationContext.getDirectoryName(),
                            file.length(),
                            true
                        )

                        if (file.exists()) {
                            if (file.encryptFile(
                                    applicationContext,
                                    fileNameWithExt.getEncryptedName()
                                )
                            ) {
                                file.delete()
                            }
                            GlobalScope.launch(Dispatchers.IO) {
                                fileData.fileDownloaded(true, id)
                            }
                            Toast.makeText(
                                applicationContext,
                                "$fileName download Completed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        stopServiceManually()
                    }
                }

                startDownload(url, fileNameWithExt)
                applicationContext.registerReceiver(receiver, intentFilter)
            } else {
                Toast.makeText(
                    applicationContext,
                    "File is not appropriate to download. ",
                    Toast.LENGTH_SHORT
                ).show()
                stopSelf()
            }
        }
        return START_NOT_STICKY
    }

    fun startDownload(url: String?, fileName: String?) {
        try {
            val request = DownloadManager.Request(Uri.parse(url))
            request.setTitle(fileName)
            request.setDestinationInExternalFilesDir(
                applicationContext,
                "data/${Environment.DIRECTORY_DOCUMENTS}",
                "/$fileName"
            )
            // get download service and enqueue file
            val manager =
                applicationContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            manager.enqueue(request)

            Toast.makeText(applicationContext, "Downloading $fileName", Toast.LENGTH_SHORT).show()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            Toast.makeText(applicationContext, e.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun startForeground() {
        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel("my_service", "My Background Service")
            } else {
                ""
            }

        val notificationBuilder = NotificationCompat.Builder(this, channelId)

        val stopSelf = Intent(this, DownloadPdfService::class.java)
        stopSelf.action = ACTION_STOP
        val pStopSelf = PendingIntent.getService(
            this,
            0,
            stopSelf, /*Stop Service*/
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        val notification = notificationBuilder.setOngoing(true)
            .setSmallIcon(R.drawable.flaticon_pdf_dummy)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .addAction(R.drawable.ic_outline_close_24, "Cancel", pStopSelf)
            .build()

        startForeground(101, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String {
        val chan = NotificationChannel(
            channelId,
            channelName, NotificationManager.IMPORTANCE_NONE
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

    private fun stopServiceManually() {
        stopForeground(true)
        stopSelf()
    }

    override fun onDestroy() {
        applicationContext.unregisterReceiver(receiver)
        super.onDestroy()
    }

}