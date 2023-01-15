package com.example.aiagenda.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.aiagenda.R
import com.example.aiagenda.databinding.FragmentSignUpBinding
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignUp.setOnClickListener {
            if (binding.etEmail.text?.trim().toString()
                    .isNotEmpty() || binding.etPassword.text?.trim().toString().isNotEmpty()
            ) {
//                createUser(
//                    binding.etEmail.text?.trim().toString(),
//                    binding.etPassword.text?.trim().toString()

                viewModel.register(
                    binding.etEmail.text?.trim().toString(),
                    binding.etPassword.text?.trim().toString()
                )
                Log.e("Task Message", "Success")

            } else {
                Toast.makeText(requireContext(), "Input Reauired", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun createUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.e("Task Message", "Success")
                } else {
                    Log.e("Task Message", "Failed" + task.exception)

                }
            }
    }
}