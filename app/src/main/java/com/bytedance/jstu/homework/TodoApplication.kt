package com.bytedance.jstu.homework

import android.app.Application
import com.bytedance.jstu.homework.TodoDatabase.TodoRepository
import com.bytedance.jstu.homework.TodoDatabase.TodoRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class TodoApplication : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { TodoRoomDatabase.getDatabase(this,applicationScope) }
    val repository by lazy { TodoRepository(database.todoDao()) }

}