package com.streamed.data.repository

import com.streamed.data.models.UsersCourseModel
import com.streamed.data.models.tables.UsersCourseTable
import com.streamed.domain.repository.UsersCourseRepository
import com.streamed.plugins.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class UsersCourseRepositoryImpl: UsersCourseRepository {
    override suspend fun subscribeUser(usersCourseModel: UsersCourseModel) {
        dbQuery {
            UsersCourseTable.insert {
                table ->
                table[userId] = usersCourseModel.userId
                table[courseId] = usersCourseModel.courseId
            }
        }
    }

    override suspend fun getAllSubCourses(userId: Int): List<Int> {
        return dbQuery {
            UsersCourseTable
                .slice(UsersCourseTable.courseId)
                .select { UsersCourseTable.userId.eq(userId) }
                .map { it[UsersCourseTable.courseId] }
        }
    }

    override suspend fun unsubscribeUser(userId: Int, courseId: Int) {
        dbQuery {
            UsersCourseTable.deleteWhere {
                UsersCourseTable.userId.eq(userId) and UsersCourseTable.courseId.eq(courseId)
            }
        }
    }
}