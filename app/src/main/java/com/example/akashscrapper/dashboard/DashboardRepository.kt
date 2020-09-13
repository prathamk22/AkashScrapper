package com.example.akashscrapper.dashboard

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.akashscrapper.dashboard.filesPanel.FilesPagingSource
import com.example.akashscrapper.database.AppDatabase
import com.example.akashscrapper.database.FileData
import com.example.akashscrapper.database.Semester
import com.example.akashscrapper.database.Subject
import com.example.akashscrapper.database.dao.FileDownloadsDao
import com.example.akashscrapper.database.dao.SemesterDao
import com.example.akashscrapper.database.dao.SubjectDao
import com.example.akashscrapper.network.AkashOnlineLib
import com.example.akashscrapper.network.models.Subjects
import com.example.akashscrapper.network.safeApiCall
import kotlinx.coroutines.flow.Flow

class DashboardRepository(
    private val semesterDao: SemesterDao,
    private val subjectDao: SubjectDao,
    private val fileDao: FileDownloadsDao,
    private val appDatabase: AppDatabase
) {

    companion object {
        private const val NETWORK_PAGE_SIZE = 10
    }

    suspend fun getToken() = safeApiCall { AkashOnlineLib.api.getToken() }

    suspend fun getSubjects() = safeApiCall { AkashOnlineLib.api.getSubjects(1) }

    suspend fun insertSubject(subject: Subjects, id: Int) =
        subjectDao.insert(Subject(subject.id, id, subject.subjectName, subject.subjectAbbrevation))

    suspend fun insertSemester(branch: Int, branchName: String, semester: Int): Long {
        val semesterValue = semesterDao.getSemesterByBranch(branch, semester)
        return semesterValue?.id?.toLong() ?: semesterDao.insert(
            Semester(
                0,
                semester,
                branch,
                branchName
            )
        )
    }

    fun getAllSemesters() = semesterDao.getAllSemesters()

    fun getSubjectsById(id: Int) = subjectDao.getAllSubjectsByClassId(id)

    suspend fun updateWishlist(id: Int): Boolean {
        val isWislisted = fileDao.getWishlist(id).isWishlisted
        fileDao.setWishlist(!isWislisted, id)
        return !isWislisted
    }

    //paging 3
    @ExperimentalPagingApi
    fun getFilesByKey(key: String): Flow<PagingData<FileData>> {

        val pagingSource = { appDatabase.fileDataDao().getFiles(key) }

        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE),
            remoteMediator = FilesPagingSource(
                key,
                appDatabase
            ),
            pagingSourceFactory = pagingSource
        ).flow
    }

}