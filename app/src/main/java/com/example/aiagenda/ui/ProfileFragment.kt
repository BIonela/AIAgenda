package com.example.aiagenda.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.aiagenda.MainActivity
import com.example.aiagenda.R
import com.example.aiagenda.databinding.FragmentProfileBinding
import com.example.aiagenda.util.FireStoreCollection
import com.example.aiagenda.util.UserDataStatus
import com.example.aiagenda.viewmodel.AuthViewModel
import com.example.aiagenda.viewmodel.ProfileViewModel
import com.example.aiagenda.viewmodel.ViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val authViewModel: AuthViewModel by viewModels {
        ViewModelFactory(requireActivity().application)
    }

    //    private val profileViewModel: ProfileViewModel by viewModels {
//        ViewModelFactory(requireActivity().application)
//    }
//    private lateinit var uri: Uri

    private var contract = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        authViewModel.getSession { user ->
            if (user != null) {
                if (uri != null) {
                    authViewModel.uploadPhoto(uri, user) { it, _ ->
                        if (it == UserDataStatus.SUCCESS) {
                            binding.pbLoading.visibility = View.GONE
                            Glide.with(this)
                                .load(uri)
                                .placeholder(R.drawable.ic_person)
                                .into(binding.ivProfile)
                        } else if (it == UserDataStatus.LOADING) {
                            binding.pbLoading.visibility = View.VISIBLE
                        } else if (it == UserDataStatus.ERROR) {
                            binding.pbLoading.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

//    private var camera = registerForActivityResult(ActivityResultContracts.TakePicture()) {}
//    {
//        if (it) {
//            binding.ivProfile.setImageURI(uri)
//        }
//    }

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

        binding.tvFragment.setOnClickListener {

//            val photoFile = File.createTempFile(
//                "IMG_",
//                ".jpg",
//                requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//            )
//
//            uri = FileProvider.getUriForFile(
//                requireContext(),
//                "${requireContext().packageName}.provider",
//                photoFile
//            )

//            camera.launch(uri)
        }


//        authViewModel.loading.observe(viewLifecycleOwner) {
//            Log.e("UPLOAD", it.toString())
//        }

        binding.tvGallery.setOnClickListener {
            contract.launch("image/*")
        }

        authViewModel.getSession { }
        authViewModel.user.observe(viewLifecycleOwner) {
            Glide.with(this)
                .load(it.photo_url)
                .placeholder(R.drawable.ic_person)
                .into(binding.ivProfile)
        }

    }
}
