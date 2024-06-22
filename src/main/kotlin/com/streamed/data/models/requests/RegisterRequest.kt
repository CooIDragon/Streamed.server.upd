package com.streamed.data.models.requests

import com.streamed.data.models.Roles

@kotlinx.serialization.Serializable
data class RegisterRequest(
    val name: String,
    val surname: String,
    val password: String,
    val email: String,
    val role: String,
    val isActive: Boolean = false,
)
