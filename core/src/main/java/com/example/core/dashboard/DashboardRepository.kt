package com.example.core.dashboard

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.core.dashboard.filesPanel.FilesPagingSource
import com.example.data.networking.AkashOnlineLib
import com.example.data.networking.models.Subjects
import com.example.data.networking.safeApiCall
import kotlinx.coroutines.flow.Flow

class DashboardRepository(
    private val appDatabase: com.example.data.database.AppDatabase
) {

    companion object {
        private const val NETWORK_PAGE_SIZE = 10
    }

    suspend fun getToken() =
        safeApiCall { AkashOnlineLib.api.getToken() }

    suspend fun getSubjects() = safeApiCall {
        AkashOnlineLib.api.getSubjects(1)
    }

    suspend fun insertSubject(subject: Subjects, id: Int) =
        appDatabase.subjectDao()
            .insert(
                com.example.data.database.Subject(
                    subject.id,
                    id,
                    subject.subjectName,
                    subject.subjectAbbrevation
                )
            )

    suspend fun insertSemester(branch: Int, branchName: String, semester: Int): Long {
        val semesterValue = appDatabase.getSemesterDao().getSemesterByBranch(branch, semester)
        return semesterValue?.id?.toLong() ?: appDatabase.getSemesterDao().insert(
            com.example.data.database.Semester(
                0,
                semester,
                branch,
                branchName
            )
        )
    }

    fun getAllSemesters() = appDatabase.getSemesterDao().getAllSemesters()

    fun getSubjectsById(id: Int) = appDatabase.subjectDao().getAllSubjectsByClassId(id)

    suspend fun updateWishlist(id: Int, fileName: String, fileUrl: String): Boolean {
        val isWislisted = appDatabase.filesDao().getWishlist(id)
        return if (isWislisted == null) {
            appDatabase.filesDao().insert(
                com.example.data.database.FileDownloadModel(
                    id,
                    fileUrl,
                    fileName,
                    null,
                    isWishlisted = true
                )
            )
            true
        } else {
            appDatabase.filesDao().setWishlist(!isWislisted.isWishlisted, id)
            !isWislisted.isWishlisted
        }
    }

    //paging 3
    @ExperimentalPagingApi
    fun getFilesByKey(key: String): Flow<PagingData<com.example.data.database.FileData>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                FilesPagingSource(
                    key,
                    appDatabase
                )
            }
        ).flow
    }

    fun getDownloadList() = appDatabase.filesDao().getDownloaded()

    fun getWishlisted() = appDatabase.filesDao().getAllWishlisted()

    fun getSubjectById(id: Int) = appDatabase.getSemesterDao().getSemesterById(id)

}