package com.example.akashscrapper.dashboard.classesPanel

import com.example.data.database.Semester

sealed class SemesterListModel {
    data class Subject(val semester: Int): SemesterListModel()
    data class SubjectItem(val item: com.example.data.database.Semester): SemesterListModel()
}