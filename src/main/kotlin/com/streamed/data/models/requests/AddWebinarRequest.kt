package com.streamed.data.models.requests

@kotlinx.serialization.Serializable
data class AddWebinarRequest(
    val id: Int? = null,
    val name: String,
    val date: String,
    val courseId: Int
)
