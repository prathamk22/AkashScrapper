package com.example.core.dashboard.filesPanel

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.data.database.AppDatabase
import com.example.data.database.FileData
import com.example.data.database.RemoteKeys
import com.example.data.networking.AkashOnlineLib
import com.example.data.networking.ResultWrapper
import com.example.data.networking.safeApiCall

@OptIn(ExperimentalPagingApi::class)
class FilesRemoteMediator(
    private val key: String,
    private val appDatabase: AppDatabase
) : RemoteMediator<Int, FileData>() {

    companion object {
        private const val NOTESHUB_STARTING_PAGE_INDEX = 1
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, FileData>
    ): MediatorResult {
        val loadKey = when (loadType) {
            LoadType.REFRESH -> null
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                state.lastItemOrNull()
                    ?: return MediatorResult.Success(endOfPaginationReached = true)
                getRemoteKeys()
            }
        }

        val page = loadKey?.nextKey ?: NOTESHUB_STARTING_PAGE_INDEX
        when (val response = safeApiCall {
            AkashOnlineLib.api.getFilesBySubject(
                key,
                page
            )
        }) {
            is ResultWrapper.GenericError -> return MediatorResult.Error(Throwable(response.error))
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
                                uploaded_by_user_id = it.uploaded_by_user_id,
                                subjectKey = key
                            )
                        }

                        val endOfPaginationReached = list?.isEmpty() ?: true

                        val prevKey =
                            if (page == NOTESHUB_STARTING_PAGE_INDEX) NOTESHUB_STARTING_PAGE_INDEX else page.minus(
                                1
                            )
                        val nextKey = if (endOfPaginationReached) null else page.plus(1)
                        val keys = list?.map {
                            RemoteKeys(
                                fileId = it.document_id,
                                prevKey = prevKey,
                                nextKey = nextKey,
                                subjectKey = key
                            )
                        }

                        appDatabase.withTransaction {
                            if (loadType == LoadType.REFRESH) {
                                appDatabase.remoteKeys().nukeTable(key)
                                appDatabase.fileDataDao().clearTable(key)
                            }

                            keys?.let { appDatabase.remoteKeys().insertAll(it) }
                            fileData?.let {
                                appDatabase.fileDataDao().insertAll(it)
                            }
                        }

                        return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
                    } else {
                        return MediatorResult.Error(Throwable(message()))
                    }
                }
            }
        }

    }

    private suspend fun getRemoteKeys(): RemoteKeys? {
        return appDatabase.remoteKeys().getRedditKeys(key).firstOrNull()
    }

}