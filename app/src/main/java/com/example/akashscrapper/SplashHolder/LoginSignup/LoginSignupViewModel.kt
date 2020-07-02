package com.example.akashscrapper.SplashHolder.LoginSignup

import androidx.lifecycle.MutableLiveData
import com.example.akashscrapper.network.ResultWrapper
import com.example.akashscrapper.network.fetchError
import com.example.akashscrapper.utils.BaseViewModel
import com.example.akashscrapper.utils.runIO

class LoginSignupViewModel(
    private val repo: LoginSignupRepository
) : BaseViewModel() {

    val token: MutableLiveData<String> = MutableLiveData()

    fun login(email: String, password: String) {
        runIO {
            when (val response = repo.login(email, password)) {
                is ResultWrapper.GenericError -> {
                    setError(response.error)
                }
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful) {
                        token.postValue(response.value.message())
                    } else {
                        setError(fetchError(response.value.code()))
                    }
                }
            }
        }
    }

    fun signUp(email: String, password: String, fullName: String) {
        runIO {
            when (val response = repo.signUp(email, password)) {
                is ResultWrapper.GenericError -> {
                    setError(response.error)
                }
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful) {
                        token.postValue(response.value.message())
                    } else {
                        setError(fetchError(response.value.code()))
                    }
                }
            }
        }
    }

}