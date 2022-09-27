package com.example.lesson80taskermvc.database.tasks

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    @ColumnInfo(name = "task_name")
    var taskName: String,
    @ColumnInfo(name = "category_id")
    var categoryId: Long,
    @ColumnInfo(name = "date")
    var date: Int,
    @ColumnInfo(name = "month")
    var month: Int,
    @ColumnInfo(name = "year")
    var year: Int,
    @ColumnInfo(name = "hour")
    var hour: Int,
    @ColumnInfo(name = "minute")
    var minute: Int
)
