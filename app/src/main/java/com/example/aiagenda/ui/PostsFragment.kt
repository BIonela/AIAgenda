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
import com.example.aiagenda.adapter.PostsAdapter
import com.example.aiagenda.databinding.FragmentPostsBinding
import com.example.aiagenda.util.UiStatus
import com.example.aiagenda.viewmodel.AuthViewModel
import com.example.aiagenda.viewmodel.PostsViewModel
import com.example.aiagenda.viewmodel.ViewModelFactory
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage

class PostsFragment : Fragment() {
    private lateinit var binding: FragmentPostsBinding

    private val postsAdapter = PostsAdapter()
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
            R.layout.fragment_posts,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setControls()
        attachAdapter()
        observeUiState()
        getPosts()
        submitList()
        updateUI()

    }

    private fun setControls() {
        binding.ivArrowBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.fabAdd.setOnClickListener {
            findNavController().navigate(
                PostsFragmentDirections.actionPostsFragmentToCreatePostFragment()
            )
        }
    }

    private fun getPosts() {
        authViewModel.getSession { user ->
            if (user != null) {
                if (user.email == "admin${user.study_year}@student.utcb.ro") {
                    binding.fabAdd.visibility = View.VISIBLE
                }
                postsViewModel.getPosts(user)
            }
        }
    }

    private fun updateUI() {
        postsViewModel.posts.observe(viewLifecycleOwner) { postsBody ->
            if (postsBody.posts.isEmpty()) {
                binding.tvNoPosts.visibility = View.VISIBLE
                binding.ivNoPosts.visibility = View.VISIBLE
            } else {
                binding.tvNoPosts.visibility = View.GONE
                binding.ivNoPosts.visibility = View.GONE
            }

        }
    }

    private fun submitList() {
        postsViewModel.posts.observe(viewLifecycleOwner) { posts ->
            postsAdapter.submitList(posts.posts)
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

    private fun attachAdapter() {
        binding.rvPosts.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = postsAdapter
        }
    }
}