package com.example.akashscrapper.dashboard.filesPanel

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.akashscrapper.R
import com.example.akashscrapper.dashboard.DashboardViewModel
import com.example.akashscrapper.pdfActivity.DownloadPdfService
import com.example.akashscrapper.pdfActivity.PdfActivity
import com.example.akashscrapper.utils.*
import kotlinx.android.synthetic.main.fragment_files_panel.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
class FilesPanel : Fragment() {

    val vm: DashboardViewModel by sharedViewModel()
    val filesAdapter = FilesPagedAdapter()
    private var searchJob: Job? = null

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
            override fun onFileClicked(url: String, name: String, fileId: Int) {
                startActivity(
                    PdfActivity.getInstance(
                        requireContext(),
                        fileUrl = url,
                        fileName = name,
                        fileId = fileId
                    )
                )
            }

            override fun onDrawableClick(
                type: Int,
                fileName: String,
                fileId: Int,
                fileUrl: String
            ) {
                when (type) {
                    1 -> {
                        //Add to wishlist
                        vm.updateWishlist(fileId).observer(this@FilesPanel) {
                            filesRv.showSnackbar(
                                if (it)
                                    "Added to Wishlist"
                                else
                                    "Removed from Wishlist"
                            )
                        }
                    }
                    2 -> {
                        //Download File
                        if (!requireContext().checkPermission()) {
                            filesRv.showSnackbar("Storage Permission not provided")
                            ActivityCompat.requestPermissions(
                                requireActivity(),
                                arrayOf(
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                                ),
                                STORAGE_PERMISSION
                            )
                        } else {
                            if (!requireContext().isMyServiceRunning(DownloadPdfService::class.java)) {
                                DownloadPdfService.startService(
                                    requireContext(),
                                    fileUrl,
                                    fileId,
                                    fileName
                                )
                            } else {
                                //Add to download list
                            }
                        }
                    }
                }
            }
        }

        vm.subjectItem.observer(viewLifecycleOwner) { subject ->
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                vm.getFilesByKey(subject.subjectName).collect {
                    filesAdapter.submitData(it)
                }
            }
        }

    }

}