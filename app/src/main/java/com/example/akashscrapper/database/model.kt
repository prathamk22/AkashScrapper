package com.example.akashscrapper.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Semester(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val semester: Int,
    val branch: Int,
    val branchName: String
)

@Entity
data class Subject(
    @PrimaryKey
    val id: Int,
    val semesterId: Int,
    val subjectName: String,
    val subjectAbbrivation: String
)

@Entity
data class FileDownloadModel(
    @PrimaryKey
    val fileId: Int,
    val fileUrl: String,
    val fileName: String,
    val isDownloaded: Boolean = false,
    val encryptedFileName: String,
    val isWishlisted: Boolean = false
)

@Entity
data class RemoteKeys(
    @PrimaryKey
    val fileId: Int,
    val prevKey: Int?,
    val nextKey: Int?
)

@Entity
data class FileData(
    @PrimaryKey
    val document_id: Int,
    val created_at: String,
    val deleted_at: String?,
    val document_absolute_path: String,
    val document_category_id: Int,
    val document_contributor: String,
    val document_description: String,
    val document_download_count: Int,
    val document_location: String,
    val document_size: Int,
    val document_title: String,
    val document_verification_flag: String,
    val document_view_count: Int,
    val favourites_count: String,
    val ratings_count: String,
    val subject_id: Int,
    val updated_at: String,
    val uploaded_by_user_id: String?
)
