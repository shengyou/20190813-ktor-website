package com.example

import com.example.schema.Posts
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
    Database.connect(
        "jdbc:mysql://127.0.0.1:8889/ktor_local?useUnicode=true&characterEncoding=utf-8&useSSL=false",
        "com.mysql.jdbc.Driver",
        "root",
        "root"
    )

    transaction {
        SchemaUtils.create(Posts)
    }
}