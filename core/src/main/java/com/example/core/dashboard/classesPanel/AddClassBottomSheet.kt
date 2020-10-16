package com.example.core.dashboard.classesPanel

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import com.example.core.GenericBottomSheet
import com.example.core.GenericOnClickListener
import com.example.core.R
import com.example.core.dashboard.DashboardViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
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

    override fun getTheme(): Int = R.style.RoundedBottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener { dialogView ->
            val d = dialogView as BottomSheetDialog
            val bottomSheet =
                d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
            BottomSheetBehavior.from<FrameLayout?>(bottomSheet!!).state =
                BottomSheetBehavior.STATE_EXPANDED
        }
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        semesterTv.setOnClickListener {
            genericBottomSheet = GenericBottomSheet(object : GenericOnClickListener {
                override fun onClick(position: Int, value: String) {
                    semesterTv.text = value
                    semesterTv.setTextColor(Color.BLACK)
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
                    branchTv.setTextColor(Color.BLACK)
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
            dismiss()
        }

        close.setOnClickListener {
            dismiss()
        }

        cancel.setOnClickListener {
            dismiss()
        }
    }

}