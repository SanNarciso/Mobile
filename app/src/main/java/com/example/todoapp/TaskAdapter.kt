package com.example.todoapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.databinding.TaskItemBinding
import com.example.todoapp.model.task.Task

class TasksAdapter : RecyclerView.Adapter<TasksAdapter.TasksViewHolder>() {

    var tasks: List<Task> = mutableListOf()
        set(newValue) {
            if (newValue != field) {
                field = newValue
                notifyDataSetChanged()
            }
        }

    class TasksViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = TaskItemBinding.bind(view)

        fun bind(task: Task) {
            binding.isCompletedCheckBox.isChecked = task.isCompleted
            binding.isCompletedCheckBox.text = task.text

            val imageRes = if (task.isFavorite) R.drawable.ic_star else R.drawable.ic_star_border
            binding.isFavouriteImageButton.setImageResource(imageRes)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TasksViewHolder(view)

    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    override fun getItemCount(): Int = tasks.size

}