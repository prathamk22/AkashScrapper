package com.example.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.database.dao.*

@Database(
    entities = [Semester::class, Subject::class, FileDownloadModel::class, FileData::class, RemoteKeys::class],
    exportSchema = true,
    version = 3
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getSemesterDao(): SemesterDao

    abstract fun subjectDao(): SubjectDao

    abstract fun filesDao(): FileDownloadsDao

    abstract fun fileDataDao(): FileDataDao

    abstract fun remoteKeys(): RemoteKeysDao
}