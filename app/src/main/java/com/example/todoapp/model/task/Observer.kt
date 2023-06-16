package com.example.todoapp.model.task

interface Observer {
    fun setChanges(tasks: List<Task>)
}