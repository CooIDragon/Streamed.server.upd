package com.streamed.data.models.tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object CommentsTable: Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val ownerName: Column<String> = varchar("ownerName", 64)
    val ownerSurname: Column<String> = varchar("ownerSurname", 64)
    val webinarId: Column<Int> = integer("webinarId").references(WebinarTable.id)
    val text: Column<String> = varchar("text", 255)
    val isAnon: Column<Boolean> = bool("isAnon")

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}