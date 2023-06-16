package com.example.todoapp.model.task

interface TaskRepository : Repository {

    fun getTasks(): List<Task>

    fun updateTask(task: Task)

    fun removeTask(task: Task)

    fun add(task: Task)

}