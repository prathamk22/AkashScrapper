package com.example.akashscrapper.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Semester::class, Subject::class], exportSchema = true, version = 3)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getSemesterDao(): SemesterDao

    abstract fun subjectDao(): SubjectDao

}