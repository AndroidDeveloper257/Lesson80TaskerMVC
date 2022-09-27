package com.example.lesson80taskermvc.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lesson80taskermvc.R
import com.example.lesson80taskermvc.database.AppDatabase
import com.example.lesson80taskermvc.database.categories.CategoryEntity
import com.example.lesson80taskermvc.databinding.MainPageCategoryItemBinding
import com.example.lesson80taskermvc.utils.Colors.GRAY
import com.example.lesson80taskermvc.utils.Colors.YELLOW

class MainPageCategoryAdapter(
    private var context: Context,
    private var categoryList: ArrayList<CategoryEntity>,
    var onCategoryClicked: (CategoryEntity) -> Unit
) : RecyclerView.Adapter<MainPageCategoryAdapter.Vh>() {

    inner class Vh(private var itemBinding: MainPageCategoryItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun onBind(category: CategoryEntity) {
            itemBinding.layout.setBackgroundColor(category.categoryColor)
            if (category.categoryColor == GRAY || category.categoryColor == YELLOW) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    itemBinding.categoryNameTv.setTextColor(context.getColor(R.color.my_black))
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    itemBinding.categoryNameTv.setTextColor(context.getColor(R.color.white))
                }
            }
            itemBinding.categoryNameTv.text = category.categoryName
            val countTasks = countTasks(category.id)
            val text = if (countTasks == 0) {
                "No task"
            } else {
                if (countTasks == 1) {
                    "1 task"
                } else {
                    "$countTasks tasks"
                }
            }
            itemBinding.taskCountTv.text = text
            itemBinding.root.setOnClickListener {
                onCategoryClicked.invoke(category)
            }
        }
    }

    private fun countTasks(categoryId: Long): Int {
        var count = 0
        val tasks = AppDatabase.getInstance(context).taskDao().getTasks()
        tasks.forEach {
            if (it.categoryId == categoryId) count++
        }
        return count
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(
            MainPageCategoryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(categoryList[position])
    }

    override fun getItemCount(): Int = categoryList.size
}