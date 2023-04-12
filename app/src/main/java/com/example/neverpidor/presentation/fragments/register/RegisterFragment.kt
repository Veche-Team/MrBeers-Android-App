package com.example.neverpidor.presentation.fragments.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.neverpidor.databinding.RegisterFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment: Fragment() {

    private var _binding: RegisterFragmentBinding? = null
    private val binding: RegisterFragmentBinding
    get() = _binding!!
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RegisterFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.registerErrors.observe(viewLifecycleOwner) {
            binding.textLayoutPhone.error = it.numberError
            binding.textLayoutName.error = it.nameError
            binding.textLayoutPassword.error = it.passwordError
            binding.textLayoutConfirmPassword.error = it.passwordRepeatError
        }
        viewModel.isButtonEnabled.observe(viewLifecycleOwner) {
            binding.registerButton.isEnabled = it
        }

        binding.editTextPhone.addTextChangedListener {
            viewModel.onPhoneInput(it.toString())
        }
        binding.editTextName.addTextChangedListener {
            viewModel.onNameInput(it.toString())
        }
        binding.editTextPassword.addTextChangedListener {
            viewModel.onPasswordInput(it.toString())
        }
        binding.editTextConfirmPassword.addTextChangedListener {
            viewModel.onRepeatPasswordInput(it.toString())
        }

        binding.registerButton.setOnClickListener {
            viewModel.register()
        }
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
        viewModel.goBackEvent.observe(viewLifecycleOwner) {
            if (it?.getContent() == true) {
                findNavController().navigateUp()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}