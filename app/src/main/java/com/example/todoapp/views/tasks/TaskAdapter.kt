package com.example.todoapp.views.tasks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.databinding.TaskItemBinding
import com.example.todoapp.model.task.Task

class TasksAdapter(private val listener: TasksListener) : RecyclerView.Adapter<TasksAdapter.TasksViewHolder>() {

    var tasks: List<Task> = mutableListOf()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    inner class TasksViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = TaskItemBinding.bind(view)

        fun bind(task: Task) = binding.run {

            root.setOnClickListener {
                listener.showTaskScreen(task)
            }

            isCompletedCheckBox.isChecked = task.isCompleted
            taskTitleTextView.text = task.text
            taskAdditInfoTextView.apply {
                if (task.additionalInfo.isNotBlank()) {
                    text = task.additionalInfo
                    visibility = View.VISIBLE
                } else {
                    visibility = View.GONE
                }
            }

            isCompletedCheckBox.setOnClickListener {
                task.isCompleted = isCompletedCheckBox.isChecked
                listener.onClickTask(task)
                notifyDataSetChanged()
            }

            val imageRes = if (task.isFavorite) R.drawable.ic_star else R.drawable.ic_star_border
            isFavoriteImageButton.setImageResource(imageRes)

            isFavoriteImageButton.setOnClickListener {
                task.isFavorite = !task.isFavorite
                listener.onClickTask(task)
                val imRes = if (task.isFavorite) R.drawable.ic_star else R.drawable.ic_star_border
                isFavoriteImageButton.setImageResource(imRes)
                notifyDataSetChanged()
            }

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
