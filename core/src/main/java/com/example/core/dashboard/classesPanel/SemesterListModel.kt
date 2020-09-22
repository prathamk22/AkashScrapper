package com.example.core.dashboard.classesPanel

import com.example.data.database.Semester

sealed class SemesterListModel {
    data class Subject(val semester: Int): SemesterListModel()
    data class SubjectItem(val item: Semester): SemesterListModel()
}