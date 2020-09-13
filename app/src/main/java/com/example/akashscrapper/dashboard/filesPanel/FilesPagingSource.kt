package com.example.akashscrapper.dashboard.filesPanel

import androidx.paging.PagingSource
import com.example.akashscrapper.network.AkashOnlineLib
import com.example.akashscrapper.network.ResultWrapper
import com.example.akashscrapper.network.models.Data
import com.example.akashscrapper.network.safeApiCall

class FilesPagingSource(
    private val key: String
) : PagingSource<Int, Data>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Data> {
        val position = params.key ?: NOTESHUB_STARTING_PAGE_INDEX
        return when (val response =
            safeApiCall { AkashOnlineLib.api.getFilesBySubject(key, position) }) {
            is ResultWrapper.GenericError -> LoadResult.Error(Throwable(response.error))
            is ResultWrapper.Success -> {
                with(response.value) {
                    if (isSuccessful) {
                        val files = body()
                        val currentOffSet = files?.documents?.current_page.toString()
                        LoadResult.Page(
                            data = files?.documents?.data ?: listOf(),
                            prevKey = if (position == NOTESHUB_STARTING_PAGE_INDEX) null else position - 1,
                            nextKey = if (files?.documents?.data?.isEmpty() == true) null else position + 1
                        )
                    } else {
                        LoadResult.Error(Throwable(response.value.message()))
                    }
                }
            }
        }
    }

    companion object {
        private const val NOTESHUB_STARTING_PAGE_INDEX = 1
    }
}