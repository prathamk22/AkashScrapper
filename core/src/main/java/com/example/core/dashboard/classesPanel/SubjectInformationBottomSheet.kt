package com.example.core.dashboard.classesPanel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.core.dashboard.DashboardViewModel
import com.example.core.R
import com.example.core.utils.SUBJECT_ID
import com.example.core.utils.observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_subject_information_bottom_sheet.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

@OptIn(ExperimentalCoroutinesApi::class)
class SubjectInformationBottomSheet : BottomSheetDialogFragment() {

    val vm: DashboardViewModel by sharedViewModel()

    val id by lazy{
        arguments?.getInt(SUBJECT_ID)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_subject_information_bottom_sheet,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm.getSubjectById(id?:0).observer(viewLifecycleOwner){
            branchName.text = it.branchName
        }
    }

    companion object {
        fun newInstance(id: Int) =
            SubjectInformationBottomSheet()
                .apply {
                arguments = Bundle().apply {
                    putInt(SUBJECT_ID, id)
                }
            }
    }
}