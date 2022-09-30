package com.example.lesson80taskermvc.fragments

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import com.example.lesson80taskermvc.adapters.BottomDialogTaskAdapter
import com.example.lesson80taskermvc.adapters.MainPageCategoryAdapter
import com.example.lesson80taskermvc.adapters.MainPageTaskAdapter
import com.example.lesson80taskermvc.database.AppDatabase
import com.example.lesson80taskermvc.database.categories.CategoryDao
import com.example.lesson80taskermvc.database.categories.CategoryEntity
import com.example.lesson80taskermvc.database.tasks.Task
import com.example.lesson80taskermvc.database.tasks.TaskDao
import com.example.lesson80taskermvc.databinding.FragmentHomeBinding
import com.example.lesson80taskermvc.databinding.LayoutBottomSheetBinding
import com.example.lesson80taskermvc.functions.countTasks
import com.example.lesson80taskermvc.functions.setPenImage
import com.example.lesson80taskermvc.functions.setTextColor
import com.example.lesson80taskermvc.receiver.AlarmReceiver
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.reactivex.rxjava3.internal.functions.Functions

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

    private fun showBottomSheetDialog(category: CategoryEntity) {
        // TODO: BottomSheetDialog chiqishi kerak
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetBinding = LayoutBottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(bottomSheetBinding.root)
        bottomSheetDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        bottomSheetBinding.layout.setBackgroundResource(category.categoryColor)
        setTextColor(category, bottomSheetBinding.categoryNameTv, requireContext())
        bottomSheetBinding.categoryNameTv.text = category.categoryName
        setTextColor(category, bottomSheetBinding.taskCountTv, requireContext())
        bottomSheetBinding.taskCountTv.text = countTasks(category.id, requireContext())
        setPenImage(category, bottomSheetBinding.pen, requireContext())
        val adapter = BottomDialogTaskAdapter(
            category,
            requireContext(),
            loadTasksByCategory(category.id),
        )
        bottomSheetBinding.taskRv.adapter = adapter
        bottomSheetDialog.show()
    }

    private fun loadTasksByCategory(categoryId: Long): ArrayList<Task> {
        val list = ArrayList<Task>()
        val tasks = database.taskDao().getTasks()
        tasks.forEach {
            if (it.categoryId == categoryId) list.add(it)
        }
        return list
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

        @SuppressLint("RestrictedApi")
        fun Menu.showIcons() {
            (this as? MenuBuilder)?.setOptionalIconsVisible(true)
        }
    }
}