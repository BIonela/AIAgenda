package com.example.aiagenda.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.util.rangeTo
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.aiagenda.R
import com.example.aiagenda.databinding.FragmentSignUpBinding
import com.example.aiagenda.repository.AuthenticationRepository
import com.example.aiagenda.util.AuthenticationStatus
import com.example.aiagenda.viewmodel.AuthViewModel
import com.example.aiagenda.viewmodel.ViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var auth: FirebaseAuth
    private val viewModel: AuthViewModel by viewModels {
        ViewModelFactory(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_sign_up,
            container,
            false
        )
        auth = FirebaseAuth.getInstance()

        viewModel.repository.status.observe(this.viewLifecycleOwner) {
            if (it == AuthenticationStatus.SUCCESS) {
                Toast.makeText(
                    requireContext(),
                    "Inregistrare efectuata cu succes.",
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().navigateUp()
            } else {
                Log.e("Nav Message", "NU MERGE")
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignUp.setOnClickListener {
            //check this in viewModel
            if (binding.etEmail.text.toString()
                    .isNotEmpty() || binding.etPassword.text.toString().isNotEmpty()
            ) {
                viewModel.register(
                    binding.etEmail.text.toString(),
                    binding.etPassword.text.toString(),
                    binding.tiePasswordLayout
                )

            } else {
                Toast.makeText(
                    requireContext(),
                    "Completati campurile!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}