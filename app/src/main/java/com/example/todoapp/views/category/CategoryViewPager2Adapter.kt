package com.example.todoapp.views.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.todoapp.databinding.FragmentCategoryItemBinding
import com.example.todoapp.views.tasks.TasksAdapter
import com.example.todoapp.views.tasks.TasksListener
import kotlin.properties.Delegates

private const val PAGE_COUNT = 3
val adapters = mutableListOf<TasksAdapter>()

class CategoryViewPager2Adapter(private val fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = PAGE_COUNT

    override fun createFragment(position: Int): Fragment = CategoryItemFragment.newInstance(fragment as TasksListener, position)

}

class CategoryItemFragment : Fragment() {

    private lateinit var binding: FragmentCategoryItemBinding
    private lateinit var adapter: TasksAdapter
    private lateinit var listener: TasksListener
    private var position by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listener = requireArguments().getSerializable(ARG_LISTENER) as TasksListener
        position = requireArguments().getInt(ARG_POSITION)
        adapter = TasksAdapter(listener, position)
        adapters.add(adapter)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryItemBinding.inflate(inflater, container, false)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        listener.observeData(viewLifecycleOwner, adapter, position)

        return binding.root
    }

    companion object {

        private const val ARG_LISTENER = "arg_listener"
        private const val ARG_POSITION = "arg_position"

        fun newInstance(listener: TasksListener, position: Int): CategoryItemFragment {
            val args = bundleOf(
                ARG_LISTENER to listener,
                ARG_POSITION to position
            )
            return CategoryItemFragment().apply {
                arguments = args
            }
        }
    }

}
