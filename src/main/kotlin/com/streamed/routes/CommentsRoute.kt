package com.streamed.routes

import com.streamed.data.models.CommentsModel
import com.streamed.data.models.UserModel
import com.streamed.data.models.requests.AddCommentRequest
import com.streamed.data.models.response.BaseResponse
import com.streamed.domain.usecase.CommentsUseCase
import com.streamed.utils.Constants
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.CommentsRoute(commentsUseCase: CommentsUseCase) {
    authenticate("jwt") {
        post("api/v1/add-comment") {
            val addCommentRequest = call.receiveNullable<AddCommentRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.MISSING_FIELDS))
                return@post
            }

            try {
                val comment = CommentsModel(
                    id = 0,
                    ownerName = call.principal<UserModel>()!!.name,
                    ownerSurname = call.principal<UserModel>()!!.surname,
                    webinarId = addCommentRequest.webinarId,
                    text = addCommentRequest.text,
                    isAnon = addCommentRequest.isAnon
                )

                commentsUseCase.addComment(comment)
                call.respond(HttpStatusCode.OK, BaseResponse(true, message = Constants.Success.ADDED_SUCCESSFULLY))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, BaseResponse(false, e.message ?: Constants.Error.GENERAL))
            }
        }

        delete("api/v1/delete-comment") {
            val commentId = call.request.queryParameters[Constants.Value.COMMENT_ID]?.toInt() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.MISSING_FIELDS))
                return@delete
            }

            try {
                commentsUseCase.deleteComment(commentId = commentId)
                call.respond(HttpStatusCode.OK, BaseResponse(success = true, message = Constants.Success.DELETED_SUCCESSFULLY))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, BaseResponse(false, e.message ?: Constants.Error.GENERAL))
            }
        }

        get("api/v1/get-all-comments") {
            try {
                val webinarId = call.request.queryParameters[Constants.Value.WEBINAR_ID]?.toInt() ?: kotlin.run {
                    call.respond(HttpStatusCode.BadRequest, BaseResponse(false, Constants.Error.MISSING_FIELDS))
                    return@get
                }

                val comment = commentsUseCase.getAllComments(webinarId)
                call.respond(HttpStatusCode.OK, comment)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, BaseResponse(false, e.message ?: Constants.Error.GENERAL))
            }
        }
    }
}