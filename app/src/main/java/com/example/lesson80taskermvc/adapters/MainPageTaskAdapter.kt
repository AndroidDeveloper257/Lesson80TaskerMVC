package com.example.lesson80taskermvc.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lesson80taskermvc.R
import com.example.lesson80taskermvc.database.AppDatabase
import com.example.lesson80taskermvc.database.tasks.Task
import com.example.lesson80taskermvc.databinding.MainPageTaskItemBinding

class MainPageTaskAdapter(
    var context: Context,
    private val taskList: ArrayList<Task>
) :
    RecyclerView.Adapter<MainPageTaskAdapter.Vh>() {

    private var selectionList: ArrayList<Boolean> =
        List(taskList.size) { false } as ArrayList<Boolean>

    inner class Vh(var itembinding: MainPageTaskItemBinding) :
        RecyclerView.ViewHolder(itembinding.root) {
        fun onBind(task: Task, position: Int) {
            val selection = selectionList[position]
            if (selection) {
                itembinding.selector.setImageResource(R.drawable.ic_main_page_selected)
            } else {
                itembinding.selector.setImageResource(R.drawable.ic_main_page_unselected)
            }
            itembinding.taskName.text = task.taskName
            itembinding.categoryIndicator.setBackgroundColor(getColor(task.categoryId))
            itembinding.selector.setOnClickListener {

                if (selection) {
                    itembinding.selector.setImageResource(R.drawable.ic_main_page_unselected)
                    selectionList[position] = false
                } else {
                    itembinding.selector.setImageResource(R.drawable.ic_main_page_selected)
                    selectionList[position] = true
                }
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