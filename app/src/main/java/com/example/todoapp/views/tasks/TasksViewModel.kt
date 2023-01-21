package com.example.todoapp.views.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.model.task.InDatabaseTaskRepository
import com.example.todoapp.model.task.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TasksViewModel : ViewModel() {

    private val taskRepository = InDatabaseTaskRepository.get()

    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> = taskRepository.getTasks()
    val completedTasks: LiveData<List<Task>> = taskRepository.getCompletedTasks()
    val favoriteTasks: LiveData<List<Task>> = taskRepository.getFavoriteTasks()


    fun updateTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.updateTask(task)
        }
    }

    fun removeTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.removeTask(task)
        }
    }

    fun createTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.add(task)
        }
    }
}

