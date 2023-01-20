package com.example.todoapp.views.tasks


import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.R
import com.example.todoapp.base.navigator.Navigator
import com.example.todoapp.databinding.CreateTaskBottomSheetBinding

import com.example.todoapp.databinding.FragmentTasksBinding
import com.example.todoapp.model.task.Task
import com.example.todoapp.views.current.CurrentTaskFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar

class TasksFragment : Fragment(), TasksListener  {

    private lateinit var binding: FragmentTasksBinding
    private val viewModel: TasksViewModel by activityViewModels()
    private var navigator: Navigator? = null
    private val adapter: TasksAdapter = TasksAdapter(this)
    private lateinit var newTaskDialog: BottomSheetDialog

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

        setFragmentResultListener(EVENT_DELETE_TASK) { key, bundle ->
            @Suppress("DEPRECATION")
            val task = bundle.getParcelable<Task>(KEY_REMOVED_TASK) as Task
            val snackbar = Snackbar.make( view, "Задача удалена", Snackbar.LENGTH_LONG)
            snackbar.setAction("Отмена") {
                Toast.makeText(requireContext(), "Отменено", Toast.LENGTH_SHORT).show()
            }
            snackbar.show()
        }


        newTaskDialog = BottomSheetDialog(requireContext(), R.style.DialogStyle)
        newTaskDialog.window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

        initRecyclerView()
        createTaskDialog()

        binding.createItemFab.setOnClickListener {
//            viewModel.createTask()
            newTaskDialog.show()
        }

        viewModel.tasks.observe(viewLifecycleOwner) {
            adapter.tasks = it.toMutableList()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(
            KEY_STATE,
            viewModel.tasks.value as ArrayList<Task>
        )
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
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

    override fun showTaskScreen(task: Task) {
        navigator?.launch(CurrentTaskFragment.newInstance(task))
    }

    override fun onMoveTask(from: Int, to: Int) {
        viewModel.onMoveTask(from, to)
    }

    companion object {

        const val EVENT_DELETE_TASK = "com.example.todoapp.views.tasks.delete_task"
        const val KEY_REMOVED_TASK = "com.example.todoapp.views.tasks.delete_task"
        private const val KEY_STATE = "com.example.todoapp.views.tasks.key_state"

        fun newInstance(): TasksFragment = TasksFragment()

    }

}