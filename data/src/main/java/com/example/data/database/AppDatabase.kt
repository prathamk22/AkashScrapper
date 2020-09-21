package com.example.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.database.dao.FileDataDao
import com.example.data.database.dao.FileDownloadsDao
import com.example.data.database.dao.SemesterDao
import com.example.data.database.dao.SubjectDao

@Database(
    entities = [Semester::class, Subject::class, FileDownloadModel::class, FileData::class],
    exportSchema = true,
    version = 2
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getSemesterDao(): SemesterDao

    abstract fun subjectDao(): SubjectDao

    abstract fun filesDao(): FileDownloadsDao

    abstract fun fileDataDao(): FileDataDao
}