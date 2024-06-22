package com.streamed.data.models.response

@kotlinx.serialization.Serializable
data class BaseResponse(
    val success: Boolean,
    val message: String
)
