package com.example.lesson80taskermvc.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lesson80taskermvc.R
import com.example.lesson80taskermvc.database.AppDatabase
import com.example.lesson80taskermvc.database.categories.CategoryEntity
import com.example.lesson80taskermvc.databinding.MainPageCategoryItemBinding

class BottomCategoryAdapter(
    var context: Context,
    var categoryList: ArrayList<CategoryEntity>,
    var selectionList: ArrayList<Boolean>,
    var onItemSelected: (CategoryEntity) -> Unit
) : RecyclerView.Adapter<BottomCategoryAdapter.Vh>() {

    private var lastSelectedIndex = -1
    private lateinit var myParent: ViewGroup

    inner class Vh(var itemBinding: MainPageCategoryItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        @SuppressLint("NotifyDataSetChanged")
        fun onBind(category: CategoryEntity, position: Int) {
            if (selectionList[position]) {
                itemBinding.selector.visibility = View.VISIBLE
            } else {
                itemBinding.selector.visibility = View.GONE
            }
            itemBinding.layout.setBackgroundResource(category.categoryColor)
            if (category.categoryColor == R.color.gray || category.categoryColor == R.color.yellow) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    itemBinding.categoryNameTv.setTextColor(context.getColor(R.color.my_black))
                    itemBinding.taskCountTv.setTextColor(context.getColor(R.color.my_black))
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    itemBinding.categoryNameTv.setTextColor(context.getColor(R.color.white))
                    itemBinding.taskCountTv.setTextColor(context.getColor(R.color.white))
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
                lastSelectedIndex = position
                onItemSelected.invoke(category)
                notifyDataSetChanged()
            }
            if (lastSelectedIndex == position) {
                itemBinding.selector.visibility = View.VISIBLE
            } else {
                itemBinding.selector.visibility = View.GONE
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
        myParent = parent
        return Vh(
            MainPageCategoryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: Vh, @SuppressLint("RecyclerView") position: Int) {
        holder.onBind(categoryList[position], position)
    }

    override fun getItemCount(): Int = categoryList.size
}