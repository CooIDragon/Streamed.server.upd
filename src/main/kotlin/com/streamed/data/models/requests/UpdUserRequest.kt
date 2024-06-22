package com.streamed.data.models.requests

@kotlinx.serialization.Serializable
data class UpdUserRequest(
    val name: String,
    val surname: String,
    val email: String
)
