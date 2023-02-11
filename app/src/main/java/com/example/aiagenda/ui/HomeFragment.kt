package com.example.aiagenda.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.aiagenda.R
import com.example.aiagenda.databinding.FragmentHomeBinding
import com.example.aiagenda.viewmodel.AuthViewModel
import com.example.aiagenda.viewmodel.ViewModelFactory

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private val authViewModel: AuthViewModel by viewModels {
        ViewModelFactory(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {}
        
        authViewModel.getSession {}
        observe()
    }

    private fun observe() {
        authViewModel.user.observe(viewLifecycleOwner) { user ->
            if (user == null) {
                authViewModel.getSession {}
                stateLoading()
            } else {
                stateSuccess()
                binding.tvName.text = getString(
                    R.string.full_name,
                    user.first_name,
                    user.last_name
                )
                binding.tvStudyYear.text = getString(
                    R.string.student_study_year, user.study_year
                )
            }
        }
    }

    private fun stateLoading() {
        binding.apply {
            binding.pbLoading.visibility = View.VISIBLE
            mcvProfileBackground.visibility = View.GONE
            sivProfilePicture.visibility = View.GONE
            tvName.visibility = View.GONE
            tvStudyYear.visibility = View.GONE
        }
    }

    private fun stateSuccess() {
        binding.apply {
            binding.pbLoading.visibility = View.GONE
            mcvProfileBackground.visibility = View.VISIBLE
            sivProfilePicture.visibility = View.VISIBLE
            tvName.visibility = View.VISIBLE
            tvStudyYear.visibility = View.VISIBLE
        }
    }

}