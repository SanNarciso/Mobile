package com.example.todoapp

import android.app.Application
import com.example.todoapp.model.task.InDatabaseTaskRepository

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        InDatabaseTaskRepository.initial(this)


    }

}