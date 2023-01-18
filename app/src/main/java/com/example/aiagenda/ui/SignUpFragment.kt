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
import com.example.aiagenda.databinding.FragmentSignUpBinding
import com.example.aiagenda.util.AuthenticationStatus
import com.example.aiagenda.util.ValidationError
import com.example.aiagenda.viewmodel.AuthViewModel
import com.example.aiagenda.viewmodel.ViewModelFactory
import com.google.firebase.auth.FirebaseAuth

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

        registerStatus()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignUp.setOnClickListener {
            viewModel.register(
                email = binding.etEmail.text.toString(),
                password = binding.etPassword.text.toString(),
                lastName = binding.etLastName.text.toString(),
                firstName = binding.etFirstName.text.toString(),
                year = binding.spYear.selectedItem.toString()
            )
            validateForm()
        }
    }

    private fun registerStatus() {
        viewModel.repository.registerStatus.observe(this.viewLifecycleOwner) {
            when (it) {
                AuthenticationStatus.SUCCESS -> {
                    findNavController().navigate(
                        SignUpFragmentDirections.actionSignUpFragmentToDialogFragment(
                            getString(
                                R.string.signup_success
                            ),
                            true
                        )
                    )
                }
                AuthenticationStatus.USER_EXISTS -> {
                    findNavController().navigate(
                        SignUpFragmentDirections.actionSignUpFragmentToDialogFragment(
                            getString(
                                R.string.email_used
                            ),
                            true
                        )
                    )
                }
                AuthenticationStatus.NO_INTERNET_CONNECTION -> {
                    findNavController().navigate(
                        SignUpFragmentDirections.actionSignUpFragmentToDialogFragment(
                            getString(
                                R.string.no_internet
                            ),
                            false
                        )
                    )
                }
                else -> {
                    return@observe
                }
            }
        }
    }

    private fun validateForm() {
        viewModel.registerError.observe(viewLifecycleOwner) {
            if (viewModel.registerError.value == ValidationError.LAST_NAME_IS_EMPTY) {
                binding.apply {
                    tieLastNameLayout.isErrorEnabled = true
                    tieLastNameLayout.error = getString(R.string.empty_field)
                    tieFirstNameLayout.isErrorEnabled = false
                    tieEmailLayout.isErrorEnabled = false
                    tiePasswordLayout.isErrorEnabled = false
                    tvSpinnerError.visibility = View.GONE
                }
            }
            if (viewModel.registerError.value == ValidationError.FIRST_NAME_IS_EMPTY) {
                binding.apply {
                    tieFirstNameLayout.isErrorEnabled = true
                    tieFirstNameLayout.error = getString(R.string.empty_field)
                    tieLastNameLayout.isErrorEnabled = false
                    tieEmailLayout.isErrorEnabled = false
                    tiePasswordLayout.isErrorEnabled = false
                    tvSpinnerError.visibility = View.GONE
                }
            }
            if (viewModel.registerError.value == ValidationError.EMAIL_IS_EMPTY) {
                binding.apply {
                    tieEmailLayout.isErrorEnabled = true
                    tieEmailLayout.error = getString(R.string.empty_field)
                    tiePasswordLayout.isErrorEnabled = false
                    tieLastNameLayout.isErrorEnabled = false
                    tieFirstNameLayout.isErrorEnabled = false
                    tvSpinnerError.visibility = View.GONE
                }
            }
            if (viewModel.registerError.value == ValidationError.EMAIL_NOT_VALID) {
                binding.apply {
                    tieEmailLayout.isErrorEnabled = true
                    tieEmailLayout.error = getString(R.string.email_not_valid)
                    tiePasswordLayout.isErrorEnabled = false
                    tieLastNameLayout.isErrorEnabled = false
                    tieFirstNameLayout.isErrorEnabled = false
                    tvSpinnerError.visibility = View.GONE
                }
            }
            if (viewModel.registerError.value == ValidationError.PASSWORD_IS_EMPTY) {
                binding.apply {
                    tiePasswordLayout.isErrorEnabled = true
                    tiePasswordLayout.error = getString(R.string.empty_field)
                    tieEmailLayout.isErrorEnabled = false
                    tieLastNameLayout.isErrorEnabled = false
                    tieFirstNameLayout.isErrorEnabled = false
                    tvSpinnerError.visibility = View.GONE
                }
            }
            if (viewModel.registerError.value == ValidationError.PASSWORD_SHORT) {
                binding.apply {
                    tiePasswordLayout.isErrorEnabled = true
                    tiePasswordLayout.error = getString(R.string.password_short)
                    tieEmailLayout.isErrorEnabled = false
                    tieLastNameLayout.isErrorEnabled = false
                    tieFirstNameLayout.isErrorEnabled = false
                    tvSpinnerError.visibility = View.GONE
                }
            }
            if (viewModel.registerError.value == ValidationError.YEAR_NOT_SELECTED) {
                binding.apply {
                    tvSpinnerError.visibility = View.VISIBLE
                    tvSpinnerError.error = ""
                    tvSpinnerError.text = getString(R.string.select_year)
                    tieEmailLayout.isErrorEnabled = false
                    tieLastNameLayout.isErrorEnabled = false
                    tieFirstNameLayout.isErrorEnabled = false
                    tiePasswordLayout.isErrorEnabled = false
                }
            }
        }
    }
}