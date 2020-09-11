package com.example.akashscrapper.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.akashscrapper.database.Semester

@Dao
interface SemesterDao : BaseDao<Semester> {

    @Query("SELECT * FROM Semester")
    fun getAllSemesters(): LiveData<List<Semester>>

    @Query("SELECT * FROM Semester WHERE branch = :branch AND semester = :semester")
    suspend fun getSemesterByBranch(branch: Int, semester: Int): Semester
}