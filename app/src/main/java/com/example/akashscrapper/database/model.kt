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