package com.streamed.data.repository

import com.streamed.data.models.CourseModel
import com.streamed.data.models.tables.CourseTable
import com.streamed.domain.repository.CourseRepository
import com.streamed.plugins.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class CourseRepositoryImpl: CourseRepository {
    override suspend fun addCourse(course: CourseModel) {
        dbQuery {
            CourseTable.insert {
                table ->
                table[duration] = course.duration
                table[price] = course.price
                table[theme] = course.theme
                table[name] = course.name
                table[description] = course.description
                table[ownerId] = course.ownerId
            }
        }
    }

    override suspend fun getAllCourses(): List<CourseModel> {
        return dbQuery {
            CourseTable.selectAll()
                .mapNotNull { rowToCourse(it) }
        }
    }

    override suspend fun getMyCourses(userId: Int): List<CourseModel> {
        return dbQuery {
            CourseTable.select {
                CourseTable.ownerId eq userId
            }.mapNotNull { rowToCourse(it) }
        }
    }

    override suspend fun updateCourse(course: CourseModel, ownerId: Int) {
        dbQuery {
            CourseTable.update (
                where = {
                    CourseTable.ownerId.eq(ownerId) and CourseTable.id.eq(course.id)
                }
            ) {
                table ->
                table[duration] = course.duration
                table[price] = course.price
                table[theme] = course.theme
                table[name] = course.name
                table[description] = course.description
                table[CourseTable.ownerId] = ownerId
            }
        }
    }

    override suspend fun deleteCourse(courseId: Int, ownerId: Int) {
        dbQuery {
            CourseTable.deleteWhere { CourseTable.id.eq(courseId) and CourseTable.ownerId.eq(ownerId) }
        }
    }

    private fun rowToCourse(row: ResultRow): CourseModel? {
        if (row == null) {
            return null
        }
        
        return CourseModel(
            id = row[CourseTable.id],
            duration = row[CourseTable.duration],
            price = row[CourseTable.price],
            theme = row[CourseTable.theme],
            name = row[CourseTable.name],
            description = row[CourseTable.description],
            ownerId = row[CourseTable.ownerId]
        )
    }
}
