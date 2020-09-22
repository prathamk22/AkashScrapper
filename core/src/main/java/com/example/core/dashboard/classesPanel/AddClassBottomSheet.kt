package com.example.core.dashboard.classesPanel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.core.GenericBottomSheet
import com.example.core.GenericOnClickListener
import com.example.core.R
import com.example.core.dashboard.DashboardViewModel
import com.example.core.utils.observer
import com.example.core.utils.setToolbar
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_add_class_bottom_sheet.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

@OptIn(ExperimentalCoroutinesApi::class)
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
                    vm.branch = position + 1
                    vm.branchName = value
                }
            })
            genericBottomSheet.showNow(childFragmentManager, "")
            val list = requireContext().resources.getStringArray(R.array.branch)
            genericBottomSheet.setList(list = list)
        }

        insertCourse.setOnClickListener {
            if (vm.semester == -1) {
                Toast.makeText(
                    requireContext(),
                    "Select a semester to add classroom",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            if (vm.branch == -1) {
                Toast.makeText(
                    requireContext(),
                    "Select a branch to add classroom",
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            dismiss()
        }
        return super.onOptionsItemSelected(item)
    }
}