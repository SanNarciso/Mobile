package com.example.todoapp.views.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.model.task.InDatabaseTaskRepository
import com.example.todoapp.model.task.Subscriber
import com.example.todoapp.model.task.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TasksViewModel : ViewModel(), Subscriber {

    private val taskRepository = InDatabaseTaskRepository.get()

    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> = taskRepository.getTasks()
    val completedTasks: LiveData<List<Task>> = taskRepository.getCompletedTasks()
    val favoriteTasks: LiveData<List<Task>> = taskRepository.getFavoriteTasks()

    init {
        taskRepository.addSubscriber(this)
    }

    override fun onCleared() {
        super.onCleared()
        taskRepository.removeSubscriber(this)
    }

    fun updateTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.updateTask(task)
        }
    }

    fun onMoveTask(from: Int, to: Int) {
        taskRepository.moveTask(from, to)
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

    fun getTasks() {
        val tasks = taskRepository.getTasks()
        _tasks.value = tasks.value
    }

    override fun setChanges(tasks: List<Task>) {
        _tasks.value = tasks
    }

    fun removeTaskFromLiveData(task: Task) {

    }

    fun initState(state: MutableList<Task>? = null) {
        if (state != null) {
            _tasks.value = state!!
            return
        }

    }


}
