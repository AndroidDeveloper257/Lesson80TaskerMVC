package com.example.lesson80taskermvc.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lesson80taskermvc.R
import com.example.lesson80taskermvc.databinding.FragmentAddTaskerBinding

class AddTaskerFragment : Fragment() {

    private lateinit var binding: FragmentAddTaskerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddTaskerBinding.inflate(layoutInflater)

        return binding.root
    }

    companion object {
        private const val TAG = "AddTaskerFragment"
    }
}