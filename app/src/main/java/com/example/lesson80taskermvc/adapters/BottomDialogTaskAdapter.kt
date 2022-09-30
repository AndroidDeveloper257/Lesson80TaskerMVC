package com.example.lesson80taskermvc.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lesson80taskermvc.R
import com.example.lesson80taskermvc.database.AppDatabase
import com.example.lesson80taskermvc.database.categories.CategoryEntity
import com.example.lesson80taskermvc.database.tasks.Task
import com.example.lesson80taskermvc.databinding.BottomTaskItemBinding
import com.example.lesson80taskermvc.databinding.MainPageTaskItemBinding
import com.example.lesson80taskermvc.functions.setTextColor

class BottomDialogTaskAdapter(
    var category: CategoryEntity,
    var context: Context,
    private val taskList: ArrayList<Task>
) :
    RecyclerView.Adapter<BottomDialogTaskAdapter.Vh>() {

    inner class Vh(var itembinding: BottomTaskItemBinding) :
        RecyclerView.ViewHolder(itembinding.root) {
        @SuppressLint("UseCompatLoadingForColorStateLists")
        fun onBind(task: Task, position: Int) {
            setTextColor(category, itembinding.taskName, context)
            itembinding.taskName.text = task.taskName
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
            BottomTaskItemBinding.inflate(
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