package com.example.todoapp.views.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todoapp.model.task.InDatabaseTaskRepository
import com.example.todoapp.model.task.Observer
import com.example.todoapp.model.task.Task

class TasksViewModel : ViewModel(), TasksListener, Observer {

    private val taskRepository = InDatabaseTaskRepository.get()

    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> = _tasks

    init {
        taskRepository.addSubscriber(this)
    }

    override fun onCleared() {
        super.onCleared()
        taskRepository.removeSubscriber(this)
    }

    override fun updateTask(task: Task) {
        taskRepository.updateTask(task)
    }

    override fun removeTask(task: Task) {
        taskRepository.removeTask(task)
    }

    fun createTask() {
        taskRepository.add(Task(false, "task", false))
    }

    override fun setChanges(tasks: List<Task>) {
        _tasks.value = tasks
    }

    fun initState(state: MutableList<Task>? = null) {
        if (state != null) {
            _tasks.value = state!!
            return
        }
        taskRepository.add(Task(true, "Task 0", true))
        for (i in 1..10) {
            taskRepository.add(
                Task(false, "Task $i", false)
            )
        }
    }


}