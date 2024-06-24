package com.streamed.domain.usecase

import com.streamed.data.models.WebinarModel
import com.streamed.data.models.tables.UsersCourseTable.userId
import com.streamed.domain.repository.WebinarRepository


class WebinarUseCase (
    private val webinarRepository: WebinarRepository
) {
    suspend fun addWebinar(webinar: WebinarModel) {
        webinarRepository.addWebinar(webinar = webinar)
    }

    suspend fun getAllWebinars(courseId: Int): List<WebinarModel> {
        return webinarRepository.getAllWebinars(courseId)
    }

    suspend fun getAllSubs(userId: Int): List<WebinarModel> {
        return webinarRepository.getAllSubs(userId = userId)
    }

    suspend fun getAllCreated(ownerId: Int): List<WebinarModel> {
        return webinarRepository.getAllCreated(ownerId = ownerId)
    }

    suspend fun updateWebinar(webinar: WebinarModel) {
        webinarRepository.updateWebinar(webinar)
    }

    suspend fun deleteWebinar(webinarId: Int) {
        webinarRepository.deleteWebinar(webinarId)
    }

    suspend fun getByCode(code: String): List<WebinarModel> {
        return webinarRepository.getWebinarByCode(code = code)
    }
}