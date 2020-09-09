package com.example.akashscrapper.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Semester::class, Subject::class, FileDownloadModel::class],
    exportSchema = true,
    version = 4
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getSemesterDao(): SemesterDao

    abstract fun subjectDao(): SubjectDao

    abstract fun filesDao(): FileDownloadsDao
}