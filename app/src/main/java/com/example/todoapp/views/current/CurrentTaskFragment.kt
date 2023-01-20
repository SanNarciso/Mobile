package com.example.todoapp.views.current

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import com.example.todoapp.R
import com.example.todoapp.base.navigator.Navigator
import com.example.todoapp.databinding.FragmentCurrentTaskBinding
import com.example.todoapp.model.task.Task
import com.example.todoapp.views.tasks.TasksFragment


class CurrentTaskFragment : Fragment() {

    private lateinit var binding: FragmentCurrentTaskBinding
    private val viewModel: CurrentTaskViewModel by activityViewModels()
    private var navigator: Navigator? = null

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
        binding = FragmentCurrentTaskBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val task = requireArguments().getParcelable<Task>(ARGS_TASK) as Task
        if (savedInstanceState == null) viewModel.initState(task)

        viewModel.task.observe(viewLifecycleOwner) {
            renderState(it)
        }

        binding.goBackImageButton.setOnClickListener {
            navigator?.goBack()
        }

        binding.favoriteImageButton.setOnClickListener {
            viewModel.updateTask(
                isFavorite = !viewModel.task.value!!.isFavorite,
                text = binding.taskTitleEditText.text.toString(),
                additionalInfo = binding.additInfoEditText.text.toString()
            )
        }

        binding.addInCompletedButton.setOnClickListener {
            viewModel.updateTask(
                isCompleted = !viewModel.task.value!!.isCompleted
            )
            if (viewModel.task.value?.isCompleted!!) navigator?.goBack()
        }

        binding.deleteImageButton.setOnClickListener {
            setFragmentResult("111", bundleOf(
                TasksFragment.EVENT_ARG_TASK to viewModel.task.value,
                TasksFragment.EVENT_ARG_POSITION to requireArguments().getInt(ARGS_POS)
            ))
            navigator?.goBack()
        }

    }

    override fun onStop() {
        super.onStop()

        viewModel.updateTask(
            text = binding.taskTitleEditText.text.toString(),
            additionalInfo = binding.additInfoEditText.text.toString()
        )
    }

    private fun renderState(task: Task) = binding.run {
        taskTitleEditText.setText(task.text)
        additInfoEditText.setText(task.additionalInfo)
        favoriteImageButton.setImageResource(
            if (task.isFavorite) R.drawable.ic_star else R.drawable.ic_star_border
        )
        addInCompletedButton.setText(
            if (task.isCompleted) R.string.uncompleted else R.string.completed
        )
    }

    companion object {

        private const val ARGS_TASK = "om.example.todoapp.views.current.args_task"
        private const val ARGS_POS = "om.example.todoapp.views.current.args_pos"

        fun newInstance(task: Task, adapterPosition: Int): CurrentTaskFragment {
            val args = Bundle().apply {
                putParcelable(ARGS_TASK, task)
                putInt(ARGS_POS, adapterPosition)
            }

            val fragment = CurrentTaskFragment()
            fragment.arguments = args

            return fragment
        }

    }

}
