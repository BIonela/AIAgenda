package com.example.aiagenda.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aiagenda.R
import com.example.aiagenda.adapter.ClassbookAdapter
import com.example.aiagenda.databinding.FragmentClassbookBinding
import com.example.aiagenda.model.Grade
import com.example.aiagenda.util.UiStatus
import com.example.aiagenda.viewmodel.AuthViewModel
import com.example.aiagenda.viewmodel.ClassViewModel
import com.example.aiagenda.viewmodel.ViewModelFactory

class ClassbookFragment : Fragment() {
    private lateinit var binding: FragmentClassbookBinding

    private val classbookAdapter = ClassbookAdapter()
    private val authViewModel: AuthViewModel by viewModels {
        ViewModelFactory(requireActivity().application)
    }
    private val classViewModel: ClassViewModel by viewModels {
        ViewModelFactory(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_classbook,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeUiState()

        binding.ivArrowBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.rvClassbook.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = classbookAdapter
        }

        authViewModel.getSession { user ->
            if (user != null) {
                classViewModel.getClasses(user)

                val grades = mutableListOf<Grade>()
                classbookAdapter.onGradeAdded = { grade ->

                    val foundGrade = grades.filter { it.name == grade.name }
                    if (foundGrade.isNotEmpty()) {
                        foundGrade[0].grade = grade.grade
                    } else {
                        grades.add(grade)
                    }
                }

                binding.btnCalculate.setOnClickListener {
                    classViewModel.setGrade(user, grades)
                }

            }
        }

        classViewModel.classes.observe(viewLifecycleOwner) {
            classbookAdapter.submitList(it)
        }
    }

    private fun observeUiState() {
        classViewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                UiStatus.LOADING -> {
                    binding.pbLoading.visibility = View.VISIBLE
                    binding.btnCalculate.visibility = View.GONE
                }
                UiStatus.SUCCESS -> {
                    binding.pbLoading.visibility = View.GONE
                    binding.btnCalculate.visibility = View.VISIBLE
                }
                else -> {
                    findNavController().navigate(
                        ClassbookFragmentDirections.actionClassbookFragmentToDialogFragment(
                            getString(R.string.another_exception),
                            false,
                            true
                        )
                    )
                }
            }
        }
    }
}