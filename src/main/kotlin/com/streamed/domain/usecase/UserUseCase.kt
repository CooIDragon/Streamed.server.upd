package com.streamed.domain.usecase

import com.auth0.jwt.JWTVerifier
import com.streamed.auth.JwtService
import com.streamed.data.models.UserModel
import com.streamed.domain.repository.UserRepository
import java.time.Instant
import java.util.*

class UserUseCase (
    private val repositoryImpl: UserRepository,
    private val jwtService: JwtService
) {

    suspend fun createUser(userModel: UserModel) = repositoryImpl.insertUser(user = userModel)

    suspend fun findUserByEmail(email: String) = repositoryImpl.getUserByEmail(email = email)

    suspend fun updateUser(userModel: UserModel) = repositoryImpl.updateUser(user = userModel)

    suspend fun deleteUser(userId: Int) = repositoryImpl.deleteUser(userId = userId)

    suspend fun updatePassword(userModel: UserModel, newHashPassword: String) = repositoryImpl.updatePassword(user = userModel, newHashPassword)

    fun generateToken(userModel: UserModel): String = jwtService.generateToken(user = userModel)

    fun getJwtVerifier(): JWTVerifier = jwtService.getVerifier()
}