package com.example.todoapp.model.task

class InDatabaseTaskRepository : TaskRepository, Observer {

    private var tasks: MutableList<Task> =  mutableListOf()

    override val subscribers = mutableListOf<Subscriber>()

    override fun getTasks(): List<Task> = tasks as List<Task>

    override fun updateTask(task: Task) {
        //todo change text to id
        tasks.forEachIndexed { ind, t ->
            if (t.text == task.text) tasks[ind] = task
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
