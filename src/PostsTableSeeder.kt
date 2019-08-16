package com.example

import com.example.schema.Posts
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

fun main() {
    Database.connect(
        "jdbc:mysql://127.0.0.1:8889/ktor_local?useUnicode=true&characterEncoding=utf-8&useSSL=false",
        "com.mysql.jdbc.Driver",
        "root",
        "root"
    )

    transaction {
        for (id in 1..10) {
            Posts.insert {
                it[title] = "Title $id"
                it[content] = "Post content $id"
                it[createdAt] = DateTime.now()
                it[updatedAt] = DateTime.now()
            }
        }
    }
}