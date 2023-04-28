package com.example.neverpidor.presentation.fragments.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neverpidor.domain.use_cases.users.UserProfileUseCases
import com.example.neverpidor.presentation.fragments.profile.util.ProfileState
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
    private val userProfileUseCases: UserProfileUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage: SharedFlow<String> = _toastMessage

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
            inputFields = state.value.inputFields.copy(oldPasswordField = text),
            fieldErrors = state.value.fieldErrors.copy(oldPasswordError = "")
        )
        enableButtons()
    }

    fun onNewPasswordInput(text: String) {
        _state.value = state.value.copy(
            inputFields = state.value.inputFields.copy(newPasswordField = text),
            fieldErrors = if (text.length in 1..5) state.value.fieldErrors.copy(newPasswordError = "password's too short")
            else state.value.fieldErrors.copy(newPasswordError = "")
        )
        enableButtons()
    }

    fun onRepeatNewPasswordInput(text: String) {
        _state.value = state.value.copy(
            inputFields = state.value.inputFields.copy(repeatNewPasswordField = text),
            fieldErrors = state.value.fieldErrors.copy(repeatNewPasswordError = "")
        )
        enableButtons()
    }

    fun onDeleteUserPasswordInput(text: String) {
        _state.value = state.value.copy(
            inputFields = state.value.inputFields.copy(deleteUserPasswordField = text),
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
        _toastMessage.emit("Name has been changed!")
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
        _toastMessage.emit("Password has been changed!")
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
        _toastMessage.emit("Account has been deleted")
    }
}
