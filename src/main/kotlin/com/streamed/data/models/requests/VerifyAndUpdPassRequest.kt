package com.streamed.data.models.requests

@kotlinx.serialization.Serializable
data class VerifyAndUpdPassRequest(
    val resetToken: String,
    val email: String,
    val newPass: String
)
