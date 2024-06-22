package com.streamed.data.models

@kotlinx.serialization.Serializable
data class CommentsModel(
    val id: Int,
    val ownerName: String,
    val ownerSurname: String,
    val webinarId: Int,
    val text: String,
    val isAnon: Boolean
)
