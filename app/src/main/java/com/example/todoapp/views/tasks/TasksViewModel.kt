package com.example.todoapp.views.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todoapp.model.task.InDatabaseTaskRepository
import com.example.todoapp.model.task.Subscriber
import com.example.todoapp.model.task.Task

class TasksViewModel : ViewModel(), Subscriber {

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

    fun updateTask(task: Task) {
        taskRepository.updateTask(task)
    }

    fun onMoveTask(from: Int, to: Int) {
        taskRepository.moveTask(from, to)
    }

    fun removeTask(task: Task) {
        taskRepository.removeTask(task)
    }

    fun createTask(task: Task) {
        taskRepository.add(task)
        getTasks()
    }

    fun getTasks() {
        val tasks = taskRepository.getTasks()
        _tasks.value = tasks
    }

    override fun setChanges(tasks: List<Task>) {
        _tasks.value = tasks
    }

    fun initState(state: MutableList<Task>? = null) {
        if (state != null) {
            _tasks.value = state!!
            return
        }
//        taskRepository.add(Task(isCompleted = true, text = "Task 0", additionalInfo = "addit info", isFavourite = true))
//        for (i in 1..10) {
//            taskRepository.add(
//                Task(isCompleted = false, text = "Task $i", additionalInfo = "" , isFavourite = false)
//            )
//        }
    }


}
