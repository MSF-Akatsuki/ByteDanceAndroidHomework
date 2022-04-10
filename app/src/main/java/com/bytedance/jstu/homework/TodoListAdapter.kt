package com.bytedance.jstu.homework

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.findFragment
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bytedance.jstu.homework.TodoDatabase.TodoDB
import com.bytedance.jstu.homework.TodoDatabase.TodoDbBrief
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.*

class TodoListAdapter : ListAdapter<TodoDB, TodoListAdapter.TodoViewHolder>(TodoListAdapter.TodosComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        Log.println(Log.VERBOSE,"nothing","on_create_view_holder")
        return TodoViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val current: TodoDB = getItem(position)
        holder.bind(current)
    }

    class TodoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val todoItemTitle = itemView.findViewById<TextView>(R.id.todo_item_title)
        private val todoItemDate = itemView.findViewById<TextView>(R.id.todo_item_date)
        var todoDb :TodoDB? = null
        fun bind(todo : TodoDB) {
            todoItemTitle.text=todo.title
            todoItemDate.text=DateFormat.getDateInstance().format(Date(todo.dueTime))
            todoDb = todo
        }

        fun popUpEditFragment() {
            AddTodoDialogFragment(AddTodoDialogFragment.STATE_VIEW, todoDb).show(
                (itemView.context as MainActivity).supportFragmentManager,AddTodoDialogFragment.TAG
            )
        }

        fun reverseIsDone()= CoroutineScope(Dispatchers.IO).launch {
            todoDb?.let { it1->
                ((itemView.context as MainActivity).application as TodoApplication).repository.updateIsDoneById(it1.id,!it1.isDone)
            }

        }

        companion object {
            fun create(parent: ViewGroup): TodoViewHolder {
                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.todo_item_view,parent,false)
                val todoViewHolder = TodoViewHolder(view)
                view.findViewById<ImageView>(R.id.todo_radio_button).setOnClickListener {
                    todoViewHolder.reverseIsDone()
                }
                view.findViewById<FrameLayout>(R.id.fl_todo_item).setOnLongClickListener {
                    todoViewHolder.popUpEditFragment()
                    true
                }
                return todoViewHolder
            }
        }
    }

    class TodosComparator : DiffUtil.ItemCallback<TodoDB>() {
        override fun areItemsTheSame(oldItem: TodoDB, newItem: TodoDB): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: TodoDB, newItem: TodoDB): Boolean {
            return oldItem.hashCode()==newItem.hashCode()
        }
    }
}
