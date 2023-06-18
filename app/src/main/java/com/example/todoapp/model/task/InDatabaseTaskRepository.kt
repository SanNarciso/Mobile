package com.example.todoapp.model.task

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.todoapp.database.TaskDatabase
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "tasks-database"

class InDatabaseTaskRepository private constructor(context: Context): TaskRepository {

    private val database: TaskDatabase = Room.databaseBuilder(
        context.applicationContext,
        TaskDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val tasksDao = database.taskDao()

    private var tasks: MutableList<Task> =  mutableListOf()


    override fun getTasks(): LiveData<List<Task>> = tasksDao.getTasks()
    override fun getCompletedTasks(): LiveData<List<Task>> = tasksDao.getCompletedTasks()
    override fun getFavoriteTasks(): LiveData<List<Task>> = tasksDao.getFavouriteTasks()

    override suspend fun updateTask(task: Task) {
        tasks.forEachIndexed { ind, t ->
            if (t.id == task.id) {
                tasks[ind] = task
            }
        }
        tasksDao.updateTask(task)
    }

    override suspend fun removeTask(task: Task) {
        tasks.remove(task)
        tasksDao.deleteTask(task)
    }

    override suspend fun add(task: Task) {
        tasks.add(task)
        tasksDao.addTask(task)
    }

    override fun moveTask(from: Int, to: Int) {
        Collections.swap(tasks, from, to)
    }



    companion object {

        private var INSTANCE: InDatabaseTaskRepository? = null

        fun get(): InDatabaseTaskRepository {
            return INSTANCE ?: throw IllegalStateException("InDatabaseTaskRepository must be initialize")
        }

        fun initial(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = InDatabaseTaskRepository(context)
            }
        }

    }

}