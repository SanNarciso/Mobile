package com.example.todoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.todoapp.databinding.ActivityMainBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.model.task.Task


class MainActivity : AppCompatActivity(), TasksListener {

    lateinit var binding: ActivityMainBinding

    private var adapter = TasksAdapter(this)
    private var tasks = mutableListOf<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
        fetchData()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList("KEY", tasks as ArrayList<Task>)
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
    }

    private fun fetchData() {
        tasks.add(Task(true, "Task 0", true))
        for (i in 1..10) {
            tasks.add(
                Task(false, "Task $i", false)
            )
        }
        adapter.tasks = tasks
    }

    override fun updateTask(task: Task) {
        //todo change text to id
        tasks.forEachIndexed { ind, t ->
            if (t.text == task.text) tasks[ind] = task
        }
    }

    override fun removeTask(task: Task) {
        tasks.remove(task)
    }

}