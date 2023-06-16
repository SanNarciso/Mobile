package com.example.todoapp.views.tasks

import com.example.todoapp.model.task.Task

interface TasksListener {

    fun onClickTask(task: Task)

    fun showTaskScreen(task:Task)


}