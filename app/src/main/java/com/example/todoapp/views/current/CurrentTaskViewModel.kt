package com.example.todoapp.views.current

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todoapp.model.task.InDatabaseTaskRepository
import com.example.todoapp.model.task.Task

class CurrentTaskViewModel : ViewModel() {

    private val taskRepository = InDatabaseTaskRepository.get()

    private val _task = MutableLiveData<Task>()
    val task: LiveData<Task> = _task

    fun updateTask(task: Task) {
        taskRepository.updateTask(task)
        _task.value = task
    }

    fun deleteTask() {
        taskRepository.removeTask(_task.value!!)
    }

    fun initState(task: Task) {
        _task.value = task
    }

}
