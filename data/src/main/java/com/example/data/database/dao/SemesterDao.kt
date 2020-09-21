package com.example.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.data.database.Semester

@Dao
interface SemesterDao :
    BaseDao<Semester> {

    @Query("SELECT * FROM Semester ORDER BY semester ASC")
    fun getAllSemesters(): LiveData<List<com.example.data.database.Semester>>

    @Query("SELECT * FROM Semester WHERE branch = :branch AND semester = :semester")
    suspend fun getSemesterByBranch(branch: Int, semester: Int): com.example.data.database.Semester?

    @Query("SELECT * FROM Semester WHERE id = :id")
    fun getSemesterById(id: Int): LiveData<com.example.data.database.Semester>
}