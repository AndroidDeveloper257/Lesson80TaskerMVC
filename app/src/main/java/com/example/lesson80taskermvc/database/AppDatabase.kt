package com.example.lesson80taskermvc.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.lesson80taskermvc.database.categories.CategoryDao
import com.example.lesson80taskermvc.database.categories.CategoryEntity
import com.example.lesson80taskermvc.database.tasks.Task
import com.example.lesson80taskermvc.database.tasks.TaskDao

@Database(entities = [Task::class, CategoryEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        private var INSTANCE : AppDatabase? = null

        fun getInstance(context: Context) : AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context, AppDatabase::class.java, "tasker_database")
                    .allowMainThreadQueries()
                    .build()
            }
            return INSTANCE!!
        }
    }
}