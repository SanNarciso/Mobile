package com.example.todoapp.views.tasks

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.databinding.TaskItemBinding
import com.example.todoapp.model.task.Task
import java.util.*

class TasksDiffCallback(
    private val oldList: List<Task>,
    private val newList: List<Task>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldTask = oldList[oldItemPosition]
        val newTask = newList[newItemPosition]
        return oldTask.id == newTask.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldTask = oldList[oldItemPosition]
        val newTask = newList[newItemPosition]
        return oldTask == newTask
    }

}

class TasksAdapter(private val listener: TasksListener) : RecyclerView.Adapter<TasksAdapter.TasksViewHolder>() {

    var tasks: MutableList<Task> = mutableListOf()
        set(newValue) {
            val diffCallback = TasksDiffCallback(field, newValue)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            field = newValue
            diffResult.dispatchUpdatesTo(this)
            Log.d("tag", "update list")
            Log.d("tag", "new? ${newValue === field}")
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
                notifyDataChanged()
            }

            val imageRes = if (task.isFavorite) R.drawable.ic_star else R.drawable.ic_star_border
            isFavoriteImageButton.setImageResource(imageRes)

            isFavoriteImageButton.setOnClickListener {
                task.isFavorite = !task.isFavorite
                listener.onClickTask(task)
                val imRes = if (task.isFavorite) R.drawable.ic_star else R.drawable.ic_star_border
                isFavoriteImageButton.setImageResource(imRes)
                notifyDataChanged()
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

    fun setList(list: MutableList<Task>) {
        val _tasks = list
        tasks = _tasks
    }

    private fun notifyDataChanged() {
        val _tasks = tasks
        tasks = _tasks
    }

    fun moveItem(from: Int, to: Int) {
        Collections.swap(tasks, from, to)
        listener.onMoveTask(from, to)
    }

    fun addItem(item: Task, pos: Int? = null) {

        val _tasks = mutableListOf<Task>()
        tasks.forEach { _tasks.add(it) }

        if (pos == null) _tasks.add(item) else _tasks.add(pos, item)

        tasks = _tasks
    }

    fun removeItem(item: Task): Int? {

        for (i in 0 until tasks.size) {
            if (item.id == tasks[i].id) {
                val t = tasks
                t.remove(item)
                tasks = t

                return i
            }
        }
        return null
    }

}
