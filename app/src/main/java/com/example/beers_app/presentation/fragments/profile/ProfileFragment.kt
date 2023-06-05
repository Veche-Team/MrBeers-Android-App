package com.example.beers_app.presentation.fragments.profile

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
import com.example.beers.R
import com.example.beers.databinding.FragmentProfileBinding
import com.example.beers_app.presentation.fragments.profile.util.VMStringResource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding
        get() = _binding!!
    val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addTextChangeListeners()
        observeErrorsAndButtonsState()
        setButtonListeners()
        showToastOnEvent()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addTextChangeListeners() {
        binding.editTextChangeName.addTextChangedListener {
            viewModel.onChangeNameInput(it.toString())
        }
        binding.editTextOldPassword.addTextChangedListener {
            viewModel.onOldPasswordInput(it.toString())
        }
        binding.editTextNewPassword.addTextChangedListener {
            viewModel.onNewPasswordInput(it.toString())
        }
        binding.editTextRepeatNewPassword.addTextChangedListener {
            viewModel.onRepeatNewPasswordInput(it.toString())
        }
        binding.editTextDelete.addTextChangedListener {
            viewModel.onDeleteUserPasswordInput(it.toString())
        }
    }

    private fun observeErrorsAndButtonsState() {
        viewLifecycleOwner.lifecycleScope.launch {
            binding.editTextChangeName.setText(viewModel.getName())
            viewModel.state.collectLatest {
                binding.textLayoutChangeName.error = it.fieldErrors.changeNameError
                binding.textLayoutOldPassword.error = it.fieldErrors.oldPasswordError
                binding.textLayoutNewPassword.error = it.fieldErrors.newPasswordError
                binding.textLayoutRepeatNewPassword.error = it.fieldErrors.repeatNewPasswordError
                binding.textLayoutDeleteUserPassword.error = it.fieldErrors.deleteUserPasswordError

                binding.changeNameButton.isEnabled = it.isButtonsEnabled.isChangeNameEnabled
                binding.changePasswordButton.isEnabled = it.isButtonsEnabled.isChangePasswordEnabled
                binding.deleteUserButton.isEnabled = it.isButtonsEnabled.isDeleteUserEnabled
            }
        }
    }

    private fun setButtonListeners() {
        binding.changeNameButton.setOnClickListener {
            viewModel.changeName()
        }
        binding.changePasswordButton.setOnClickListener {
            viewModel.changePassword()
        }
        binding.deleteUserButton.setOnClickListener {
            viewModel.deleteUser()
        }
    }

    private fun showToastOnEvent() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.toastMessage.collectLatest {
                when (it) {
                    VMStringResource.NameChanged -> {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.name_changed_toast),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    VMStringResource.PasswordChanged -> {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.password_changed_toast),
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.editTextOldPassword.setText("")
                        binding.editTextNewPassword.setText("")
                        binding.editTextRepeatNewPassword.setText("")
                    }
                    VMStringResource.AccountDeleted -> {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.account_deleted_toast),
                            Toast.LENGTH_SHORT
                        ).show()
                        findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToListFragment())
                    }
                }
            }
        }
    }
}