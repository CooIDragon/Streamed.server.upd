package com.streamed.domain.usecase

import com.streamed.data.models.CourseModel
import com.streamed.domain.repository.CourseRepository

class CourseUseCase(
    private val courseRepository: CourseRepository
) {
    suspend fun addCourse(course: CourseModel) {
        courseRepository.addCourse(course = course)
    }

    suspend fun getAllCourses(): List<CourseModel> {
        return courseRepository.getAllCourses()
    }

    suspend fun getMyCourses(userId: Int): List<CourseModel> {
        return courseRepository.getMyCourses(userId = userId)
    }

    suspend fun updateCourse(course: CourseModel, ownerId: Int) {
        courseRepository.updateCourse(course = course, ownerId = ownerId)
    }

    suspend fun deleteCourse(courseId: Int, ownerId: Int) {
        courseRepository.deleteCourse(courseId = courseId, ownerId = ownerId)
    }
}
