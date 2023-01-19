package com.example.todoapp.model.task

interface Subscriber {

    fun setChanges(tasks: List<Task>)

}

interface Observer {

    val subscribers: MutableList<Subscriber>

    fun addSubscriber(subscriber: Subscriber)

    fun removeSubscriber(subscriber: Subscriber)

    fun notifySubscribers()

}