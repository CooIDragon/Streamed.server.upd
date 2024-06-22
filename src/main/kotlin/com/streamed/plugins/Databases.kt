package com.streamed.plugins

import com.streamed.data.models.tables.*
import com.typesafe.config.ConfigFactory
import com.zaxxer.hikari.HikariConfig
import io.ktor.server.application.*
import io.ktor.server.config.*
import org.jetbrains.exposed.sql.Database
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    private val appConfig = HoconApplicationConfig(ConfigFactory.load())
    private val dbUrl = System.getenv("DB_POSTGRES_URL")
    private val dbUser = System.getenv("DB_POSTGRES_USER")
    private val dbPassword = System.getenv("DB_PASSWORD")

    fun Application.initDatabase() {
        Database.connect(getHikariDatasource())

        transaction {
            SchemaUtils.create(
                UserTable, CourseTable, UsersCourseTable, WebinarTable, CommentsTable
            )
        }
    }

    private fun getHikariDatasource(): HikariDataSource {
        println("DB URL: $dbUrl")
        println("DB USER: $dbUser")

        val config = HikariConfig()
        config.driverClassName = "org.postgresql.Driver"
        config.jdbcUrl = dbUrl
        config.username = dbUser
        config.password = dbPassword
        config.maximumPoolSize = 3
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()
        return HikariDataSource(config)
    }

    suspend fun <T> dbQuery(block: () -> T): T {
        return withContext(Dispatchers.IO) {
            transaction { block() }
        }
    }
}