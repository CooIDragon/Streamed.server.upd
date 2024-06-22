package com.streamed.domain.usecase

import com.streamed.data.models.UsersCourseModel
import com.streamed.domain.repository.UsersCourseRepository

class UsersCourseUseCase(
    private val usersCourseRepository: UsersCourseRepository
) {
    suspend fun subscribeUser(usersCourseModel: UsersCourseModel) {
        usersCourseRepository.subscribeUser(usersCourseModel)
    }

    suspend fun getAllSubCourses(userID: Int): List<Int> {
        return usersCourseRepository.getAllSubCourses(userID)
    }
}