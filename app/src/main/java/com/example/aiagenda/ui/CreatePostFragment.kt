package com.example.aiagenda.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.aiagenda.R
import com.example.aiagenda.databinding.FragmentCreatePostBinding
import com.example.aiagenda.model.Post
import com.example.aiagenda.util.UiStatus
import com.example.aiagenda.viewmodel.AuthViewModel
import com.example.aiagenda.viewmodel.PostsViewModel
import com.example.aiagenda.viewmodel.ViewModelFactory
import java.util.*

class CreatePostFragment : Fragment() {
    private lateinit var binding: FragmentCreatePostBinding

    private val authViewModel: AuthViewModel by viewModels {
        ViewModelFactory(requireActivity().application)
    }
    private val postsViewModel: PostsViewModel by viewModels {
        ViewModelFactory(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_create_post,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addViewModel()
        observeUiState()
        setControls()
        createPost()

    }

    private fun addViewModel() {
        binding.viewModel = postsViewModel
        binding.lifecycleOwner = this
    }

    private fun createPost() {
        binding.btnCreatePost.setOnClickListener {
            authViewModel.getSession { user ->
                if (user != null) {
                    postsViewModel.addPost(
                        user,
                        Post(
                            id = Random().nextLong().toString(),
                            title = binding.etTitle.text.toString(),
                            type = binding.etPostType.text.toString(),
                            date = binding.etDate.text.toString(),
                            hour = binding.etHour.text.toString()
                        )
                    )
                }
            }
        }
    }


    private fun setControls() {
        binding.ivArrowBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.etDate.setOnClickListener {
            openDatePickerDialog(binding.etDate)
        }

        binding.etHour.setOnClickListener {
            openTimePickerDialog(binding.etHour)
        }
    }

    private fun observeUiState() {
        postsViewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                UiStatus.LOADING -> {
                    binding.pbLoading.visibility = View.VISIBLE
                }
                UiStatus.SUCCESS -> {
                    binding.pbLoading.visibility = View.GONE
                    clearFields()
                }
                else -> {
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToDialogFragment(
                            getString(R.string.another_exception),
                            false,
                            true
                        )
                    )
                }
            }
        }
    }

    private fun clearFields() {
        binding.apply {
            etTitle.text.clear()
            etPostType.text.clear()
            etHour.text.clear()
            etDate.text.clear()
        }
    }

    private fun openDatePickerDialog(editTextField: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(), { _, yearPicked, monthOfYear, dayOfMonth ->
                val datePicked = String.format(
                    "%02d/%02d/%02d", dayOfMonth, monthOfYear + 1, yearPicked
                )
                editTextField.setText(datePicked)
            }, year, month, day
        )
        datePickerDialog.show()
    }

    private fun openTimePickerDialog(editTextField: EditText) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, hourPicked, minutePicked ->
                val timePicked = String.format("%02d:%02d", hourPicked, minutePicked)
                editTextField.setText(timePicked)
            },
            hour,
            minute,
            true
        )
        timePickerDialog.show()
    }

}