package com.streamed.data.models.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.Column

object UserTable: Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val name: Column<String> = varchar("name", 30)
    val surname: Column<String> = varchar("surname", 30)
    val email: Column<String> = varchar("email", 100).uniqueIndex()
    val password: Column<String> = varchar("password", 50)
    val role: Column<String> = varchar("role", 20)
    val isActive: Column<Boolean> = bool("is_active")

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}