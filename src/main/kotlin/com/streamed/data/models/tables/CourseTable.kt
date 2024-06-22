package com.streamed.data.models.tables

import com.streamed.data.models.tables.WebinarTable.references
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.Column

object CourseTable: Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val duration: Column<Int> = integer("duration")
    val price: Column<Int> = integer("price")
    val theme: Column<String> = varchar("theme", 50)
    val name: Column<String> = varchar("name", 30)
    val description: Column<String> = varchar("description", 5000)
    val ownerId: Column<Int> = integer("owner_id")

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}