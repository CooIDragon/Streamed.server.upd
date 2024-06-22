package com.streamed.routes

import com.streamed.auth.hash
import com.streamed.data.models.UserModel
import com.streamed.data.models.getRoleByString
import com.streamed.data.models.requests.*
import com.streamed.data.models.response.BaseResponse
import com.streamed.domain.usecase.UserUseCase
import com.streamed.utils.Constants
import com.streamed.utils.EmailUtil
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Route.UserRoute(userUseCase: UserUseCase) {
    val hashFunction = {p: String -> hash(password = p)}
    post("api/v1/signup") {
        val registerRequest = call.receiveNullable<RegisterRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.GENERAL))
            return@post
        }

        try {
            val user = UserModel(
                id = 0,
                email = registerRequest.email.trim().lowercase(),
                name = registerRequest.name.trim(),
                surname = registerRequest.surname.trim(),
                password = hashFunction(registerRequest.password.trim()),
                role = registerRequest.role.trim().getRoleByString()
            )

            userUseCase.createUser(user)
            call.respond(HttpStatusCode.OK, BaseResponse(true, userUseCase.generateToken(userModel = user)))
        } catch (e: java.lang.Exception) {
            call.respond(HttpStatusCode.Conflict, BaseResponse(false, e.message ?: Constants.Error.GENERAL))
        }
    }

    post("api/v1/login") {
        val loginRequest = call.receiveNullable<LoginRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.GENERAL))
            return@post
        }

        try {
            val user = userUseCase.findUserByEmail(loginRequest.email.trim().lowercase())

            if (user == null) {
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.WRONG_EMAIL))
            } else {
                if (user.password == hashFunction(loginRequest.password)) {
                    call.respond(HttpStatusCode.OK, BaseResponse(true, userUseCase.generateToken(userModel = user)))
                } else {
                    call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.INCORRECT_PASSWORD))
                }
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, BaseResponse(false, e.message ?: Constants.Error.GENERAL))
        }
    }

    post("api/v1/reset_password_req") {
        val resetPassRequest = call.receiveNullable<ResetPassRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.GENERAL))
            return@post
        }

        val user = userUseCase.findUserByEmail(resetPassRequest.email.trim().lowercase())
        if (user != null) {
            val token = hashFunction(user.email).substring(0, 8)

            EmailUtil.sendPasswordResetEmail(user.email, token)
            call.respond(HttpStatusCode.OK, BaseResponse(true, "Password reset email sent"))
        } else {
            call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.USER_NOT_FOUND))
        }
    }

    post("api/v1/reset_password") {
        val verifyAndUpdPassRequest = call.receiveNullable<VerifyAndUpdPassRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.GENERAL))
            return@post
        }

        val user = userUseCase.findUserByEmail(verifyAndUpdPassRequest.email.trim().lowercase())
        if (user != null) {
            if (hashFunction(user.email).substring(0, 8) == verifyAndUpdPassRequest.resetToken) {
                userUseCase.updatePassword(user, newHashPassword = hashFunction(verifyAndUpdPassRequest.newPass))
                call.respond(HttpStatusCode.OK, BaseResponse(true, "Password updated successfully"))
            }
        } else {
            call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.USER_NOT_FOUND))
        }
    }

    authenticate("jwt") {

        get("api/v1/get-user-info") {
            try {
                val user = call.principal<UserModel>()

                if (user != null) {
                    call.respond(HttpStatusCode.OK, user)
                } else {
                    call.respond(HttpStatusCode.Conflict, BaseResponse(false, Constants.Error.USER_NOT_FOUND))
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.GENERAL))
            }
        }

        post("api/v1/upd-user") {
            try {
                val user = call.principal<UserModel>()

                if (user != null) {
                    val updUserRequest = call.receiveNullable<UpdUserRequest>() ?: kotlin.run {
                        call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.GENERAL))
                        return@post
                    }

                    val user2 = UserModel(
                        id = user.id,
                        email = updUserRequest.email,
                        name = updUserRequest.name,
                        surname = updUserRequest.surname,
                        password = user.password,
                        role = user.role
                    )

                    userUseCase.updateUser(user2)
                    call.respond(HttpStatusCode.OK, BaseResponse(true, userUseCase.generateToken(userModel = user2)))
                } else {
                    call.respond(HttpStatusCode.Conflict, BaseResponse(false, Constants.Error.USER_NOT_FOUND))
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.GENERAL))
            }
        }

        post("api/v1/delete-user") {
            try {
                val user = call.principal<UserModel>()

                if (user != null) {
                    userUseCase.deleteUser(user.id)
                    call.respond(HttpStatusCode.OK, "User deleted")
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.GENERAL))
            }
        }
    }
}