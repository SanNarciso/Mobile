package com.example.todoapp.views.tasks


import android.content.Context
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.R
import com.example.todoapp.base.navigator.Navigator
import com.example.todoapp.databinding.CreateTaskBottomSheetBinding

import com.example.todoapp.databinding.FragmentTasksBinding
import com.example.todoapp.model.task.Task
import com.example.todoapp.views.category.CategoryViewPager2Adapter
import com.example.todoapp.views.category.adapters
import com.example.todoapp.views.current.CurrentTaskFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator

val categories = arrayOf(
    "Избранные",
    "Мои задачи",
    "Выполненные"
)

class TasksFragment : Fragment(), TasksListener  {

    private lateinit var binding: FragmentTasksBinding
    private val viewModel: TasksViewModel by activityViewModels()
    private var navigator: Navigator? = null
    private lateinit var adapter: CategoryViewPager2Adapter
    private lateinit var newTaskDialog: BottomSheetDialog
    private var deletingMode = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigator = context as Navigator
    }

    override fun onDetach() {
        super.onDetach()
        navigator = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            viewModel.initState()
        } else {
            @Suppress("DEPRECATION")
            val state = savedInstanceState.getParcelableArrayList<Task>(KEY_STATE) as MutableList<Task>
            viewModel.initState(state)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentTasksBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFragmentResultListener("111") { key, bundle ->
            deletingMode = true

            @Suppress("DEPRECATION")
            val task = bundle.getParcelable<Task>(EVENT_ARG_TASK) as Task
            val position = bundle.getInt(EVENT_ARG_POSITION)
            val snackbar = Snackbar.make(view, "Задача удалена", Snackbar.LENGTH_LONG)

            val deletingItemPos: Int? = adapters[position].removeItem(task)
            Log.d("delete", "pos $deletingItemPos")

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
                        adapters[position].addItem(task, deletingItemPos)
                    }
                    deletingMode = false
                }
            })
            snackbar.show()
        }

        adapter = CategoryViewPager2Adapter(this)
        binding.categoryViewPager2.adapter = adapter

        TabLayoutMediator(binding.categoryTabLayout, binding.categoryViewPager2) {tab, position ->

            if (position == 0) {
                tab.setIcon(R.drawable.ic_star)
                val tabIconColor = ContextCompat.getColor(requireContext(), R.color.blue)
                tab.icon?.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN)
            } else {
                tab.text = categories[position]
            }
        }.attach()

        newTaskDialog = BottomSheetDialog(requireContext(), R.style.DialogStyle)
        newTaskDialog.window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

        createTaskDialog()

        binding.createItemFab.setOnClickListener {
            newTaskDialog.show()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(
            KEY_STATE,
            viewModel.tasks.value as ArrayList<Task>
        )
    }

    private fun createTaskDialog() {

        val dialogBinding = CreateTaskBottomSheetBinding.inflate(LayoutInflater.from(requireContext()), null, false)

        dialogBinding.saveTaskButton.isEnabled = false

        dialogBinding.additionalInfoImageBtn.setOnClickListener {
            dialogBinding.additionalInfoEditText.visibility = View.VISIBLE
        }
        dialogBinding.addInFavoriteCheckBox.setOnCheckedChangeListener { _, isChecked ->
            dialogBinding.addInFavoriteCheckBox.setButtonDrawable(
                if (isChecked) R.drawable.ic_star else R.drawable.ic_star_border
            )
        }

        dialogBinding.taskTitleEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                dialogBinding.saveTaskButton.isEnabled = s?.isNotBlank() == true
            }
        })

        dialogBinding.saveTaskButton.setOnClickListener {
            val task = Task(
                isCompleted = false,
                text = dialogBinding.taskTitleEditText.text.toString(),
                additionalInfo = dialogBinding.additionalInfoEditText.text.toString(),
                isFavorite = dialogBinding.addInFavoriteCheckBox.isChecked
            )
            viewModel.createTask(task)
            newTaskDialog.dismiss()
        }
        dialogBinding.taskTitleEditText.requestFocus()

        newTaskDialog.setOnDismissListener {
            dialogBinding.apply {
                taskTitleEditText.text.clear()
                additionalInfoEditText.text.clear()
                additionalInfoEditText.visibility = View.GONE
                addInFavoriteCheckBox.isChecked = false
            }
        }

        newTaskDialog.setContentView(dialogBinding.root)

    }

    override fun onClickTask(task: Task) {
        viewModel.updateTask(task)
    }

    override fun showTaskScreen(task: Task, adapterPosition: Int) {
        navigator?.launch(CurrentTaskFragment.newInstance(task, adapterPosition))
    }

    override fun onMoveTask(from: Int, to: Int) {

    }

    override fun observeData(lifecycleOwner: LifecycleOwner, adapter: TasksAdapter, position: Int) {
        when (position) {
            0 -> viewModel.tasks.observe(lifecycleOwner) { if(!deletingMode) adapter.tasks = it.toMutableList()}
            1 -> viewModel.tasks.observe(lifecycleOwner) { if(!deletingMode) adapter.tasks = it.toMutableList()}
            2 -> viewModel.tasks.observe(lifecycleOwner) { if(!deletingMode) adapter.tasks = it.toMutableList()}
        }
    }

    companion object {

        const val EVENT_ARG_TASK = "event_arg_task"
        const val EVENT_ARG_POSITION = "event_arg_position"
        private const val KEY_STATE = "com.example.todoapp.views.tasks.key_state"

        fun newInstance(): TasksFragment = TasksFragment()

    }

}