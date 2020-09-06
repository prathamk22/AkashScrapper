package com.example.akashscrapper.network.models

import com.google.gson.annotations.SerializedName

data class AccessToken(
    @SerializedName("access_token")
    val accessToken: String
)

data class Subjects(
    @SerializedName("subject_id")
    val id: Int,
    @SerializedName("branch_id")
    val branchId: String,
    @SerializedName("semester_id")
    val semesterId: Int,
    @SerializedName("course_id")
    val courseId: Int,
    @SerializedName("subject_name")
    val subjectName: String,
    @SerializedName("subject_abbreviation")
    val subjectAbbrevation: String
)