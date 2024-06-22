package com.streamed.routes

import com.streamed.data.models.CourseModel
import com.streamed.data.models.Roles
import com.streamed.data.models.UserModel
import com.streamed.data.models.requests.AddCourseRequest
import com.streamed.data.models.response.BaseResponse
import com.streamed.domain.usecase.CourseUseCase
import com.streamed.utils.Constants
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.CourseRoute(courseUseCase: CourseUseCase) {
    get("api/v1/get-all-courses") {
        try {
            val course = courseUseCase.getAllCourses()
            call.respond(HttpStatusCode.OK, course)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, BaseResponse(false, e.message ?: Constants.Error.GENERAL))
        }
    }

    authenticate("jwt") {
        post("api/v1/get-my-courses") {
            try {
                val userId = call.principal<UserModel>()!!.id
                val course = courseUseCase.getMyCourses(userId)
                call.respond(HttpStatusCode.OK, course)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, BaseResponse(false, e.message ?: Constants.Error.GENERAL))
            }
        }
        post("api/v1/create-course") {
            val courseRequest = call.receiveNullable<AddCourseRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.MISSING_FIELDS))
                return@post
            }

            try {
                if (call.principal<UserModel>()!!.role != Roles.STUDENT) {
                    val course = CourseModel(
                        id = 0,
                        duration = courseRequest.duration,
                        price = courseRequest.price,
                        theme = courseRequest.theme,
                        name = courseRequest.name,
                        description = courseRequest.description,
                        ownerId = call.principal<UserModel>()!!.id
                    )

                    courseUseCase.addCourse(course = course)
                    call.respond(HttpStatusCode.OK,
                        BaseResponse(success = true, message = Constants.Success.ADDED_SUCCESSFULLY))
                } else {
                    call.respond(HttpStatusCode.Conflict, BaseResponse(false, Constants.Error.ACCESS_RESTRICTED))
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, BaseResponse(false, e.message ?: Constants.Error.GENERAL))
            }
        }

        post("api/v1/update-course") {
            val courseRequest = call.receiveNullable<AddCourseRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.MISSING_FIELDS))
                return@post
            }

            try {
                val ownerId = call.principal<UserModel>()!!.id
                val course = CourseModel(
                    id = courseRequest.id ?: 0,
                    ownerId = ownerId,
                    duration = courseRequest.duration,
                    price = courseRequest.price,
                    theme = courseRequest.theme,
                    name = courseRequest.name,
                    description = courseRequest.description,
                )

                courseUseCase.updateCourse(course = course, ownerId = ownerId)
                call.respond(HttpStatusCode.OK, BaseResponse(success = true, message = Constants.Success.UPDATE_SUCCESSFULLY))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, BaseResponse(false, e.message ?: Constants.Error.GENERAL))
            }
        }

        delete("api/v1/delete-course") {
            val courseRequest = call.request.queryParameters[Constants.Value.ID]?.toInt() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.MISSING_FIELDS))
                return@delete
            }

            try {
                val ownerId = call.principal<UserModel>()!!.id

                courseUseCase.deleteCourse(courseId = courseRequest, ownerId = ownerId)
                call.respond(HttpStatusCode.OK, BaseResponse(success = true, message = Constants.Success.DELETED_SUCCESSFULLY))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, BaseResponse(false, e.message ?: Constants.Error.GENERAL))
            }
        }
    }
}