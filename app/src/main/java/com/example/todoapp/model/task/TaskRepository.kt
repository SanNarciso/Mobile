package com.example.todoapp.model.task

import androidx.lifecycle.LiveData

interface TaskRepository : Repository {

    fun getTasks(): LiveData<List<Task>>

    fun getFavoriteTasks(): LiveData<List<Task>>

    fun getCompletedTasks(): LiveData<List<Task>>

    suspend fun updateTask(task: Task)

    suspend fun removeTask(task: Task)

    suspend fun add(task: Task)

    fun moveTask(from: Int, to: Int)

}