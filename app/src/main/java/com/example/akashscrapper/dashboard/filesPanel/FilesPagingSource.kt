package com.example.akashscrapper.dashboard.filesPanel

import androidx.paging.PagingSource
import com.example.akashscrapper.database.AppDatabase
import com.example.akashscrapper.database.FileData
import com.example.akashscrapper.network.AkashOnlineLib
import com.example.akashscrapper.network.ResultWrapper
import com.example.akashscrapper.network.safeApiCall

class FilesPagingSource(
    private val key: String,
    private val appDatabase: AppDatabase
) : PagingSource<Int, FileData>() {

    companion object {
        private const val NOTESHUB_STARTING_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FileData> {
        val position = params.key ?: NOTESHUB_STARTING_PAGE_INDEX
        return when (val response =
            safeApiCall { AkashOnlineLib.api.getFilesBySubject(key, position) }) {
            is ResultWrapper.GenericError -> LoadResult.Error(Throwable(response.error))
            is ResultWrapper.Success -> {
                with(response.value) {
                    if (isSuccessful) {
                        val files = body()
                        val list = files?.documents?.data
                        val fileData = list?.map {
                            FileData(
                                document_id = it.document_id,
                                created_at = it.created_at,
                                deleted_at = it.deleted_at,
                                document_absolute_path = it.document_absolute_path,
                                document_category_id = it.document_category_id,
                                document_contributor = it.document_contributor,
                                document_description = it.document_description,
                                document_download_count = it.document_download_count,
                                document_location = it.document_location,
                                document_size = it.document_size,
                                document_title = it.document_title,
                                document_verification_flag = it.document_verification_flag,
                                document_view_count = it.document_view_count,
                                favourites_count = it.favourites_count,
                                ratings_count = it.ratings_count,
                                subject_id = it.subject_id,
                                updated_at = it.updated_at,
                                uploaded_by_user_id = it.uploaded_by_user_id
                            )
                        }

                        fileData?.let { appDatabase.fileDataDao().insertAll(it) }

                        LoadResult.Page(
                            data = fileData ?: listOf(),
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
}