package com.example.core.utils

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {
    var errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun setError(error: String) {
        errorLiveData.postValue(error)
    }
}


enum class STATE {
    SUCCESS,
    ERROR,
    LOADING
}
