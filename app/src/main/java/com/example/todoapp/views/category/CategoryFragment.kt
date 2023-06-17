package com.example.todoapp.views.category

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.base.navigator.Navigator
import com.example.todoapp.databinding.FragmentCategoryItemBinding
import com.example.todoapp.model.task.Task
import com.example.todoapp.views.current.CurrentTaskFragment
import com.example.todoapp.views.tasks.TasksAdapter
import com.example.todoapp.views.tasks.TasksListener
import com.example.todoapp.views.tasks.itemTouchHelper
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

class CategoryFragment : Fragment(), TasksListener {

    private lateinit var binding: FragmentCategoryItemBinding
    private val adapter: TasksAdapter = TasksAdapter(this)
    private val viewModel: CategoryViewModel by activityViewModels()
    private var navigator: Navigator? = null
    private var deletingMode = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigator = context as Navigator
    }

    override fun onDetach() {
        super.onDetach()
        navigator = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryItemBinding.inflate(inflater, container, false)

        initRecyclerView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentResultListener(EVENT_DELETE_TASK) { key, bundle ->
            deletingMode = true

            @Suppress("DEPRECATION")
            val task = bundle.getParcelable<Task>(KEY_REMOVED_TASK) as Task
            val snackbar = Snackbar.make(view, "Задача удалена", Snackbar.LENGTH_LONG)

            val deletingItemPos: Int? = adapter.removeItem(task)

            var flagDeleteTask = true

            snackbar.setAction("Отмена") {
                flagDeleteTask = false
            }
            snackbar.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                override fun onShown(transientBottomBar: Snackbar?) {
                    super.onShown(transientBottomBar)
                }

                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    if (flagDeleteTask) viewModel.removeTask(task)
                    else {
                        adapter.addItem(task, deletingItemPos)
                    }
                    deletingMode = false
                }
            })
            snackbar.show()
        }

        viewModel.tasks.observe(viewLifecycleOwner) {
            adapter.tasks = it.toMutableList()
        }
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    override fun onClickTask(task: Task) {
        viewModel.updateTask(task)
    }

    override fun showTaskScreen(task: Task) {
        navigator?.launch(CurrentTaskFragment.newInstance(task))
    }

    override fun onMoveTask(from: Int, to: Int) {
        viewModel.onMoveTask(from, to)
    }

    companion object {

        const val EVENT_DELETE_TASK = "com.example.todoapp.views.tasks.delete_task"
        const val KEY_REMOVED_TASK = "com.example.todoapp.views.tasks.delete_task"


    }
}