package com.example.akashscrapper.dashboard

import androidx.lifecycle.liveData
import com.example.akashscrapper.network.ResultWrapper
import com.example.akashscrapper.utils.BaseViewModel
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

}