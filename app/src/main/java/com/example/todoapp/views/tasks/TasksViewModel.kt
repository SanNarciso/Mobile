package com.example.todoapp.views.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todoapp.TasksListener
import com.example.todoapp.model.task.Task

class TasksViewModel : ViewModel(), TasksListener {

    private val _tasks = MutableLiveData<MutableList<Task>>()
    val tasks: LiveData<MutableList<Task>> = _tasks

    override fun updateTask(task: Task) {
        val list = _tasks.value ?: return
        list.forEachIndexed { ind, t ->
            if (t.text == task.text) list[ind] = task
        }
        _tasks.value = list
    }

    override fun removeTask(task: Task) {
        val list = _tasks.value ?: return
        list.remove(task)
        _tasks.value = list
    }

    fun initState(state: MutableList<Task>? = null) {
        if (state != null) {
            _tasks.value = state!!
            return
        }
        val tasks = mutableListOf<Task>()
        tasks.add(Task(true, "Task 0", true))
        for (i in 1..10) {
            tasks.add(
                Task(false, "Task $i", false)
            )
        }
        _tasks.value = tasks
    }


}