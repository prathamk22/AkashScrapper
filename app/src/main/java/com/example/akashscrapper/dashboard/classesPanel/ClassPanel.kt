package com.example.akashscrapper.dashboard.classesPanel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.akashscrapper.R
import com.example.akashscrapper.dashboard.DashboardViewModel
import com.example.akashscrapper.utils.observer
import kotlinx.android.synthetic.main.fragment_class_panel.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ClassPanel : Fragment() {

    val vm: DashboardViewModel by sharedViewModel()
    val classAdapter = ClassesAdapter()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_class_panel, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        classesRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = classAdapter
        }

        subjectsRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = classAdapter
        }

        addFab.setOnClickListener {
            val addClassBottomSheet = AddClassBottomSheet()
            addClassBottomSheet.showNow(requireFragmentManager(), "")
        }

        vm.getAllSemester().observer(viewLifecycleOwner) {
            classAdapter.submitList(it)
        }
    }
}