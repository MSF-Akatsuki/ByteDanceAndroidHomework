package com.bytedance.jstu.homework

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bytedance.jstu.homework.TodoDatabase.TodoDbBrief
import com.bytedance.jstu.homework.databinding.FragmentFirstBinding
import com.bytedance.jstu.homework.databinding.FragmentSecondBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class SecondFragment : Fragment() {

    protected var _binding: FragmentFirstBinding? = null
    protected var adapter: TodoListAdapter = TodoListAdapter()
    protected open val todoViewModel: TodoViewModel by activityViewModels()

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

        todoViewModel.allDoneTodos.observe(viewLifecycleOwner) { todos ->
            todos.let { adapter?.submitList(it) }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}