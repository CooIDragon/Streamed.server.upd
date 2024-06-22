package com.streamed.domain.repository

import com.streamed.data.models.CourseModel

interface CourseRepository {
    suspend fun addCourse(course: CourseModel)
    suspend fun getAllCourses(): List<CourseModel>
    suspend fun updateCourse(course: CourseModel, ownerId: Int)
    suspend fun deleteCourse(courseId: Int, ownerId: Int)
    suspend fun getMyCourses(userId: Int): List<CourseModel>
}