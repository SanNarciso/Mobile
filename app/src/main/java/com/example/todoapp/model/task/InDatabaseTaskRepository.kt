package com.example.todoapp.model.task

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.todoapp.database.TaskDatabase
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "tasks-database"

class InDatabaseTaskRepository private constructor(context: Context): TaskRepository, Observer {

    private val database: TaskDatabase = Room.databaseBuilder(
        context.applicationContext,
        TaskDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val tasksDao = database.taskDao()
    private val executor = Executors.newSingleThreadExecutor()

    private var tasks: MutableList<Task> =  mutableListOf()

    override val subscribers = mutableListOf<Subscriber>()

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
        //notifySubscribers()
    }

    override suspend fun removeTask(task: Task) {
        tasks.remove(task)
        tasksDao.deleteTask(task)
//        notifySubscribers()
    }

    override suspend fun add(task: Task) {
        tasks.add(task)
        tasksDao.addTask(task)
        //notifySubscribers()
    }

    override fun moveTask(from: Int, to: Int) {
        Collections.swap(tasks, from, to)
    }

    override fun addSubscriber(subscriber: Subscriber) {
        subscribers.add(subscriber)
    }

    override fun removeSubscriber(subscriber: Subscriber) {
        subscribers.remove(subscriber)
    }

    override fun notifySubscribers() {
        subscribers.forEach { it.setChanges(tasks) }
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