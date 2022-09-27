package com.example.lesson80taskermvc.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.lesson80taskermvc.R
import com.example.lesson80taskermvc.adapters.MainPageCategoryAdapter
import com.example.lesson80taskermvc.adapters.MainPageTaskAdapter
import com.example.lesson80taskermvc.database.AppDatabase
import com.example.lesson80taskermvc.database.categories.CategoryDao
import com.example.lesson80taskermvc.database.categories.CategoryEntity
import com.example.lesson80taskermvc.database.tasks.Task
import com.example.lesson80taskermvc.database.tasks.TaskDao
import com.example.lesson80taskermvc.databinding.FragmentHomeBinding
import com.example.lesson80taskermvc.utils.Colors
import com.example.lesson80taskermvc.utils.Colors.GRAY
import com.example.lesson80taskermvc.utils.Colors.GREEN
import com.example.lesson80taskermvc.utils.Colors.PURPLE
import com.example.lesson80taskermvc.utils.Colors.RED
import com.example.lesson80taskermvc.utils.Colors.YELLOW

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var database: AppDatabase
    private lateinit var categoryDao: CategoryDao
    private lateinit var taskDao: TaskDao
    private lateinit var taskList: ArrayList<Task>
    private lateinit var categoryList: ArrayList<CategoryEntity>
    private lateinit var taskAdapter: MainPageTaskAdapter
    private lateinit var categoryAdapter: MainPageCategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        database = AppDatabase.getInstance(requireContext())

        loadTasks()
        loadCategories()

        taskAdapter = MainPageTaskAdapter(
            requireContext(),
            taskList
        )
        binding.taskRv.adapter = taskAdapter
        if (taskList.isEmpty()) binding.emptyTv.visibility = View.VISIBLE
        else binding.emptyTv.visibility = View.GONE

        categoryAdapter = MainPageCategoryAdapter(
            requireContext(),
            categoryList
        ) {
            showBottomSheetDialog(it)
        }
        binding.categoryRv.adapter = categoryAdapter

        return binding.root
    }

    private fun showBottomSheetDialog(category: CategoryEntity) {
        // TODO: BottomSheetDialog chiqishi kerak
        Toast.makeText(requireContext(), category.categoryName, Toast.LENGTH_SHORT).show()

    }

    private fun loadTasks() {
        taskDao = database.taskDao()
        taskList = ArrayList(taskDao.getTasks())
    }

    private fun loadCategories() {
        categoryDao = database.categoryDao()
        categoryList = ArrayList(categoryDao.getCategories())
        if (categoryList.isEmpty()) {
            categoryList.add(CategoryEntity(0, "Inbox", GRAY))
            categoryList.add(CategoryEntity(0, "Work", GREEN))
            categoryList.add(CategoryEntity(0, "Shopping", RED))
            categoryList.add(CategoryEntity(0, "Family", YELLOW))
            categoryList.add(CategoryEntity(0, "Personal", PURPLE))
            categoryList.forEach {
                categoryDao.addCategory(it)
            }
        }
    }

    companion object {
        private const val TAG = "HomeFragment"
    }
}