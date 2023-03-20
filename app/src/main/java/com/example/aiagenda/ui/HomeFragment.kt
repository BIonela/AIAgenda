package com.example.aiagenda.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.aiagenda.R
import com.example.aiagenda.adapter.TaskAdapter
import com.example.aiagenda.databinding.FragmentHomeBinding
import com.example.aiagenda.viewmodel.AuthViewModel
import com.example.aiagenda.viewmodel.TaskViewModel
import com.example.aiagenda.viewmodel.ViewModelFactory

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private val taskAdapter = TaskAdapter()
    private val authViewModel: AuthViewModel by viewModels {
        ViewModelFactory(requireActivity().application)
    }
    private val taskViewModel: TaskViewModel by viewModels {
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
        observe()
        //////////////////////

        binding.rvTasks.layoutManager = LinearLayoutManager(context)
        binding.rvTasks.adapter = taskAdapter

        taskAdapter.onItemClick = {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToTaskDetailsFragment(it))
        }

        authViewModel.getSession { user ->
            if (user != null) {
                taskViewModel.getTasks(user)
            }
        }

        taskViewModel.tasks.observe(viewLifecycleOwner) { tasks ->
            //TODO: verifica daca lista e goala
            taskAdapter.submitList(tasks.tasks)
        }


        /////////////////////
    }

    private fun observe() {
        authViewModel.getSession {
            authViewModel.user.observe(viewLifecycleOwner) { user ->
                if (user == null) {
                    authViewModel.getSession {}
                    stateLoading()
                } else {
                    binding.tvName.text = getString(
                        R.string.full_name,
                        user.first_name,
                        user.last_name
                    )
                    binding.tvStudyYear.text = getString(
                        R.string.student_study_year, user.study_year
                    )
                    if (user.photo_url != "") {
                        binding.sivProfilePicture.colorFilter = null
                        Glide.with(this)
                            .load(user.photo_url)
                            .placeholder(R.drawable.progress_animation)
                            .centerCrop()
                            .into(binding.sivProfilePicture)

                    } else {
                        binding.sivProfilePicture.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.orange_medium
                            )
                        )
                        binding.sivProfilePicture.setImageResource(R.drawable.ic_person)
                        binding.sivProfilePicture.setColorFilter(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.white
                            )
                        )
                    }
                    stateSuccess()
                }
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