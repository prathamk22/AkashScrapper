package com.example.akashscrapper.dashboard.filesPanel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.akashscrapper.R
import com.example.akashscrapper.dashboard.DashboardViewModel
import com.example.akashscrapper.pdfActivity.PdfActivity
import com.example.akashscrapper.utils.observer
import kotlinx.android.synthetic.main.fragment_files_panel.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FilesPanel : Fragment() {

    val vm: DashboardViewModel by sharedViewModel()
    val filesAdapter = FilesPagedAdapter()
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
        }

        vm.subjectItem.observer(viewLifecycleOwner) { subject ->
            vm.getFilesBySubject(subject.subjectName)
            vm.filesLiveData.observer(viewLifecycleOwner) {
                filesAdapter.submitList(it)
            }
        }

    }

}