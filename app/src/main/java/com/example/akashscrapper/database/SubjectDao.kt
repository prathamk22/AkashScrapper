package com.example.akashscrapper.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

@Dao
interface SubjectDao : BaseDao<Subject> {

    @Query("SELECT * FROM Subject WHERE semesterId = :id")
    fun getAllSubjectsByClassId(id: Int): LiveData<List<Subject>>

}