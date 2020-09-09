package com.example.akashscrapper.pdfActivity

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
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.akashscrapper.R
import com.example.akashscrapper.database.FileDownloadModel
import com.example.akashscrapper.database.FileDownloadsDao
import com.example.akashscrapper.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.io.File

class DownloadPdfService : Service() {

    companion object {
        fun startService(context: Context, fileUrl: String, fileId: Int, fileName: String) {
            val intent = Intent(context, DownloadPdfService::class.java).apply {
                putExtra(FILE_URL, fileUrl)
                putExtra(FILE_NAME, fileName)
                putExtra(FILE_ID, fileId)
            }
            ContextCompat.startForegroundService(context, intent)
        }


        const val ACTION_STOP = "ACTION_STOP_FOREGROUND_SERVICE"
        const val NOTIFICATION_ID = 10
        var notificationId = 1
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    private val fileDownloadDao: FileDownloadsDao by inject()
    lateinit var receiver: BroadcastReceiver
    lateinit var intentFilter: IntentFilter

    private val notificationManager by lazy {
        applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val fileName = intent?.getStringExtra(FILE_NAME)
        val fileNameWithExt = "$fileName.pdf"
        val url = intent?.getStringExtra(FILE_URL)
        val id = intent?.getIntExtra(FILE_ID, 0) ?: 0
        val encryptedName = "${fileName}_encrypted.dat"

        if (fileName?.isNotEmpty() == true && url?.isNotEmpty() == true) {
            val notification =
                NotificationCompat.Builder(applicationContext, DOWNLOAD_CHANNEL_ID).apply {
                    setSmallIcon(R.drawable.ic_baseline_arrow_downward_24)
                    setContentTitle(fileName)
                    setOnlyAlertOnce(true)
                    setContentText("Downloading file")
                    setOngoing(true)
                }
            startForeground()
            GlobalScope.launch(Dispatchers.IO) {
                fileDownloadDao.insert(
                    FileDownloadModel(
                        id,
                        url,
                        fileName,
                        false,
                        encryptedName
                    )
                )
            }

            intentFilter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
            val downloadManager =
                applicationContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

            receiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    val file =
                        File("${applicationContext.getDirectoryName()}/$fileNameWithExt")

                    Log.e("TAG", "onReceive: ${intent.action}")
                    downloadManager.addCompletedDownload(
                        fileName,
                        " ",
                        false,
                        "application/pdf",
                        applicationContext.getDirectoryName(),
                        file.length(),
                        true
                    )
                    if (intent.action == DOWNLOAD_COMPLETE) {

                    } else {

                    }
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
                // If earlier version channel ID is not used
                // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)
                ""
            }

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
        val notification = notificationBuilder.setOngoing(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
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