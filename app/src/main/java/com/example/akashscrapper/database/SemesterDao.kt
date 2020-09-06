package com.example.akashscrapper.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

@Dao
interface SemesterDao : BaseDao<Semester> {

    @Query("SELECT * FROM Semester")
    fun getAllSemesters(): LiveData<List<Semester>>

    @Query("SELECT * FROM Semester WHERE branch = :branch AND semester = :semester")
    suspend fun getSemesterByBranch(branch: Int, semester: Int): Semester
}