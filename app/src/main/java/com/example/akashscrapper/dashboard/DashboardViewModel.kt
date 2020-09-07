package com.example.akashscrapper.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.akashscrapper.dashboard.filesPanel.FilesDataSource
import com.example.akashscrapper.database.Subject
import com.example.akashscrapper.network.ResultWrapper
import com.example.akashscrapper.network.models.Data
import com.example.akashscrapper.utils.BaseViewModel
import com.example.akashscrapper.utils.runIO
import kotlinx.coroutines.Dispatchers

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

    //Files Panel

    lateinit var filesLiveData: LiveData<PagedList<Data>>

    fun getFilesBySubject(key: String) {
        val config = PagedList.Config.Builder()
            .setPageSize(10)
            .setEnablePlaceholders(true)
            .build()
        filesLiveData = initializedPagedListBuilder(config, key).build()
    }

    private fun initializedPagedListBuilder(config: PagedList.Config, key: String):
            LivePagedListBuilder<String, Data> {

        val dataSourceFactory = object : DataSource.Factory<String, Data>() {
            override fun create(): DataSource<String, Data> {
                return FilesDataSource(viewModelScope, key)
            }
        }
        return LivePagedListBuilder(dataSourceFactory, config)
    }
}