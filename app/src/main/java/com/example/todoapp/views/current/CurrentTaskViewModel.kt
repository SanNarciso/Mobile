package com.example.todoapp.views.current

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.model.task.InDatabaseTaskRepository
import com.example.todoapp.model.task.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CurrentTaskViewModel : ViewModel() {

    private val taskRepository = InDatabaseTaskRepository.get()

    private val _task = MutableLiveData<Task>()
    val task: LiveData<Task> = _task

    fun updateTask(
        isCompleted: Boolean? = null,
        text: String? = null,
        additionalInfo: String? = null,
        isFavorite: Boolean? = null) {

        val task = this._task.value!!
        val newTask = task.copy(
            isCompleted = isCompleted ?: task.isCompleted,
            text = text ?: task.text,
            additionalInfo = additionalInfo ?: task.additionalInfo,
            isFavorite = isFavorite ?: task.isFavorite
        )
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.updateTask(newTask)
        }
        _task.value = newTask
    }

//    fun deleteTask() {
//        taskRepository.removeTask(_task.value!!)
//    }

    fun initState(task: Task) {
        _task.value = task
    }

}