package com.example.todoapp.model.task

import java.util.*

class InDatabaseTaskRepository : TaskRepository, Observer {

    private var tasks: MutableList<Task> =  mutableListOf()

    override val subscribers = mutableListOf<Subscriber>()

    override fun getTasks(): List<Task> = tasks as List<Task>

    override fun updateTask(task: Task) {
        tasks.forEachIndexed { ind, t ->
            if (t.id == task.id) {
                tasks[ind] = task
            }
        }
        notifySubscribers()
    }

    override fun removeTask(task: Task) {
        tasks.remove(task)
        notifySubscribers()
    }

    override fun add(task: Task) {
        tasks.add(task)
        notifySubscribers()
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

        fun initial() {
            if (INSTANCE == null) {
                INSTANCE = InDatabaseTaskRepository()
            }
        }

    }

}