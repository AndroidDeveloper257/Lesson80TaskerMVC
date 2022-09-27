package com.example.lesson80taskermvc.database.tasks

import androidx.room.*

@Dao
interface TaskDao {
    @Insert
    fun addTask(task: Task)

    @Update
    fun updateTask(task: Task)

    @Delete
    fun deleteTask(task: Task)

    @Query("SELECT * FROM tasks_table")
    fun getTasks(): List<Task>

    @Query("SELECT * FROM tasks_table WHERE id = :id")
    fun getTaskById(id: Long) : Task
}