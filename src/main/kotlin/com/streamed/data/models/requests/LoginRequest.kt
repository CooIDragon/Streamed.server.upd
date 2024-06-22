package com.streamed.data.models.requests

@kotlinx.serialization.Serializable
data class LoginRequest(
    val email: String,
    val password: String
)
