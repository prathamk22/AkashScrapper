package com.example.akashscrapper.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Login::class], exportSchema = true, version = 1)
abstract class AppDatabase : RoomDatabase() {

}