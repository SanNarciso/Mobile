package com.example.todoapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.todoapp.model.task.Task
import java.util.*

@Dao
interface TaskDao {

    @Query("SELECT * FROM task WHERE isCompleted = 0")
    fun getTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE isFavorite = 1 AND isCompleted = 0")
    fun getFavouriteTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE isCompleted = 1")
    fun getCompletedTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE id=(:id)")
    fun getTask(id: UUID): LiveData<Task?>

    @Insert
    suspend fun addTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)
}