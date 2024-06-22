package com.streamed.data.models

@kotlinx.serialization.Serializable
data class WebinarModel (
    val id: Int,
    val name: String,
    val inviteCode: String = "null",
    val date: String,
    val courseId: Int
)