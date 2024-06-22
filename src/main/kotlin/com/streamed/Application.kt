package com.streamed

import com.streamed.auth.JwtService
import com.streamed.data.repository.*
import com.streamed.domain.repository.CommentsRepository
import com.streamed.domain.usecase.*
import com.streamed.plugins.DatabaseFactory.initDatabase
import com.streamed.plugins.configureMonitoring
import com.streamed.plugins.configureRouting
import com.streamed.plugins.configureSecurity
import com.streamed.plugins.configureSerialization
import io.ktor.network.tls.certificates.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.slf4j.LoggerFactory
import java.io.File

fun main() {
    val keyStoreFile = File("ssl/server.jks")
    val keyStore = buildKeyStore {
        certificate("sampleAlias") {
            password = "admin"
            domains = listOf("0.0.0.0", "localhost", "158.160.29.10")
        }
    }
    keyStore.saveToFile(keyStoreFile, "123456")

    val environment = applicationEngineEnvironment {
        log = LoggerFactory.getLogger("ktor.application")
        connector {
            port = 8080
        }
        sslConnector(
            keyStore = keyStore,
            keyAlias = "sampleAlias",
            keyStorePassword = { "123456".toCharArray() },
            privateKeyPassword = { "admin".toCharArray() }) {
            port = 8443
            keyStorePath = keyStoreFile
        }
        module(Application::module)
    }
    embeddedServer(Netty, environment)
        .start(wait = true)
}

fun Application.module() {
    val jwtService = JwtService()
    val userRepository = UserRepositoryImpl()
    val courseRepository = CourseRepositoryImpl()
    val webinarRepository = WebinarRepositoryImpl()
    val usersCourseRepository = UsersCourseRepositoryImpl()
    val commentsRepository = CommentsRepositoryImpl()
    val userUseCase = UserUseCase(userRepository, jwtService)
    val courseUseCase = CourseUseCase(courseRepository)
    val webinarUseCase = WebinarUseCase(webinarRepository)
    val usersCourseUseCase = UsersCourseUseCase(usersCourseRepository)
    val commentsUseCase = CommentsUseCase(commentsRepository)

    initDatabase()
    configureMonitoring()
    configureSerialization()
    configureSecurity(userUseCase)
    configureRouting(userUseCase, courseUseCase, webinarUseCase, usersCourseUseCase, commentsUseCase)
}
