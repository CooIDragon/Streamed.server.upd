package com.streamed.domain.repository

import com.streamed.data.models.WebinarModel

interface WebinarRepository {
    suspend fun addWebinar(webinar: WebinarModel)
    suspend fun getAllWebinars(courseId: Int): List<WebinarModel>
    suspend fun getWebinarByCode(code: String): List<WebinarModel>
    suspend fun getAllSubs(userId: Int): List<WebinarModel>
    suspend fun getAllCreated(ownerId: Int): List<WebinarModel>
    suspend fun updateWebinar(webinar: WebinarModel)
    suspend fun deleteWebinar(webinarId: Int)
}