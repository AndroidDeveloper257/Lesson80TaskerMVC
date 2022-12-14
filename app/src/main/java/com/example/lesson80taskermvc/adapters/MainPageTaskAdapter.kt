package com.example.lesson80taskermvc.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.lesson80taskermvc.R
import com.example.lesson80taskermvc.database.AppDatabase
import com.example.lesson80taskermvc.database.tasks.Task
import com.example.lesson80taskermvc.databinding.MainPageTaskItemBinding
import java.lang.Exception

class MainPageTaskAdapter(
    var context: Context,
    private val taskList: ArrayList<Task>
) :
    RecyclerView.Adapter<MainPageTaskAdapter.Vh>() {

    inner class Vh(var itembinding: MainPageTaskItemBinding) :
        RecyclerView.ViewHolder(itembinding.root) {
        @SuppressLint("UseCompatLoadingForColorStateLists")
        fun onBind(task: Task, position: Int) {
            itembinding.taskName.text = task.taskName
            try {
                itembinding.categoryIndicator.backgroundTintList =
                    context.resources.getColorStateList(getColor(task.categoryId))
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getColor(categoryId: Long): Int {
        val categoryList = AppDatabase.getInstance(context).categoryDao().getCategories()
        categoryList.forEach { category ->
            if (category.id == categoryId) return category.categoryColor
        }
        return categoryList.elementAt(1).categoryColor
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(
            MainPageTaskItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(taskList[position], position)
    }

    override fun getItemCount(): Int = taskList.size
}