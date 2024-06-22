package com.streamed.data.models.requests

@kotlinx.serialization.Serializable
data class AddCommentRequest(
    val id: Int? = null,
    val webinarId: Int,
    val text: String,
    val isAnon: Boolean
)
