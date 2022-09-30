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
import com.example.lesson80taskermvc.functions.countTasks

class MainPageCategoryAdapter(
    private var context: Context,
    private var categoryList: ArrayList<CategoryEntity>,
    var onCategoryClicked: (CategoryEntity) -> Unit
) : RecyclerView.Adapter<MainPageCategoryAdapter.Vh>() {

    inner class Vh(private var itemBinding: MainPageCategoryItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun onBind(category: CategoryEntity) {
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
            itemBinding.taskCountTv.text = countTasks(category.id, context)
            itemBinding.root.setOnClickListener {
                onCategoryClicked.invoke(category)
            }
        }
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