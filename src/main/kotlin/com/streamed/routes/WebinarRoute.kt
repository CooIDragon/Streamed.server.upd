package com.streamed.routes

import com.streamed.data.models.Roles
import com.streamed.data.models.UserModel
import com.streamed.data.models.WebinarModel
import com.streamed.data.models.requests.AddWebinarRequest
import com.streamed.data.models.response.BaseResponse
import com.streamed.domain.usecase.WebinarUseCase
import com.streamed.utils.Constants
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.WebinarRoute(webinarUseCase: WebinarUseCase) {
    authenticate("jwt") {
        get("api/v1/get-webinar-by-id") {
            val courseRequest = call.request.queryParameters[Constants.Value.ID]?.toInt() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.MISSING_FIELDS))
                return@get
            }

            try {
                val webinar = webinarUseCase.getAllWebinars(courseRequest)
                call.respond(HttpStatusCode.OK, webinar)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, BaseResponse(false, e.message ?: Constants.Error.GENERAL))
            }
        }

        get("api/v1/get-webinar-by-code") {
            val codeRequest = call.request.queryParameters[Constants.Value.CODE] ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.MISSING_FIELDS))
                return@get
            }

            try {
                val webinar = webinarUseCase.getByCode(codeRequest)
                call.respond(HttpStatusCode.OK, webinar)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, BaseResponse(false, e.message ?: Constants.Error.GENERAL))
            }
        }

        get("api/v1/get-all-webinars-for-sub") {
            try {
                val userId = call.principal<UserModel>()!!.id
                val webinar = webinarUseCase.getAllSubs(userId)

                call.respond(HttpStatusCode.OK, webinar)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, BaseResponse(false, e.message ?: Constants.Error.GENERAL))
            }
        }

        get("api/v1/get-all-webinars-for-prof") {
            try {
                val ownerId = call.principal<UserModel>()!!.id
                val webinar = webinarUseCase.getAllCreated(ownerId)

                call.respond(HttpStatusCode.OK, webinar)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, BaseResponse(false, e.message ?: Constants.Error.GENERAL))
            }
        }

        post("api/v1/create-webinar") {
            val webinarRequest = call.receiveNullable<AddWebinarRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.MISSING_FIELDS))
                return@post
            }

            try {
                if (call.principal<UserModel>()!!.role != Roles.STUDENT) {
                    val webinar = WebinarModel(
                        id = 0,
                        name = webinarRequest.name,
                        date = webinarRequest.date,
                        courseId = webinarRequest.courseId
                    )

                    webinarUseCase.addWebinar(webinar = webinar)
                    call.respond(HttpStatusCode.OK, webinar)
                } else {
                    call.respond(HttpStatusCode.Conflict, BaseResponse(false, Constants.Error.ACCESS_RESTRICTED))
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, BaseResponse(false, e.message ?: Constants.Error.GENERAL))
            }
        }

        post("api/v1/update-webinar") {
            val webinarRequest = call.receiveNullable<AddWebinarRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.MISSING_FIELDS))
                return@post
            }

            try {
                val webinar = WebinarModel(
                    id = webinarRequest.id ?: 0,
                    name = webinarRequest.name,
                    date = webinarRequest.date,
                    courseId = webinarRequest.courseId
                )

                webinarUseCase.updateWebinar(webinar = webinar)
                call.respond(HttpStatusCode.OK, BaseResponse(success = true, message = Constants.Success.UPDATE_SUCCESSFULLY))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, BaseResponse(false, e.message ?: Constants.Error.GENERAL))
            }
        }

        delete("api/v1/delete-webinar") {
            val webinarRequest = call.request.queryParameters[Constants.Value.ID]?.toInt() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.MISSING_FIELDS))
                return@delete
            }

            try {
                webinarUseCase.deleteWebinar(webinarId = webinarRequest)
                call.respond(HttpStatusCode.OK, BaseResponse(success = true, message = Constants.Success.DELETED_SUCCESSFULLY))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, BaseResponse(false, e.message ?: Constants.Error.GENERAL))
            }
        }
    }
}
