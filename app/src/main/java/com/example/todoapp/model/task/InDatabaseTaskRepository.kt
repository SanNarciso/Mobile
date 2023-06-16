package com.example.todoapp.model.task

class InDatabaseTaskRepository : TaskRepository {

    private var tasks: MutableList<Task> =  mutableListOf()

    override fun getTasks(): List<Task> = tasks as List<Task>

    override fun updateTask(task: Task) {

    }

    override fun removeTask(task: Task) {

    }

    override fun add(task: Task) {

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