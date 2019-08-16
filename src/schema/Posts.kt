package com.example.schema

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.joda.time.DateTime

object Posts : Table() {
    val id: Column<Int> = integer("id").primaryKey().autoIncrement()
    val title: Column<String> = varchar("title", 255)
    val content: Column<String> = text("content")
    val createdAt: Column<DateTime> = datetime("created_at")
    val updatedAt: Column<DateTime> = datetime("updated_at")
}