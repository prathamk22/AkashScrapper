package com.example.akashscrapper.dashboard.filesPanel

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.akashscrapper.R
import com.example.akashscrapper.dashboard.DashboardViewModel
import com.example.akashscrapper.utils.checkPermission
import com.example.akashscrapper.utils.decryptFile
import com.example.akashscrapper.utils.observer
import com.example.akashscrapper.utils.showSnackbar
import kotlinx.android.synthetic.main.fragment_files_panel.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.io.File

class FilesPanel : Fragment() {

    val vm: DashboardViewModel by sharedViewModel()
    val filesAdapter = FilesPagedAdapter()
    lateinit var fileName: String
    lateinit var receiver: BroadcastReceiver
    lateinit var intentFilter: IntentFilter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_files_panel, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        filesRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = filesAdapter
        }
        filesAdapter.onClick = object : FileClickListener {
            override fun onFileClicked(url: String, name: String) {
                fileName = "$name.pdf"
                downloadFileAndShow(url, fileName)
            }
        }

        vm.subjectItem.observer(viewLifecycleOwner) { subject ->
            vm.getFilesBySubject(subject.subjectName)
            vm.filesLiveData.observer(viewLifecycleOwner) {
                filesAdapter.submitList(it)
            }
        }

        intentFilter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        val downloadManager =
            requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val file =
                    File("${requireContext().getExternalFilesDir(Environment.getDataDirectory().absolutePath)}/${Environment.DIRECTORY_DOCUMENTS}/$fileName")

                if (file.exists()) {
//                    file.encryptFile(requireContext())
                    Log.e("TAG", "onReceive: ${context.decryptFile(file.name)}")
                }
                downloadManager.addCompletedDownload(
                    fileName,
                    " ",
                    false,
                    "application/pdf",
                    "${requireContext().getExternalFilesDir(Environment.getDataDirectory().absolutePath)}/${Environment.DIRECTORY_DOCUMENTS}}",
                    file.length(),
                    true
                )
            }
        }

        requireContext().registerReceiver(receiver, intentFilter)
    }

    fun downloadFileAndShow(url: String, fileName: String) {
        if (!requireContext().checkPermission()) {
            filesRv.showSnackbar("Storage Permission not provided")
            return
        }

        try {
            val request = DownloadManager.Request(Uri.parse(url))
            request.setTitle(fileName)
            request.setDestinationInExternalFilesDir(
                requireContext(),
                "data/${Environment.DIRECTORY_DOCUMENTS}",
                "/$fileName"
            )
            // get download service and enqueue file
            val manager =
                requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            manager.enqueue(request)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            filesRv.showSnackbar(
                e.localizedMessage ?: "Error in download this file. Please try again."
            )
        }
    }

    override fun onDestroy() {
        requireContext().unregisterReceiver(receiver)
        super.onDestroy()
    }
}