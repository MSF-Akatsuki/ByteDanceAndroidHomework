package com.bytedance.jstu.homework

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bytedance.jstu.homework.TodoDatabase.TodoDbBrief
import com.bytedance.jstu.homework.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private var adapter: TodoListAdapter = TodoListAdapter()
    private val todoViewModel: TodoViewModel by activityViewModels{
        TodoViewModelFactory(( activity?.application as TodoApplication).repository)
    }
    // This property is only valid between onCreateView and
    // onDestroyView.
    protected val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerviewTodoUndone.adapter = adapter
        binding.recyclerviewTodoUndone.layoutManager = LinearLayoutManager(context)

        todoViewModel.allUndoneTodos.observe(viewLifecycleOwner) { todos ->
            todos.let { adapter?.submitList(it) }
        }
        //binding.recyclerviewTodoUndone.recycledViewPool.

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        // adapter = null
    }

}