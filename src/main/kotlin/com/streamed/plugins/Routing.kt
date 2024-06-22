package com.streamed.plugins

import com.streamed.domain.usecase.*
import com.streamed.routes.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(userUseCase: UserUseCase, courseUseCase: CourseUseCase, webinarUseCase: WebinarUseCase, usersCourseUseCase: UsersCourseUseCase, commentsUseCase: CommentsUseCase) {
    routing {
        UserRoute(userUseCase = userUseCase)
        CourseRoute(courseUseCase = courseUseCase)
        WebinarRoute(webinarUseCase = webinarUseCase)
        SubscribeRoute(usersCourseUseCase = usersCourseUseCase)
        CommentsRoute(commentsUseCase = commentsUseCase)
    }
}
