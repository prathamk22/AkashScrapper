package com.example.akashscrapper.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.akashscrapper.database.Subject

@Dao
interface SubjectDao : BaseDao<Subject> {

    @Query("SELECT * FROM Subject WHERE semesterId = :id")
    fun getAllSubjectsByClassId(id: Int): LiveData<List<Subject>>

}