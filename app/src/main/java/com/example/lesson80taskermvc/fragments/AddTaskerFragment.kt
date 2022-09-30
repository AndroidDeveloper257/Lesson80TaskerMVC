package com.example.lesson80taskermvc.fragments

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.lesson80taskermvc.R
import com.example.lesson80taskermvc.adapters.BottomCategoryAdapter
import com.example.lesson80taskermvc.database.AppDatabase
import com.example.lesson80taskermvc.database.categories.CategoryEntity
import com.example.lesson80taskermvc.database.tasks.Task
import com.example.lesson80taskermvc.databinding.BottomCategoryListItemBinding
import com.example.lesson80taskermvc.databinding.FragmentAddTaskerBinding
import com.example.lesson80taskermvc.receiver.AlarmReceiver
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.time.LocalDateTime

class AddTaskerFragment : Fragment() {

    private lateinit var binding: FragmentAddTaskerBinding
    private lateinit var database: AppDatabase
    private var date: Int = -1
    private var month: Int = -1
    private var year: Int = -1
    private var hour: Int = -1
    private var minute: Int = -1
    private lateinit var category: CategoryEntity
    private var categoryId: Long = -1
    private lateinit var alarmManager: AlarmManager
    private lateinit var intent: Intent

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddTaskerBinding.inflate(layoutInflater)
        database = AppDatabase.getInstance(requireContext())
        intent = Intent(requireContext(), AlarmReceiver::class.java)
        alarmManager = requireContext().getSystemService(ALARM_SERVICE) as AlarmManager
        binding.apply {

            cancelBtn.setOnClickListener {
                findNavController().popBackStack()
            }

            calendar.setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    val datePickerDialog = DatePickerDialog(requireContext())
                    datePickerDialog.setOnDateSetListener { datePicker, y, m, d ->
                        /**
                         * month starts from 0
                         */
                        if (isDateValid(d, m + 1, y)) {
                            date = d
                            month = m + 1
                            year = y
                            binding.calendar.setImageResource(R.drawable.ic_calendar_checked)
                            hour = -1
                            minute = -1
                            binding.alarm.setImageResource(R.drawable.ic_alarm_unchecked)
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "An elapsed day selected",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    datePickerDialog.show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "You version code is unavailable",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            alarm.setOnClickListener {
                if (date == -1) {
                    Toast.makeText(requireContext(), "Please select a day", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                } else {
                    val now = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        LocalDateTime.now()
                    } else {
                        TODO("VERSION.SDK_INT < O")
                    }
                    val timePickerDialog = TimePickerDialog(
                        requireContext(),
                        { p0, h, m ->
                            if (isTimeValid(h, m)) {
                                hour = h
                                minute = m
                                binding.alarm.setImageResource(R.drawable.ic_alarm_checked)
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "An elapsed time selected",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        now.hour,
                        now.minute,
                        true
                    )
                    timePickerDialog.show()
                }
            }

            categoryNameTv.setOnClickListener {
                showCategoriesOnBottomSheet()
            }

            doneBtn.setOnClickListener {
                doneCLick()
            }

        }

        return binding.root
    }

    private fun isTimeValid(h: Int, m: Int): Boolean {
        val now = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.now()
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        if (now.hour > h) return false
        else if (now.hour == h) {
            if (now.minute > m) return false
        }
        return true
    }

    private fun isDateValid(d: Int, m: Int, y: Int): Boolean {
        val now = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.now()
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        if (y < now.year) return false
        else if (y == now.year) {
            if (m < now.monthValue) return false
            else if (m == now.monthValue) {
                if (d < now.dayOfMonth) return false
            }
        }
        return true
    }

    @SuppressLint("UseCompatLoadingForColorStateLists", "ResourceAsColor")
    private fun showCategoriesOnBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomItemBinding = BottomCategoryListItemBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(bottomItemBinding.root)
        bottomSheetDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val categories =
            ArrayList(database.categoryDao().getCategories())
        val selectionList = List(categories.size) { false } as ArrayList
        val bottomAdapter = BottomCategoryAdapter(
            requireContext(),
            categories,
            selectionList
        ) {
            category = it
            categoryId = it.id
            bottomItemBinding.doneBtn.visibility = View.VISIBLE
        }
        bottomItemBinding.categoryRv.adapter = bottomAdapter
        bottomItemBinding.doneBtn.setOnClickListener {
            binding.categoryNameTv.text = category.categoryName
            binding.categoryNameTv.setTextColor(resources.getColor(category.categoryColor))
            binding.categoryIndicator.backgroundTintList =
                resources.getColorStateList(category.categoryColor)
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.show()
    }

    private fun doneCLick() {
        val taskName = binding.taskNameEt.text.toString()
        if (taskName.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter the task", Toast.LENGTH_SHORT).show()
            return
        }
        if (date == -1) {
            Toast.makeText(requireContext(), "Please set the date", Toast.LENGTH_SHORT).show()
            return
        }
        if (hour == -1) {
            Toast.makeText(requireContext(), "Please set alarm", Toast.LENGTH_SHORT).show()
            return
        }
        if (categoryId == -1L) {
            Toast.makeText(requireContext(), "Please select the category", Toast.LENGTH_SHORT)
                .show()
            return
        }
        val task = Task(
            0,
            taskName,
            categoryId,
            date,
            month,
            year,
            hour,
            minute
        )
        database.taskDao().addTask(task)
        Toast.makeText(requireContext(), "Added successfully", Toast.LENGTH_SHORT).show()
        setAlarm()
        findNavController().popBackStack()
    }

    private fun setAlarm() {
        val elapsedRealTime = SystemClock.elapsedRealtime()
        val interval = 5000
        val requestCode = getRequestCode()

        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getBroadcast(
                requireContext(),
                requestCode,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            PendingIntent.getBroadcast(
                requireContext(),
                requestCode,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
            )
        }

        // TODO: there is a problem with FLAG on pending intent

        alarmManager.setExact(
            AlarmManager.ELAPSED_REALTIME,
            elapsedRealTime + interval,
            pendingIntent
        )
    }

    private fun getRequestCode(): Int {
        val list = database.taskDao().getTasks()
        return (list.elementAt(list.size - 1).id).toInt()
    }

    companion object {
        private const val TAG = "AddTaskerFragment"
    }
}