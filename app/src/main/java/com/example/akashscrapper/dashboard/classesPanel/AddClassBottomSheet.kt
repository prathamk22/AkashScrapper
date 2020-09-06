package com.example.akashscrapper.dashboard.classesPanel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.akashscrapper.GenericBottomSheet
import com.example.akashscrapper.GenericOnClickListener
import com.example.akashscrapper.R
import com.example.akashscrapper.dashboard.DashboardViewModel
import com.example.akashscrapper.utils.observer
import com.example.akashscrapper.utils.setToolbar
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_add_class_bottom_sheet.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class AddClassBottomSheet : BottomSheetDialogFragment() {

    lateinit var genericBottomSheet: GenericBottomSheet
    val vm: DashboardViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_class_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setToolbar(
            toolbar,
            indicator = R.drawable.ic_outline_close_24,
            title = "Add Classroom"
        )
        semesterTv.setOnClickListener {
            genericBottomSheet = GenericBottomSheet(object : GenericOnClickListener {
                override fun onClick(position: Int, value: String) {
                    semesterTv.text = value
                    vm.semester = value.toInt()
                }
            })
            genericBottomSheet.showNow(childFragmentManager, "")
            val list = requireContext().resources.getStringArray(R.array.semester)
            genericBottomSheet.setList(list = list)
        }

        branchTv.setOnClickListener {
            genericBottomSheet = GenericBottomSheet(object : GenericOnClickListener {
                override fun onClick(position: Int, value: String) {
                    branchTv.text = value
                    vm.branch = position
                    vm.branchName = value
                }
            })
            genericBottomSheet.showNow(childFragmentManager, "")
            val list = requireContext().resources.getStringArray(R.array.branch)
            genericBottomSheet.setList(list = list)
        }

        insertCourse.setOnClickListener {
            if (vm.branch == -1) {
                Toast.makeText(
                    requireContext(),
                    "Select a branch to add classroom",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            if (vm.semester == -1) {
                Toast.makeText(
                    requireContext(),
                    "Select a semester to add classroom",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            vm.insertCourse()
        }

        vm.courseInserted.observer(viewLifecycleOwner) {
            dismiss()
        }

    }


}