package com.example.akashscrapper.dashboard.classesPanel

import com.example.akashscrapper.database.Semester

sealed class SemesterListModel {
    data class Subject(val semester: Int): SemesterListModel()
    data class SubjectItem(val item: Semester): SemesterListModel()
}