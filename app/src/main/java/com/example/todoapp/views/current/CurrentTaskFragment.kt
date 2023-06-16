package com.example.todoapp.views.current

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentCurrentTaskBinding
import com.example.todoapp.model.task.Task


class CurrentTaskFragment : Fragment() {

    private lateinit var binding: FragmentCurrentTaskBinding
    private val viewModel: CurrentTaskViewModel by activityViewModels()

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

        val task = requireArguments().getParcelable<Task>(ARGS_KEY) as Task
        if (savedInstanceState == null) viewModel.initState(task)

        viewModel.task.observe(viewLifecycleOwner) {
            renderState(it)
        }

        binding.favoriteImageButton.setOnClickListener {
            viewModel.updateTask(
                task = viewModel.task.value!!.copy(
                    isFavorite = !viewModel.task.value!!.isFavorite
                )
            )
        }

        binding.addInCompletedButton.setOnClickListener {
            viewModel.updateTask(
                task = viewModel.task.value!!.copy(
                    isCompleted = !viewModel.task.value!!.isCompleted
                )
            )
        }

        binding.deleteImageButton.setOnClickListener {
            viewModel.deleteTask()
        }

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

        private const val ARGS_KEY = "om.example.todoapp.views.current.args_key"

        fun newInstance(task: Task): CurrentTaskFragment {
            val args = Bundle().apply {
                putParcelable(ARGS_KEY, task)
            }

            val fragment = CurrentTaskFragment()
            fragment.arguments = args

            return fragment
        }

    }

}