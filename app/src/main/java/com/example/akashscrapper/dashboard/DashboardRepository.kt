package com.example.akashscrapper.dashboard

import com.example.akashscrapper.database.Semester
import com.example.akashscrapper.database.SemesterDao
import com.example.akashscrapper.database.Subject
import com.example.akashscrapper.database.SubjectDao
import com.example.akashscrapper.network.AkashOnlineLib
import com.example.akashscrapper.network.models.Subjects
import com.example.akashscrapper.network.safeApiCall

class DashboardRepository(
    private val semesterDao: SemesterDao,
    private val subjectDao: SubjectDao,
) {

    suspend fun getToken() = safeApiCall { AkashOnlineLib.api.getToken() }

    suspend fun getSubjects() = safeApiCall { AkashOnlineLib.api.getSubjects(1) }

    suspend fun insertSubject(subject: Subjects, id: Int) =
        subjectDao.insert(Subject(subject.id, id, subject.subjectName, subject.subjectAbbrevation))

    suspend fun insertSemester(branch: Int, branchName: String, semester: Int): Long {
        val semesterValue = semesterDao.getSemesterByBranch(branch, semester)
        return if (semesterValue == null) {
            semesterDao.insert(Semester(0, semester, branch, branchName))
        } else {
            semesterValue.id.toLong()
        }
    }

    fun getAllSemesters() = semesterDao.getAllSemesters()
}