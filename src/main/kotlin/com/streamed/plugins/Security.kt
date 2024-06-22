package com.streamed.plugins

import com.auth0.jwt.interfaces.Payload
import com.streamed.auth.JwtService
import com.streamed.data.models.Roles
import com.streamed.data.models.UserModel
import com.streamed.data.models.getRoleByString
import com.streamed.data.repository.UserRepositoryImpl
import com.streamed.domain.usecase.UserUseCase
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import kotlinx.coroutines.runBlocking

fun Application.configureSecurity(userUseCase: UserUseCase) {
    authentication {
        jwt("jwt") {
            verifier(userUseCase.getJwtVerifier())
            realm = "Service server"
            validate {
                val payload: Payload = it.payload
                val email = payload.getClaim("email").asString()
                val user = userUseCase.findUserByEmail(email = email)
                user
            }
        }
    }
}
