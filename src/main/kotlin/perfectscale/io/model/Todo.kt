package io.perfectscale.model

import kotlinx.serialization.Serializable

enum class Priority {
    Low, Medium, High, Vital
}

@Serializable
data class Todo(
    val name: String,
    val description: String,
    val priority: Priority
)