package com.streamed.data.models.requests

@kotlinx.serialization.Serializable
data class ResetPassRequest (
    val email: String
)