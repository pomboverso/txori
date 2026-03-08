package com.rama.adiskide

data class Task(
    val id: Long = 0,
    var type: TaskType,
    var label: String,
    var difficulty: Int = 0,
    val dateCreation: Long,
    var dateCompletion: Long? = null
)