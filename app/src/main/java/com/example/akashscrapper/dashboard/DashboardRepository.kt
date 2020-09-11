package com.example.akashscrapper.dashboard

import com.example.akashscrapper.database.Semester
import com.example.akashscrapper.database.Subject
import com.example.akashscrapper.database.dao.FileDownloadsDao
import com.example.akashscrapper.database.dao.SemesterDao
import com.example.akashscrapper.database.dao.SubjectDao
import com.example.akashscrapper.network.AkashOnlineLib
import com.example.akashscrapper.network.models.Subjects
import com.example.akashscrapper.network.safeApiCall

class DashboardRepository(
    private val semesterDao: SemesterDao,
    private val subjectDao: SubjectDao,
    private val fileDao: FileDownloadsDao
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

    fun getSubjectsById(id: Int) = subjectDao.getAllSubjectsByClassId(id)

    suspend fun updateWishlist(id: Int): Boolean {
        val isWislisted = fileDao.getWishlist(id).isWishlisted
        fileDao.setWishlist(!isWislisted, id)
        return !isWislisted
    }
}