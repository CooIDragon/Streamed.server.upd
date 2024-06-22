package com.streamed.data.models.tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object UsersCourseTable: Table() {
    val userId: Column<Int> = integer("user_id").references(UserTable.id)
    val courseId: Column<Int> = integer("course_id").references(CourseTable.id)
}