package com.example.aiagenda.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.aiagenda.MainActivity
import android.Manifest
import android.net.Uri
import android.widget.Toast
import com.example.aiagenda.R
import com.example.aiagenda.databinding.FragmentProfileBinding
import com.example.aiagenda.util.UserDataStatus
import com.example.aiagenda.viewmodel.AuthViewModel
import com.example.aiagenda.viewmodel.UiStateViewModel
import com.example.aiagenda.viewmodel.ViewModelFactory
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val authViewModel: AuthViewModel by viewModels {
        ViewModelFactory(requireActivity().application)
    }
    private val loadingViewModel: UiStateViewModel by activityViewModels()

    private val requestSinglePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                gallery.launch("image/*")
            } else {
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.permission_deny),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    private var gallery = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        authViewModel.getSession { user ->
            if (user != null) {
                if (uri != null) {
                    authViewModel.uploadPhoto(uri, user) { it, _ ->
                        if (it == UserDataStatus.SUCCESS) {
                            if (isAdded) {
                                binding.sivProfile.colorFilter = null
                                Glide.with(this)
                                    .load(uri)
                                    .placeholder(R.drawable.progress_animation)
                                    .centerCrop()
                                    .into(binding.sivProfile)
                                binding.sivProfile.visibility = View.VISIBLE
                                loadingViewModel.setSuccess()
                            }
                            binding.pbLoading.visibility = View.GONE
                        } else if (it == UserDataStatus.LOADING) {
                            binding.pbLoading.visibility = View.VISIBLE
                            binding.sivProfile.visibility = View.GONE
                            loadingViewModel.setLoading()
                        } else if (it == UserDataStatus.ERROR) {
                            binding.pbLoading.visibility = View.GONE
                            binding.sivProfile.visibility = View.GONE
                            loadingViewModel.setError()
                        }
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_profile,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvSignOut.setOnClickListener {
            authViewModel.logout {
                val i = Intent(activity, MainActivity::class.java)
                startActivity(i)
            }
        }

        loadPhoto()

        binding.tvGallery.setOnClickListener {
            requestSinglePermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

    }

    private fun loadPhoto() {
        authViewModel.getSession {
            authViewModel.user.observe(viewLifecycleOwner) { user ->
                if (user.photo_url != "") {
                    binding.sivProfile.colorFilter = null
                    Glide.with(this)
                        .load(user.photo_url)
                        .placeholder(R.drawable.progress_animation)
                        .centerCrop()
                        .into(binding.sivProfile)
                } else {
                    binding.sivProfile.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.orange_medium
                        )
                    )
                    binding.sivProfile.setImageResource(R.drawable.ic_person)
                    binding.sivProfile.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                }
            }
        }
    }
}
