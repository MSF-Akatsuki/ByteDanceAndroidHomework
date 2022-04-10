package com.bytedance.jstu.homework

import androidx.lifecycle.*
import com.bytedance.jstu.homework.TodoDatabase.TodoDB
import com.bytedance.jstu.homework.TodoDatabase.TodoDbBrief
import com.bytedance.jstu.homework.TodoDatabase.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoViewModel(private val repository: TodoRepository): ViewModel() {

    val allUndoneTodos : LiveData<List<TodoDB>> = repository.allUndoneTodos.asLiveData()
    val allDoneTodos : LiveData<List<TodoDB>> = repository.allDoneTodos.asLiveData()
    fun insert(todo: TodoDB) = viewModelScope.launch (Dispatchers.IO) {
        repository.insert(todo)
    }

    fun updateIsDoneById(id: Int,isDone: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateIsDoneById(id,isDone)
    }

    fun update(todoDb: TodoDB) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(todoDb)
    }

    fun delete(id : Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(id)
    }
}

class TodoViewModelFactory(private val repository: TodoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TodoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
