package com.example.todoapp.database

import androidx.room.*
import com.example.todoapp.model.task.Task
import java.util.*

@Dao
interface TaskDao {

    @Query("SELECT * FROM task")
    fun getTasks(): List<Task>

    @Query("SELECT * FROM task WHERE id=(:id)")
    fun getTask(id: UUID): Task?

    @Insert
    fun addTask(task: Task)

    @Update
    fun updateTask(task: Task)

    @Delete
    fun deleteTask(task: Task)
}