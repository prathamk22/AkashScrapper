package com.example.core.dashboard.classesPanel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.core.R
import com.example.core.dashboard.DashboardActivity
import com.example.core.dashboard.DashboardViewModel
import com.example.core.utils.getPrefs
import com.example.core.utils.observer
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.fragment_class_panel.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

@ExperimentalPagingApi
@OptIn(ExperimentalCoroutinesApi::class)
class ClassPanel : Fragment() {

    val vm: DashboardViewModel by sharedViewModel()
    val classAdapter = ClassesAdapter()
    val subjectsAdapter =
        SubjectsAdapter()

    val clickListener = object :
        ClassesClickListener {
        override fun onClick(id: Int) {
            getPrefs()?.SP_SELECTED_COURSE = id
            classAdapter.notifyDataSetChanged()
            vm.getSubjectsById(id).observer(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    vm.subjectItem.postValue(it[0])
                    subjectsAdapter.submitList(it)
                }
            }
        }

        override fun onLongPress(id: Int) {
            val addClassBottomSheet =
                SubjectInformationBottomSheet.newInstance(
                    id
                )
            addClassBottomSheet.show(requireFragmentManager(), "")
        }
    }

    val subjectClickListener = object :
        SubjectClickListener {
        override fun onSubjectClicked(subject: com.example.data.database.Subject) {
            vm.subjectItem.postValue(subject)
            (activity as DashboardActivity).overlapping_panels.openStartPanel()
        }
    }

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
        classAdapter.clickListener = clickListener

        subjectsRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = subjectsAdapter
        }
        subjectsAdapter.subjectClickListener = subjectClickListener

        addFab.setOnClickListener {
            val addClassBottomSheet =
                AddClassBottomSheet()
            addClassBottomSheet.show(requireFragmentManager(), "")
        }

        vm.getAllSemester().observer(viewLifecycleOwner) { it ->
            if (it.isNotEmpty()) {
                var semester = -1
                val list = ArrayList<SemesterListModel>()
                it.forEach {
                    if(semester == -1){
                        semester = it.semester
                        list.add(
                            SemesterListModel.Subject(
                                it.semester
                            )
                        )
                    }
                    if (semester != it.semester){
                        list.add(
                            SemesterListModel.Subject(
                                it.semester
                            )
                        )
                        semester = it.semester
                    }
                    list.add(
                        SemesterListModel.SubjectItem(
                            it
                        )
                    )
                }
                clickListener.onClick(getPrefs()?.SP_SELECTED_COURSE ?: 0)
                classAdapter.submitList(list)
            }
        }
    }
}