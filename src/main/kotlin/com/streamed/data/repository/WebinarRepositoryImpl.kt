package com.streamed.data.repository

import com.streamed.data.models.WebinarModel
import com.streamed.data.models.tables.CourseTable
import com.streamed.data.models.tables.UsersCourseTable
import com.streamed.data.models.tables.WebinarTable
import com.streamed.domain.repository.WebinarRepository
import com.streamed.plugins.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.*

class WebinarRepositoryImpl : WebinarRepository {
    override suspend fun addWebinar(webinar: WebinarModel) {
        val uuid = UUID.randomUUID().toString()
        dbQuery {
            WebinarTable.insert {
                table ->
                table[name] = webinar.name
                table[inviteCode] = uuid.substring(0, 8)
                table[date] = webinar.date
                table[courseId] = webinar.courseId
            }
        }
    }

    override suspend fun getAllWebinars(courseId: Int): List<WebinarModel> {
        return dbQuery {
            WebinarTable.select {
                WebinarTable.courseId eq courseId
            }.mapNotNull { rowToWebinar(it) }
        }
    }

    override suspend fun getAllSubs(userId: Int): List<WebinarModel> {
        return dbQuery {
            val courseIds = UsersCourseTable
                .slice(UsersCourseTable.courseId)
                .select { UsersCourseTable.userId eq userId }
                .map { it[UsersCourseTable.courseId] }

            if (courseIds.isNotEmpty()) {
                WebinarTable
                    .select { WebinarTable.courseId inList courseIds }
                    .mapNotNull { rowToWebinar(it) }
            } else {
                emptyList()
            }
        }
    }

    override suspend fun getAllCreated(ownerId: Int): List<WebinarModel> {
        return dbQuery {
            val courseIds = CourseTable
                .slice(CourseTable.id)
                .select { CourseTable.ownerId eq ownerId }
                .map { it[CourseTable.id] }

            WebinarTable
                .select { WebinarTable.courseId inList courseIds }
                .mapNotNull { rowToWebinar(it) }
        }
    }

    override suspend fun updateWebinar(webinar: WebinarModel) {
        dbQuery {
            WebinarTable.update(where = {WebinarTable.id.eq(webinar.id)} ) {
                table ->
                table[name] = webinar.name
                table[date] = webinar.date
                table[courseId] = webinar.courseId
            }
        }
    }

    override suspend fun deleteWebinar(webinarId: Int) {
        dbQuery {
            WebinarTable.deleteWhere { WebinarTable.id.eq(webinarId) }
        }
    }

    private fun rowToWebinar(row: ResultRow): WebinarModel? {
        if (row == null) {
            return null
        }

        return WebinarModel(
            id = row[WebinarTable.id],
            name = row[WebinarTable.name],
            inviteCode = row[WebinarTable.inviteCode],
            date = row[WebinarTable.date],
            courseId = row[WebinarTable.courseId]
        )
    }
}
