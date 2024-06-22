package com.streamed.data.models.requests

@kotlinx.serialization.Serializable
data class AddCourseRequest (
    val id: Int? = null,
    val duration: Int,
    val price: Int,
    val theme: String,
    val name: String,
    val description: String
)