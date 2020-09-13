package com.example.akashscrapper.dashboard.filesPanel

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.akashscrapper.database.AppDatabase
import com.example.akashscrapper.database.FileData
import com.example.akashscrapper.database.RemoteKeys
import com.example.akashscrapper.network.AkashOnlineLib
import com.example.akashscrapper.network.ResultWrapper
import com.example.akashscrapper.network.safeApiCall
import java.io.InvalidObjectException

@ExperimentalPagingApi
class FilesPagingSource(
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

        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: NOTESHUB_STARTING_PAGE_INDEX
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                if (remoteKeys == null || remoteKeys.nextKey == null) {
                    throw InvalidObjectException("Remote key should not be null for $loadType")
                }
                remoteKeys.nextKey
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                if (remoteKeys == null) {
                    // The LoadType is PREPEND so some data was loaded before,
                    // so we should have been able to get remote keys
                    // If the remoteKeys are null, then we're an invalid state and we have a bug
                    throw InvalidObjectException("Remote key and the prevKey should not be null")
                }
                // If the previous key is null, then we can't request more data
                val prevKey = remoteKeys.prevKey
                if (prevKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                remoteKeys.prevKey
            }
        }

        return when (val response =
            safeApiCall { AkashOnlineLib.api.getFilesBySubject(key, page) }) {
            is ResultWrapper.GenericError -> MediatorResult.Error(Throwable(response.error))
            is ResultWrapper.Success -> {
                with(response.value) {
                    if (isSuccessful) {
                        val files = body()
                        val list = files?.documents?.data ?: listOf()
                        val endOfPaginationReached = list.isEmpty()

                        appDatabase.withTransaction {
                            if (loadType == LoadType.REFRESH) {
                                appDatabase.remoteKeysDao().clearRemoteKeys()
                                appDatabase.fileDataDao().clearTable()
                            }

                            val prevKey =
                                if (page == NOTESHUB_STARTING_PAGE_INDEX) null else page - 1
                            val nextKey = if (endOfPaginationReached) null else page + 1

                            val keys = list.map {
                                RemoteKeys(
                                    fileId = it.document_id,
                                    prevKey = prevKey,
                                    nextKey = nextKey
                                )
                            }

                            val fileData = list.map {
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

                            appDatabase.remoteKeysDao().insertAll(keys)
                            appDatabase.fileDataDao().insertAll(fileData)
                        }

                        MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
                    } else {
                        MediatorResult.Error(Throwable(response.value.message()))
                    }
                }
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, FileData>): RemoteKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        Log.e("TAG", "getRemoteKeyClosestToCurrentPosition: ${state.pages}")
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { repo ->
            // Get the remote keys of the first items retrieved
            appDatabase.remoteKeysDao().remoteKeysRepoId(repo.document_id)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, FileData>): RemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        Log.e("TAG", "getRemoteKeyForLastItem: pages ${state.pages}")
        return state.pages.lastOrNull {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { repo ->
            // Get the remote keys of the last item retrieved
            appDatabase.remoteKeysDao().remoteKeysRepoId(repo.document_id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, FileData>
    ): RemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        Log.e("TAG", "getRemoteKeyClosestToCurrentPosition: ${state.pages}")
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.document_id?.let { repoId ->
                appDatabase.remoteKeysDao().remoteKeysRepoId(repoId)
            }
        }
    }
}