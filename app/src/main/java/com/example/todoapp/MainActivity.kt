package com.example.todoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.todoapp.databinding.ActivityMainBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.model.task.Task


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private var adapter = TasksAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
        fetchData()
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
    }

    private fun fetchData() {
        val tasks = mutableListOf<Task>()
        tasks.add(Task(true, "Task 0", true))
        for (i in 1..10) {
            tasks.add(
                Task(false, "Task $i", false)
            )
        }
        adapter.tasks = tasks
    }

}