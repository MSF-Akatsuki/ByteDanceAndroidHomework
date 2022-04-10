package com.bytedance.jstu.homework.TodoDatabase

import android.provider.UserDictionary
import android.util.Log
import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.forEach

class TodoRepository(private val todoDao: TodoDao) {

    val allUndoneTodos: Flow<List<TodoDB>> = todoDao.getUndoneItemBrief()
    val allDoneTodos: Flow<List<TodoDB>> = todoDao.getDoneItemBrief()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(todoDb: TodoDB) {
        todoDao.insertAll(todoDb)
        Log.println(Log.VERBOSE, "nothing", "insert done")
    }

    @WorkerThread
    suspend fun updateIsDoneById(id: Int,isDone: Boolean) {
        todoDao.updateIsDone(TodoDbIsDone(id,isDone))
    }

    @WorkerThread
    suspend fun update(todoDb: TodoDB){
        todoDao.update(todoDb)
        Log.println(Log.VERBOSE, "nothing", "update done")
    }

    @WorkerThread
    suspend fun delete(id: Int) {
        val ret = todoDao.deleteByRowId(TodoDB(id,"","",0,false))
        Log.println(Log.VERBOSE,"sql_delete","Successfully delete $ret values.")
    }

}