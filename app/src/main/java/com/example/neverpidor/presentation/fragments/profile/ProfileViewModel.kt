package com.example.neverpidor.presentation.fragments.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neverpidor.domain.use_cases.user_validation.UserValidationUseCases
import com.example.neverpidor.domain.use_cases.users.UserProfileUseCases
import com.example.neverpidor.presentation.fragments.profile.util.ProfileState
import com.example.neverpidor.presentation.fragments.profile.util.VMStringResource
import com.example.neverpidor.util.PasswordException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userProfileUseCases: UserProfileUseCases,
    private val validationUseCases: UserValidationUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state

    private val _toastMessage: MutableSharedFlow<VMStringResource> = MutableSharedFlow()
    val toastMessage: SharedFlow<VMStringResource> = _toastMessage

    fun onChangeNameInput(text: String) {
        _state.value = state.value.copy(
            inputFields = state.value.inputFields.copy(changeNameField = text),
            fieldErrors = state.value.fieldErrors.copy(changeNameError = "")
        )
        enableButtons()
    }

    suspend fun getName() = withContext(viewModelScope.coroutineContext) {
        userProfileUseCases.getUserUseCase().name
    }

    fun onOldPasswordInput(text: String) {
        _state.value = state.value.copy(
            inputFields = state.value.inputFields.copy(oldPasswordField = text.toCharArray()),
            fieldErrors = state.value.fieldErrors.copy(oldPasswordError = "")
        )
        enableButtons()
    }

    fun onNewPasswordInput(text: String) {
        try {
            validationUseCases.passwordValidationUseCase(text)
            _state.value = state.value.copy(
                inputFields = state.value.inputFields.copy(newPasswordField = text.toCharArray()),
                fieldErrors = state.value.fieldErrors.copy(newPasswordError = "")
            )
        } catch (e: PasswordException.NewPasswordException) {
            _state.value = state.value.copy(
                fieldErrors = state.value.fieldErrors.copy(newPasswordError =  e.message ?: "")
            )
        }
        enableButtons()
    }

    fun onRepeatNewPasswordInput(text: String) {
        _state.value = state.value.copy(
            inputFields = state.value.inputFields.copy(repeatNewPasswordField = text.toCharArray()),
            fieldErrors = state.value.fieldErrors.copy(repeatNewPasswordError = "")
        )
        enableButtons()
    }

    fun onDeleteUserPasswordInput(text: String) {
        _state.value = state.value.copy(
            inputFields = state.value.inputFields.copy(deleteUserPasswordField = text.toCharArray()),
            fieldErrors = state.value.fieldErrors.copy(deleteUserPasswordError = "")
        )
        enableButtons()
    }

    private fun enableButtons() {
        _state.value = state.value.copy(
            isButtonsEnabled = state.value.isButtonsEnabled.copy(
                isChangeNameEnabled = state.value.inputFields.changeNameField.isNotEmpty()
                        && state.value.fieldErrors.changeNameError.isEmpty(),
                isChangePasswordEnabled = state.value.inputFields.oldPasswordField.isNotEmpty()
                        && state.value.inputFields.newPasswordField.isNotEmpty()
                        && state.value.inputFields.repeatNewPasswordField.isNotEmpty()
                        && state.value.fieldErrors.oldPasswordError.isEmpty()
                        && state.value.fieldErrors.newPasswordError.isEmpty()
                        && state.value.fieldErrors.repeatNewPasswordError.isEmpty(),
                isDeleteUserEnabled = state.value.inputFields.deleteUserPasswordField.isNotEmpty()
                        && state.value.fieldErrors.deleteUserPasswordError.isEmpty()
            )
        )
    }

    fun changeName() = viewModelScope.launch {
        userProfileUseCases.changeUserNameUseCase(state.value.inputFields.changeNameField)
        _toastMessage.emit(VMStringResource.NameChanged)
    }

    fun changePassword() = viewModelScope.launch(Dispatchers.IO) {
        try {
            userProfileUseCases.changeUserPasswordUseCase(
                oldPassword = state.value.inputFields.oldPasswordField,
                newPassword = state.value.inputFields.newPasswordField,
                repeatNewPassword = state.value.inputFields.repeatNewPasswordField
            )
        } catch (e: PasswordException) {
            _state.emit(
                state.value.copy(
                    fieldErrors = when (e) {
                        is PasswordException.OldPasswordException -> {
                            state.value.fieldErrors.copy(
                                oldPasswordError = e.message ?: ""
                            )
                        }
                        is PasswordException.NewPasswordException -> {
                            state.value.fieldErrors.copy(
                                newPasswordError = e.message ?: ""
                            )
                        }
                        is PasswordException.RepeatPasswordException -> {
                            state.value.fieldErrors.copy(
                                repeatNewPasswordError = e.message ?: ""
                            )
                        }
                    }
                )
            )
            return@launch
        }
        _toastMessage.emit(VMStringResource.PasswordChanged)
        _state.emit(
            state.value.copy(
                fieldErrors = state.value.fieldErrors.copy(
                    oldPasswordError = "",
                    newPasswordError = "",
                    repeatNewPasswordError = ""
                )
            )
        )
    }

    fun deleteUser() = viewModelScope.launch {
        try {
            userProfileUseCases.deleteUserUseCase(
                state.value.inputFields.deleteUserPasswordField
            )
        } catch (e: PasswordException) {
            _state.emit(
                state.value.copy(
                    fieldErrors = state.value.fieldErrors.copy(
                        deleteUserPasswordError = e.message ?: ""
                    )
                )
            )
        }
        _toastMessage.emit(VMStringResource.AccountDeleted)
    }
}
