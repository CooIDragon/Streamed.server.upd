package com.streamed.data.models

@kotlinx.serialization.Serializable
data class CourseModel(
    val id: Int,
    val duration: Int,
    val price: Int,
    val theme: String,
    val name: String,
    val description: String,
    val ownerId: Int
)
