package com.example.todoapp

import com.example.todoapp.model.task.Task

interface TasksListener {

    fun updateTask(task: Task)

    fun removeTask(task: Task)

}