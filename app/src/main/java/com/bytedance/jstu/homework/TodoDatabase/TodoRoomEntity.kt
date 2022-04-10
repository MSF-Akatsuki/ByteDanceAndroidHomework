package com.bytedance.jstu.homework.TodoDatabase

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.util.*

@Entity(tableName = "TodoDB")
data class TodoDB(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val detail: String,
    @ColumnInfo(name = "due_time") val dueTime: Long,
    @ColumnInfo(name = "is_done") val isDone : Boolean
)

data class DbSize(@ColumnInfo(name="db_size") val dbSize: Int)
data class TodoDbBrief(
    val id: Int,
    val title: String,
    @ColumnInfo(name = "due_time") val dueTime: Long,
    @ColumnInfo(name = "is_done") val isDone : Boolean
)
data class TodoDbIsDone(
    val id: Int,
    @ColumnInfo(name = "is_done") val isDone : Boolean
)

@Dao
interface TodoDao {
    @Insert
    fun insertAll(vararg todoDB: TodoDB)

    @Delete
    fun deleteByRowId(vararg todoDB: TodoDB):Int

    @Update
    fun update(vararg todoDB: TodoDB)

    @Update(entity = TodoDB::class)
    fun updateIsDone(vararg todoDbIsDone: TodoDbIsDone)

    @Query("SELECT * FROM TodoDB")
    fun getAll(): Flow<List<TodoDB>>

    @Query("SELECT * FROM TodoDB WHERE id = :id")
    fun getItemById(id: Int): List<TodoDB>

    @Query("SELECT COUNT(*) as db_size FROM TodoDB")
    fun getDbSize(): List<DbSize>

    @Query("SELECT * FROM TodoDB WHERE is_done is 0")
    fun getUndoneItemBrief() : Flow<List<TodoDB>>

    @Query("SELECT * FROM TodoDB WHERE not(is_done is 0)")
    fun getDoneItemBrief() : Flow<List<TodoDB>>

}