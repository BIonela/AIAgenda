package com.example.aiagenda.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
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

        viewModel.repository.status.observe(this.viewLifecycleOwner) {
            if (it == AuthenticationStatus.SUCCESS) {
                Toast.makeText(
                    requireContext(),
                    "Inregistrare efectuata cu succes.",
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().navigateUp()
            } else if (it == AuthenticationStatus.USER_EXISTS) {
                Log.e("Nav Message", "EXISTA DEJA")
            } else if (it == AuthenticationStatus.NO_INTERNET_CONNECTION) {
                Log.e("Nav Message", "FARA INTERNET")
            }
        }

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

            viewModel.error.observe(viewLifecycleOwner) {
                if (viewModel.error.value == ValidationError.LAST_NAME_IS_EMPTY) {
                    binding.tieLastNameLayout.error = "Completati campul!"
                    binding.tieFirstNameLayout.isErrorEnabled = false
                    binding.tieEmailLayout.isErrorEnabled = false
                    binding.tiePasswordLayout.isErrorEnabled = false
                    binding.tvSpinnerError.visibility = View.GONE

                }
                if (viewModel.error.value == ValidationError.FIRST_NAME_IS_EMPTY) {
                    binding.tieFirstNameLayout.error = "Completati campul!"
                    binding.tieLastNameLayout.isErrorEnabled = false
                    binding.tieEmailLayout.isErrorEnabled = false
                    binding.tiePasswordLayout.isErrorEnabled = false
                    binding.tvSpinnerError.visibility = View.GONE

                }
                if (viewModel.error.value == ValidationError.EMAIL_IS_EMPTY) {
                    binding.tieEmailLayout.error = "Completati campul!"
                    binding.tiePasswordLayout.isErrorEnabled = false
                    binding.tieLastNameLayout.isErrorEnabled = false
                    binding.tieFirstNameLayout.isErrorEnabled = false
                    binding.tvSpinnerError.visibility = View.GONE

                }
                if (viewModel.error.value == ValidationError.EMAIL_NOT_VALID) {
                    binding.tieEmailLayout.error = "Nu ati introdus adresa institutionala UTCB!"
                    binding.tiePasswordLayout.isErrorEnabled = false
                    binding.tieLastNameLayout.isErrorEnabled = false
                    binding.tieFirstNameLayout.isErrorEnabled = false
                    binding.tvSpinnerError.visibility = View.GONE

                }
                if (viewModel.error.value == ValidationError.PASSWORD_IS_EMPTY) {
                    binding.tiePasswordLayout.error = "Completati campul!"
                    binding.tieEmailLayout.isErrorEnabled = false
                    binding.tieLastNameLayout.isErrorEnabled = false
                    binding.tieFirstNameLayout.isErrorEnabled = false
                    binding.tvSpinnerError.visibility = View.GONE

                }
                if (viewModel.error.value == ValidationError.PASSWORD_SHORT) {
                    binding.tiePasswordLayout.error = "Parola trebuie sa contina minim 6 caractere!"
                    binding.tieEmailLayout.isErrorEnabled = false
                    binding.tieLastNameLayout.isErrorEnabled = false
                    binding.tieFirstNameLayout.isErrorEnabled = false
                    binding.tvSpinnerError.visibility = View.GONE
                }
                if (viewModel.error.value == ValidationError.YEAR_NOT_SELECTED) {
                    binding.tvSpinnerError.visibility = View.VISIBLE
                    binding.tvSpinnerError.error = "Eroare"
                    binding.tvSpinnerError.text = getString(R.string.select_year)
                    binding.tieEmailLayout.isErrorEnabled = false
                    binding.tieLastNameLayout.isErrorEnabled = false
                    binding.tieFirstNameLayout.isErrorEnabled = false
                    binding.tiePasswordLayout.isErrorEnabled = false
                }
            }

        }
    }
}