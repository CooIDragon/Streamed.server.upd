package com.streamed.domain.repository

import com.streamed.data.models.UserModel

interface UserRepository {
    suspend fun getUserByEmail(email: String): UserModel?
    suspend fun insertUser(user: UserModel)
    suspend fun updateUser(user: UserModel)
    suspend fun deleteUser(userId: Int)
    suspend fun updatePassword(user:UserModel, newHashPassword: String)
}