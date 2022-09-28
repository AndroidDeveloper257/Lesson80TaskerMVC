package com.example.lesson80taskermvc.fragments

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.view.menu.MenuBuilder
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.lesson80taskermvc.R
import com.example.lesson80taskermvc.adapters.MainPageCategoryAdapter
import com.example.lesson80taskermvc.adapters.MainPageTaskAdapter
import com.example.lesson80taskermvc.database.AppDatabase
import com.example.lesson80taskermvc.database.categories.CategoryDao
import com.example.lesson80taskermvc.database.categories.CategoryEntity
import com.example.lesson80taskermvc.database.tasks.Task
import com.example.lesson80taskermvc.database.tasks.TaskDao
import com.example.lesson80taskermvc.databinding.FragmentHomeBinding

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


        binding.addBtn.setOnClickListener {
            showPopup()
        }

        return binding.root
    }

    private fun showPopup() {
        val popup = PopupMenu(requireContext(), binding.addBtn)
        popup.menuInflater.inflate(R.menu.popup_menu, popup.getMenu())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popup.setForceShowIcon(true)
        }
        popup.setOnMenuItemClickListener { item ->
            val itemId = item.itemId
            if (itemId == R.id.add_task) {
                addTask()
            }
            if (itemId == R.id.add_category) {
                addCategory()
            }
            true
        }
        popup.show() //showing popup menu

    }

    private fun addCategory() {
        Toast.makeText(
            requireContext(),
            "Adding category feature is coming soon...",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun addTask() {
        val navOptions: NavOptions = NavOptions.Builder()
            .setEnterAnim(R.anim.enter)
            .setExitAnim(R.anim.exit)
            .setPopEnterAnim(R.anim.pop_enter)
            .setPopExitAnim(R.anim.pop_exit)
            .build()
        findNavController().navigate(R.id.addTaskerFragment, null, navOptions)
    }

    @SuppressLint("RestrictedApi")
    fun Menu.showIcons() {
        (this as? MenuBuilder)?.setOptionalIconsVisible(true)
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
            categoryList.add(CategoryEntity(0, "Inbox", R.color.gray))
            categoryList.add(CategoryEntity(0, "Work", R.color.green))
            categoryList.add(CategoryEntity(0, "Shopping", R.color.red))
            categoryList.add(CategoryEntity(0, "Family", R.color.yellow))
            categoryList.add(CategoryEntity(0, "Personal", R.color.purple))
            categoryList.forEach {
                categoryDao.addCategory(it)
            }
        }
    }

    companion object {
        private const val TAG = "HomeFragment"
    }
}