package com.example.todoapp.model.task

import androidx.lifecycle.LiveData

interface TaskRepository : Repository {

    fun getTasks(): LiveData<List<Task>>

    fun updateTask(task: Task)

    fun removeTask(task: Task)

    fun add(task: Task)

    fun moveTask(from: Int, to: Int)

}