package com.example.akashscrapper.dashboard.filesPanel

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.example.akashscrapper.network.AkashOnlineLib
import com.example.akashscrapper.network.ResultWrapper
import com.example.akashscrapper.network.models.Data
import com.example.akashscrapper.network.safeApiCall
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class FilesDataSource(
    private val courountineScope: CoroutineScope,
    private val key: String
) : PageKeyedDataSource<String, Data>() {

    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, Data>
    ) {
        courountineScope.launch {
            when (val response = safeApiCall { AkashOnlineLib.api.getFilesBySubject(key, 1) }) {
                is ResultWrapper.GenericError -> Log.e("TAG", "loadInitial: ${response.error}")
                is ResultWrapper.Success -> {
                    with(response.value) {
                        if (isSuccessful) {
                            val files = body()
                            val currentOffSet = files?.documents?.current_page.toString()
                            if (files?.documents?.current_page != files?.documents?.last_page) {
                                callback.onResult(
                                    files?.documents?.data ?: listOf(),
                                    currentOffSet,
                                    files?.documents?.current_page?.plus(1).toString()
                                )
                            }
                        } else {

                        }
                    }
                }
            }
        }
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, Data>) {

    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, Data>) {
        courountineScope.launch {
            when (val response =
                safeApiCall { AkashOnlineLib.api.getFilesBySubject(key, params.key.toInt()) }) {
                is ResultWrapper.GenericError -> Log.e("TAG", "loadInitial: ${response.error}")
                is ResultWrapper.Success -> {
                    with(response.value) {
                        if (isSuccessful) {
                            val files = body()
                            if (files?.documents?.current_page != files?.documents?.last_page) {
                                callback.onResult(
                                    files?.documents?.data ?: listOf(),
                                    files?.documents?.current_page?.plus(1).toString()
                                )
                            }
                        } else {

                        }
                    }
                }
            }
        }
    }

    override fun invalidate() {
        super.invalidate()
        courountineScope.cancel()
    }
}