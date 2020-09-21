package com.example.akashscrapper.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.akashscrapper.database.FileData
import com.example.akashscrapper.database.Subject
import com.example.akashscrapper.network.ResultWrapper
import com.example.akashscrapper.utils.BaseViewModel
import com.example.akashscrapper.utils.runIO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
class DashboardViewModel(
    private val repo: DashboardRepository
) : BaseViewModel() {

    fun getToken() = liveData(Dispatchers.IO) {
        when (val response = repo.getToken()) {
            is ResultWrapper.GenericError -> setError(response.error)
            is ResultWrapper.Success -> {
                if (response.value.isSuccessful) {
                    emit(response.value.body()?.accessToken)
                } else {
                    setError(response.value.message())
                }
            }
        }
    }

    //Class Panel
    val subjectItem = MutableLiveData<Subject>()

    fun getAllSemester() = repo.getAllSemesters()

    fun getSubjectsById(id: Int) = repo.getSubjectsById(id)

    //Add Bottom Sheet ViewModel
    var branch: Int = -1
    var semester: Int = -1
    var branchName: String = ""
    val courseInserted = MutableLiveData<Boolean>()

    fun insertCourse() {
        runIO {
            when (val response = repo.getSubjects()) {
                is ResultWrapper.GenericError -> setError(response.error)
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful) {
                        val list = response.value.body()
                        val id = repo.insertSemester(branch, branchName, semester)
                        for (subject in list ?: emptyList()) {
                            val branches = subject.branchId.split(",")
                            if (subject.semesterId == semester && branches.contains(branch.toString())) {
                                repo.insertSubject(subject, id.toInt())
                            }
                        }
                        courseInserted.postValue(true)
                    } else {
                        setError(response.value.message())
                    }
                }
            }
        }
    }

    //subject detail Model
    fun getSubjectById(id: Int) = repo.getSubjectById(id)

    //Files Panel
    private var currentQueryValue: String? = null

    private var currentSearchResult: Flow<PagingData<FileData>>? = null

    @ExperimentalPagingApi
    fun getFilesByKey(key: String): Flow<PagingData<FileData>> {
        val lastResult = currentSearchResult
        if (key == currentQueryValue && lastResult != null) {
            return lastResult
        }
        currentQueryValue = key
        val newResult = repo.getFilesByKey(key)
            .cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }

    fun updateWishlist(id: Int, fileName: String, fileUrl: String) = liveData {
        emit(repo.updateWishlist(id, fileName, fileUrl))
    }

    //Users Panel
    fun getDownloadList() = repo.getDownloadList()

    fun getWishlisted() = repo.getWishlisted()
}