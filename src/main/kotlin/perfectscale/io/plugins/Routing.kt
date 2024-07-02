package io.perfectscale.plugins

import io.perfectscale.model.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.serialization.*
import io.ktor.server.request.*

fun Application.configureRouting() {
    routing {
        staticResources("static", "static")

        get("/todos") {
            val todos = TodoRepository.allTodos()
            call.respond(todos)
        }

        get("/todos/byName/{todoName}") {
                val name = call.parameters["todoName"]
                if (name == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }

                val todo = TodoRepository.todoByName(name)
                if (todo == null) {
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }
                call.respond(todo)
            }
        get("/todos/byPriority/{priority}") {
            val priorityAsText = call.parameters["priority"]
            if (priorityAsText == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            try {
                val priority = Priority.valueOf(priorityAsText)
                val todos = TodoRepository.todosByPriority(priority)

                if (todos.isEmpty()) {
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }
                call.respond(todos)
            } catch (ex: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
        post("/todos") {
                try {
                    val todo = call.receive<Todo>()
                    TodoRepository.addTodo(todo)
                    call.respond(HttpStatusCode.NoContent)
                } catch (ex: IllegalStateException) {
                    call.respond(HttpStatusCode.BadRequest)
                } catch (ex: JsonConvertException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
        }

        delete("/todos/{todoName}") {
                val name = call.parameters["todoName"]
                if (name == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@delete
                }

                if (TodoRepository.removeTodo(name)) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
    }
}