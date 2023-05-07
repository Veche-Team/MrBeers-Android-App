package com.example.neverpidor.presentation.fragments.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neverpidor.domain.use_cases.user_validation.UserValidationUseCases
import com.example.neverpidor.domain.use_cases.users.UserProfileUseCases
import com.example.neverpidor.presentation.fragments.register.util.RegisterState
import com.example.neverpidor.util.InvalidNameException
import com.example.neverpidor.util.InvalidNumberException
import com.example.neverpidor.util.NumberAlreadyExistsException
import com.example.neverpidor.util.PasswordException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userProfileUseCases: UserProfileUseCases,
    private val userValidationUseCases: UserValidationUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState> = _state

    private val _goBackEvent = MutableSharedFlow<Boolean>()
    val goBackEvent: SharedFlow<Boolean> = _goBackEvent

    fun onPhoneInput(text: String) {
        try {
            userValidationUseCases.phoneNumberValidationUseCase(text)
            _state.value = state.value.copy(
                errors = state.value.errors.copy(
                    numberError = ""
                ),
                inputFields = state.value.inputFields.copy(
                    number = text
                )
            )
        } catch (e: InvalidNumberException) {
            _state.value = state.value.copy(
                errors = state.value.errors.copy(numberError = e.message ?: "")
            )
        }
        enableRegisterButton()
    }

    fun onNameInput(text: String) {
        try {
            userValidationUseCases.nameValidationUseCase(text)
            _state.value = state.value.copy(
                inputFields = state.value.inputFields.copy(name = text),
                errors = state.value.errors.copy(nameError = "")
            )
        } catch (e: InvalidNameException) {
            _state.value = state.value.copy(
                errors = state.value.errors.copy(nameError = e.message ?: "")
            )
        }
        enableRegisterButton()
    }

    fun onPasswordInput(text: String) {
        try {
            userValidationUseCases.passwordValidationUseCase(text)
            _state.value = state.value.copy(
                errors = state.value.errors.copy(
                    passwordError = ""
                ),
                inputFields = state.value.inputFields.copy(
                    password = text.toCharArray()
                )
            )
        } catch (e: PasswordException.NewPasswordException) {
            _state.value =
                state.value.copy(errors = state.value.errors.copy(passwordError = e.message ?: ""))
        }
        enableRegisterButton()
    }

    fun onRepeatPasswordInput(text: String) {
        _state.value = state.value.copy(
            inputFields = state.value.inputFields.copy(
                repeatPassword = text.toCharArray()
            ),
            errors = state.value.errors.copy(
                passwordRepeatError = ""
            )
        )
        enableRegisterButton()
    }

    private fun enableRegisterButton() {
        _state.value = state.value.copy(
            isButtonEnabled = state.value.inputFields.password.isNotEmpty() &&
                    state.value.inputFields.name.isNotEmpty() &&
                    state.value.inputFields.password.isNotEmpty() &&
                    state.value.inputFields.repeatPassword.isNotEmpty() &&
                    state.value.errors.numberError.isEmpty() &&
                    state.value.errors.nameError.isEmpty() &&
                    state.value.errors.passwordError.isEmpty() &&
                    state.value.errors.passwordRepeatError.isEmpty()
        )
    }

    fun register() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                userProfileUseCases.registerUserUseCase(
                    registerInputFields = state.value.inputFields
                )
                _goBackEvent.emit(true)
            } catch (e: PasswordException.RepeatPasswordException) {
                _state.emit(
                    state.value.copy(
                        errors = state.value.errors.copy(
                            passwordRepeatError = e.message ?: ""
                        )
                    )
                )
            } catch (e: NumberAlreadyExistsException) {
                _state.emit(
                    state.value.copy(
                        errors = state.value.errors.copy(
                            numberError = e.message ?: ""
                        )
                    )
                )
            }
        }
    }
}