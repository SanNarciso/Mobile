package com.example.todoapp.views.tasks

import androidx.lifecycle.LifecycleOwner
import com.example.todoapp.model.task.Task

interface TasksListener : java.io.Serializable {

    fun onClickTask(task: Task)

    fun showTaskScreen(task:Task, adapterPosition: Int)

    fun onMoveTask(from: Int, to: Int)

    fun observeData(lifecycleOwner: LifecycleOwner, adapter: TasksAdapter, position: Int)

}