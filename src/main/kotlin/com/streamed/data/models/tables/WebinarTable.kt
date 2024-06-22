package com.streamed.data.models.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.Column

object WebinarTable: Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val name: Column<String> = varchar("name", 30)
    val inviteCode: Column<String> = varchar("invite_code", 8)
    val date: Column<String> = varchar("date", 24)
    val courseId: Column<Int> = integer("course_id").references(CourseTable.id)

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}