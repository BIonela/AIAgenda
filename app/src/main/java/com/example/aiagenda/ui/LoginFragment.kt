package com.example.aiagenda.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.aiagenda.R
import com.example.aiagenda.databinding.FragmentLoginBinding
import com.example.aiagenda.util.AuthenticationStatus
import com.example.aiagenda.util.ValidationError
import com.example.aiagenda.viewmodel.AuthViewModel
import com.example.aiagenda.viewmodel.ViewModelFactory

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: AuthViewModel by viewModels {
        ViewModelFactory(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_login,
            container,
            false
        )
        registerStatus()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvSignUp.setOnClickListener {
            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
            )
        }

        binding.btnLogin.setOnClickListener {
            viewModel.login(
                email = binding.tieEmail.text.toString(),
                password = binding.tiePassword.text.toString()
            )
            validateForm()
        }
    }

    private fun registerStatus() {
        viewModel.repository.loginStatus.observe(this.viewLifecycleOwner) {
            when (it) {
                AuthenticationStatus.SUCCESS -> {
                    //navigare catre home
                    findNavController().navigate(
                        LoginFragmentDirections.actionLoginFragmentToDialogFragment(
                            "Login efectuat cu succes",
                            false
                        )
                    )
                }
                AuthenticationStatus.EMAIL_NOT_FOUND -> {
                    findNavController().navigate(
                        LoginFragmentDirections.actionLoginFragmentToDialogFragment(
                            getString(R.string.email_not_found),
                            false
                        )
                    )
                }
                AuthenticationStatus.WRONG_PASSWORD -> {
                    findNavController().navigate(
                        LoginFragmentDirections.actionLoginFragmentToDialogFragment(
                            getString(R.string.wrong_password),
                            false
                        )
                    )
                }
                AuthenticationStatus.NO_INTERNET_CONNECTION -> {
                    findNavController().navigate(
                        LoginFragmentDirections.actionLoginFragmentToDialogFragment(
                            getString(
                                R.string.no_internet
                            ),
                            false
                        )
                    )
                }
                else -> return@observe
            }
        }
    }

    private fun validateForm() {
        viewModel.loginError.observe(viewLifecycleOwner) {
            if (viewModel.loginError.value == ValidationError.EMAIL_IS_EMPTY) {
                binding.apply {
                    tieEmailLayout.isErrorEnabled = true
                    tieEmailLayout.error = getString(R.string.empty_field)
                    tiePasswordLayout.isErrorEnabled = false
                }
            }
            if (viewModel.loginError.value == ValidationError.EMAIL_NOT_VALID) {
                binding.apply {
                    tieEmailLayout.isErrorEnabled = true
                    tieEmailLayout.error = getString(R.string.email_not_valid)
                    tiePasswordLayout.isErrorEnabled = false
                }
            }
            if (viewModel.loginError.value == ValidationError.PASSWORD_IS_EMPTY) {
                binding.apply {
                    tiePasswordLayout.isErrorEnabled = true
                    tiePasswordLayout.error = getString(R.string.empty_field)
                    tieEmailLayout.isErrorEnabled = false
                }
            }
            if (viewModel.loginError.value == ValidationError.PASSWORD_SHORT) {
                binding.apply {
                    tiePasswordLayout.isErrorEnabled = true
                    tiePasswordLayout.error = getString(R.string.password_short)
                    tieEmailLayout.isErrorEnabled = false
                }
            }
        }
    }
}