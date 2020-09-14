package com.example.akashscrapper.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.akashscrapper.database.dao.FileDataDao
import com.example.akashscrapper.database.dao.FileDownloadsDao
import com.example.akashscrapper.database.dao.SemesterDao
import com.example.akashscrapper.database.dao.SubjectDao

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