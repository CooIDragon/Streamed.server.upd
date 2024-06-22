package com.streamed.routes

import com.streamed.data.models.CourseModel
import com.streamed.data.models.Roles
import com.streamed.data.models.UserModel
import com.streamed.data.models.UsersCourseModel
import com.streamed.data.models.requests.AddCourseRequest
import com.streamed.data.models.requests.AddSubscribe
import com.streamed.data.models.response.BaseResponse
import com.streamed.domain.usecase.UsersCourseUseCase
import com.streamed.utils.Constants
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.SubscribeRoute(usersCourseUseCase: UsersCourseUseCase) {
    authenticate("jwt") {
        post("api/v1/subscribe-user") {
            val subscribeRequest = call.receiveNullable<AddSubscribe>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.MISSING_FIELDS))
                return@post
            }

            try {
                if (call.principal<UserModel>()!!.role == Roles.STUDENT) {
                    val subscribe = UsersCourseModel(
                        courseId = subscribeRequest.courseId,
                        userId = call.principal<UserModel>()!!.id
                    )

                    usersCourseUseCase.subscribeUser(subscribe)
                    call.respond(HttpStatusCode.OK,
                        BaseResponse(success = true, message = Constants.Success.ADDED_SUCCESSFULLY))
                } else {
                    call.respond(HttpStatusCode.Conflict, BaseResponse(false, Constants.Error.ACCESS_RESTRICTED))
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, BaseResponse(false, e.message ?: Constants.Error.GENERAL))
            }
        }

        delete("api/v1/unsubscribe-user") {
            val subscribeRequest = call.receiveNullable<AddSubscribe>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.MISSING_FIELDS))
                return@delete
            }

            try {
                if (call.principal<UserModel>()!!.role == Roles.STUDENT) {
                    val userId = call.principal<UserModel>()!!.id
                    usersCourseUseCase.unsubscribeUser(userId, subscribeRequest.courseId)
                    call.respond(HttpStatusCode.OK, BaseResponse(true, Constants.Success.DELETED_SUCCESSFULLY))
                } else {
                    call.respond(HttpStatusCode.Conflict, BaseResponse(false, Constants.Error.ACCESS_RESTRICTED))
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, BaseResponse(false, e.message ?: Constants.Error.GENERAL))
            }
        }

        get("api/v1/get-all-sub-courses") {
            val userId = call.principal<UserModel>()!!.id

            try {
                val course = usersCourseUseCase.getAllSubCourses(userId)
                call.respond(HttpStatusCode.OK, course)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, BaseResponse(false, e.message ?: Constants.Error.GENERAL))
            }
        }
    }
}