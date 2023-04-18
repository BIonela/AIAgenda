package com.example.aiagenda.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aiagenda.R
import com.example.aiagenda.adapter.SchoolClassAdapter
import com.example.aiagenda.databinding.FragmentClassesBinding
import com.example.aiagenda.util.UiStatus
import com.example.aiagenda.viewmodel.AuthViewModel
import com.example.aiagenda.viewmodel.ClassViewModel
import com.example.aiagenda.viewmodel.ViewModelFactory

class ClassesFragment : Fragment() {
    private lateinit var binding: FragmentClassesBinding

    private val schoolClassAdapter = SchoolClassAdapter()
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
            R.layout.fragment_classes,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeUiState()
        navigateToClass()

        binding.rvClasses.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = schoolClassAdapter
        }

        binding.ivArrowBack.setOnClickListener {
            findNavController().navigateUp()
        }

        authViewModel.getSession { user ->
            if (user != null) {
                classViewModel.getClasses(user)
            }
        }

        classViewModel.classes.observe(viewLifecycleOwner) {
            Log.e("CLASSES", it.toString())
            schoolClassAdapter.submitList(it)
        }

    }

    private fun observeUiState() {
        classViewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                UiStatus.LOADING -> {
                    binding.pbLoading.visibility = View.VISIBLE
                }
                UiStatus.SUCCESS -> {
                    binding.pbLoading.visibility = View.GONE
                }
                else -> {
                    findNavController().navigate(
                        ClassesFragmentDirections.actionClassesFragmentToDialogFragment(
                            getString(R.string.another_exception),
                            false,
                            true
                        )
                    )
                }
            }
        }
    }

    private fun navigateToClass() {
        schoolClassAdapter.onItemClick = {
            findNavController().navigate(
                ClassesFragmentDirections.actionClassesFragmentToClassDetailsFragment(it)
            )
        }
    }
}