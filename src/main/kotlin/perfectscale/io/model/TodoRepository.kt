package io.perfectscale.model

import org.slf4j.LoggerFactory

object TodoRepository {
    private val todos = mutableListOf(
        Todo("cleaning", "Clean the house", Priority.Low),
        Todo("gardening", "Mow the lawn", Priority.Medium),
        Todo("shopping", "Buy the groceries", Priority.High),
        Todo("painting", "Paint the fence", Priority.Medium)
    )

    private val log = LoggerFactory.getLogger(TodoRepository::class.java)

    fun allTodos(): List<Todo> = todos

    fun todosByPriority(priority: Priority) = todos.filter {
        it.priority == priority
    }

    fun todoByName(name: String) = todos.find {
        it.name.equals(name, ignoreCase = true)
    }

    fun addTodo(todo: Todo) {
        log.info("In addTodo")
        if (todoByName(todo.name) != null) {
            log.info("Found todo")
            throw IllegalStateException("Cannot duplicate todo names!")
        }
        log.info("Adding ${todo}")
        todos.add(todo)
        log.info("Todos are  ${todos}")
    }

    fun removeTodo(name: String): Boolean {
        return todos.removeIf { it.name == name }
    }
}