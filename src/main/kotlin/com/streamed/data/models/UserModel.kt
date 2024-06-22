package com.streamed.data.models

import io.ktor.server.auth.*

@kotlinx.serialization.Serializable
data class UserModel (
    val id: Int,
    val name: String,
    val surname: String,
    val password: String,
    val email: String,
    val role: Roles,
    val isActive: Boolean = true,
): Principal
