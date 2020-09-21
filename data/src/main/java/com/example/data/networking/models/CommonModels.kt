package com.example.data.networking.models

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

data class CourseXX(
    val course_abbreviation: String,
    val course_active_status: Boolean,
    val course_admin_user_id: Int,
    val course_id: Int,
    val course_name: String,
    val created_at: String,
    val deleted_at: Any,
    val last_semester: Int,
    val university: UniversityXX,
    val university_id: Int,
    val updated_at: String
)

data class Course(
    val course_abbreviation: String,
    val course_active_status: Boolean,
    val course_admin_user_id: Int,
    val course_id: Int,
    val course_name: String,
    val created_at: String,
    val deleted_at: Any,
    val last_semester: Int,
    val university: University,
    val university_id: Int,
    val updated_at: String
)

data class Category(
    val created_at: String,
    val deleted_at: Any,
    val document_category_id: Int,
    val document_category_name: String,
    val updated_at: String
)

data class CategoryX(
    val created_at: String,
    val deleted_at: Any,
    val document_category_id: Int,
    val document_category_name: String,
    val updated_at: String
)

data class CourseX(
    val course_abbreviation: String,
    val course_active_status: Boolean,
    val course_admin_user_id: Int,
    val course_id: Int,
    val course_name: String,
    val created_at: String,
    val deleted_at: Any,
    val last_semester: Int,
    val university_id: Int,
    val updated_at: String
)

data class Data(
    val category: Category,
    val created_at: String,
    val deleted_at: String,
    val document_absolute_path: String,
    val document_category_id: Int,
    val document_contributor: String,
    val document_description: String,
    val document_download_count: Int,
    val document_id: Int,
    val document_location: String,
    val document_size: Int,
    val document_title: String,
    val document_verification_flag: String,
    val document_view_count: Int,
    val favourites_count: String,
    val ratings_count: String,
    val subject: Subject,
    val subject_id: Int,
    val updated_at: String,
    val uploaded_by_user_id: String
)

data class Documents(
    val current_page: Int,
    val `data`: List<Data>,
    val first_page_url: String,
    val from: Int,
    val last_page: Int,
    val last_page_url: String,
    val next_page_url: String,
    val path: String,
    val per_page: Int,
    val prev_page_url: Any,
    val to: Int,
    val total: Int
)

data class FilesModel(
    val documents: Documents,
    val filters: Filters,
    val subject_availability_mappings: List<SubjectAvailabilityMapping>
)

data class Filters(
    val categories: List<CategoryX>,
    val courses: List<CourseX>,
    val universities: List<UniversityX>
)

data class Subject(
    val branch_id: String,
    val course: Course,
    val course_id: Int,
    val created_at: String,
    val deleted_at: Any,
    val semester_id: Int,
    val subject_abbreviation: String,
    val subject_id: Int,
    val subject_name: String,
    val updated_at: String
)

data class SubjectAvailabilityMapping(
    val branch_id: String,
    val course: CourseXX,
    val course_id: Int,
    val created_at: String,
    val deleted_at: Any,
    val semester_id: Int,
    val subject_abbreviation: String,
    val subject_id: Int,
    val subject_name: String,
    val updated_at: String
)

data class University(
    val created_at: String,
    val deleted_at: Any,
    val university_abbreviation: String,
    val university_active_status: Boolean,
    val university_admin_user_id: Int,
    val university_id: Int,
    val university_name: String,
    val updated_at: String
)

data class UniversityX(
    val created_at: String,
    val deleted_at: Any,
    val university_abbreviation: String,
    val university_active_status: Boolean,
    val university_admin_user_id: Int,
    val university_id: Int,
    val university_name: String,
    val updated_at: String
)

data class UniversityXX(
    val created_at: String,
    val deleted_at: Any,
    val university_abbreviation: String,
    val university_active_status: Boolean,
    val university_admin_user_id: Int,
    val university_id: Int,
    val university_name: String,
    val updated_at: String
)