package com.example.akashscrapper.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.akashscrapper.network.ResultWrapper
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

    fun getAllSemester() = repo.getAllSemesters()
}