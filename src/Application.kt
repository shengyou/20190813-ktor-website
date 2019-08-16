package com.example

import com.example.model.Post
import com.example.schema.Posts
import freemarker.cache.ClassTemplateLoader
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.freemarker.FreeMarker
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
    }

    Database.connect(
        "jdbc:mysql://127.0.0.1:8889/ktor_local?useUnicode=true&characterEncoding=utf-8&useSSL=false",
        "com.mysql.jdbc.Driver",
        "root",
        "root"
    )

    routing {
        static("static") {
            resources("static")
        }

        get("/") {
            call.respond(
                FreeMarkerContent(
                    "home/index.ftl",
                    mapOf("title" to "Hello, world")
                )
            )
        }

        get("/posts") {
            val posts = transaction {
                Posts.selectAll()
                    .orderBy(Posts.updatedAt to SortOrder.DESC)
                    .limit(3)
                    .map {
                        Post(
                            it[Posts.id],
                            it[Posts.title],
                            it[Posts.content],
                            it[Posts.createdAt],
                            it[Posts.updatedAt]
                        )
                    }
            }

            call.respond(
                FreeMarkerContent(
                    "posts/index.ftl",
                    mapOf("posts" to posts)
                )
            )
        }

        get("/posts/{id}") {
            val id = call.parameters["id"]?.toInt()!!
            val post = transaction {
                Posts.select { Posts.id eq id }
                    .map {
                        Post(
                            it[Posts.id],
                            it[Posts.title],
                            it[Posts.content],
                            it[Posts.createdAt],
                            it[Posts.updatedAt]
                        )
                    }
                    .singleOrNull()
            }

            if (post == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(
                    FreeMarkerContent(
                        "posts/show.ftl",
                        mapOf("post" to post)
                    )
                )
            }
        }
    }

}

