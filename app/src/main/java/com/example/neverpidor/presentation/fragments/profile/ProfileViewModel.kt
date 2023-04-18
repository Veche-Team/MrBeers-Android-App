package com.example.neverpidor.presentation.fragments.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neverpidor.data.database.entities.UserEntity
import com.example.neverpidor.data.settings.AppSettings
import com.example.neverpidor.domain.repositories.UserRepository
import com.example.neverpidor.presentation.fragments.profile.util.ProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val appSettings: AppSettings,
    private val repository: UserRepository
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

    fun getName() = appSettings.getCurrentUser().name

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
            fieldErrors = if (text.length < 6) state.value.fieldErrors.copy(newPasswordError = "password's too short")
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
        val user = appSettings.getCurrentUser()
        Log.e("NAME", user.toString())
        user.let {
            val newUser = it.copy(name = state.value.inputFields.changeNameField)
            repository.updateUser(newUser)
            appSettings.setCurrentUser(newUser)
            _toastMessage.emit("Name has been changed!")
        }
        val another = appSettings.getCurrentUser()
        Log.e("NAME", another.toString())
    }

    fun changePassword() = viewModelScope.launch(Dispatchers.IO) {
        if (state.value.inputFields.oldPasswordField == state.value.inputFields.newPasswordField) {
            _state.emit(
                state.value.copy(
                    fieldErrors = state.value.fieldErrors.copy(
                        newPasswordError = "New password is the same as old"
                    )
                )
            )
            return@launch
        }
        if (state.value.inputFields.newPasswordField != state.value.inputFields.repeatNewPasswordField) {
            _state.emit(
                state.value.copy(
                    fieldErrors = state.value.fieldErrors.copy(
                        repeatNewPasswordError = "Passwords should match"
                    )
                )
            )
            return@launch
        }

        val user = appSettings.getCurrentUser()
        user.let {
            if (it.password != state.value.inputFields.oldPasswordField) {
                _state.emit(
                    state.value.copy(
                        fieldErrors = state.value.fieldErrors.copy(
                            oldPasswordError = "Wrong password"
                        )
                    )
                )
                return@launch
            }
            val newUser = it.copy(password = state.value.inputFields.newPasswordField)
            Log.e("PASS", newUser.toString())
            repository.updateUser(newUser)
            appSettings.setCurrentUser(newUser)
            _toastMessage.emit("Password has been changed!")
            _state.emit(state.value.copy(
                inputFields = state.value.inputFields.copy(
                    oldPasswordField = "",
                    newPasswordField = "",
                    repeatNewPasswordField = ""
                )
            ))
        }
    }

    fun deleteUser() = viewModelScope.launch {
        val user = appSettings.getCurrentUser()
        user.let {
            Log.e("DELETE", it.password)
            if (it.password != state.value.inputFields.deleteUserPasswordField) {
                _state.emit(
                    state.value.copy(
                        fieldErrors = state.value.fieldErrors.copy(
                            deleteUserPasswordError = "Wrong password"
                        )
                    )
                )
                return@launch
            }
            repository.deleteUser(user)
            appSettings.setCurrentUser(UserEntity())
            _toastMessage.emit("Account has been deleted")
        }
    }
}