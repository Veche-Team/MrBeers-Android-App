package com.example.neverpidor.presentation.fragments.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.neverpidor.databinding.RegisterFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment() {

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

        observeErrorsAndEnableButton()
        addTextChangedListeners()
        setupButtonsListeners()
        showToastOnEvent()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeErrorsAndEnableButton() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collectLatest {
                binding.registerButton.isEnabled = it.isButtonEnabled
                binding.textLayoutPhone.error = it.errors.numberError
                binding.textLayoutName.error = it.errors.nameError
                binding.textLayoutPassword.error = it.errors.passwordError
                binding.textLayoutConfirmPassword.error = it.errors.passwordRepeatError
            }
        }
    }

    private fun addTextChangedListeners() {
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
    }

    private fun setupButtonsListeners() {
        binding.registerButton.setOnClickListener {
            viewModel.register()
        }
        binding.backButton.setOnClickListener {
            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToGreetingFragment())
        }
    }

    private fun showToastOnEvent() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.goBackEvent.collectLatest {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToGreetingFragment())
            }
        }
    }
}