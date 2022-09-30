package com.example.lesson80taskermvc.functions

import android.content.Context
import android.os.Build
import android.widget.ImageView
import android.widget.TextView
import com.example.lesson80taskermvc.R
import com.example.lesson80taskermvc.database.AppDatabase
import com.example.lesson80taskermvc.database.categories.CategoryEntity

fun setTextColor(category: CategoryEntity, textView: TextView, context: Context) {
    if (category.categoryColor == R.color.gray || category.categoryColor == R.color.yellow) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textView.setTextColor(context.getColor(R.color.my_black))
        }
    } else {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textView.setTextColor(context.getColor(R.color.white))
        }
    }
}

fun setPenImage(category: CategoryEntity, imageView: ImageView, context: Context) {
    if (category.categoryColor == R.color.gray || category.categoryColor == R.color.yellow) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            imageView.setImageResource(R.drawable.ic_black_pen)
        }
    } else {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            imageView.setImageResource(R.drawable.ic_white_pen)
        }
    }
}

fun countTasks(categoryId: Long, context: Context): String {
    var count = 0
    val tasks = AppDatabase.getInstance(context).taskDao().getTasks()
    tasks.forEach {
        if (it.categoryId == categoryId) count++
    }
    return if (count == 0) {
        "No task"
    } else {
        if (count == 1) {
            "1 task"
        } else {
            "$count tasks"
        }
    }
}