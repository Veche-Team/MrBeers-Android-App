package com.example.neverpidor.presentation.fragments.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neverpidor.domain.use_cases.users.UserProfileUseCases
import com.example.neverpidor.presentation.fragments.register.util.RegisterState
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
    private val userProfileUseCases: UserProfileUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState> = _state

    private val _goBackEvent = MutableSharedFlow<String>()
    val goBackEvent: SharedFlow<String> = _goBackEvent

    fun onPhoneInput(text: String) {
        _state.value = state.value.copy(
            errors = state.value.errors.copy(
                numberError = if (text.isEmpty()) "Number can't be empty" else ""
            ),
            inputFields = state.value.inputFields.copy(
                number = text
            )
        )
        enableRegisterButton()
    }

    fun onNameInput(text: String) {
        _state.value = state.value.copy(
            errors = state.value.errors.copy(
                nameError = if (text.isEmpty()) "Name can't be empty" else ""
            ),
            inputFields = state.value.inputFields.copy(
                name = text
            )
        )
        enableRegisterButton()
    }

    fun onPasswordInput(text: String) {
        _state.value = state.value.copy(
            errors = state.value.errors.copy(
                passwordError = when {
                    text.isEmpty() -> "Password can't be empty"
                    text.length < 6 -> "Password's too short"
                    else -> ""
                }
            ),
            inputFields = state.value.inputFields.copy(
                password = text
            )
        )
        enableRegisterButton()
    }

    fun onRepeatPasswordInput(text: String) {
        _state.value = state.value.copy(
            inputFields = state.value.inputFields.copy(
                repeatPassword = text
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
                    phoneNumber = state.value.inputFields.number,
                    name = state.value.inputFields.name,
                    password = state.value.inputFields.password,
                    repeatPassword = state.value.inputFields.repeatPassword
                )
                _goBackEvent.emit("Account has been created!")
            } catch (e: PasswordException.RepeatPasswordException) {
                _state.emit(state.value.copy(
                    errors = state.value.errors.copy(
                        passwordRepeatError = e.message ?: ""
                    )
                ))
            } catch (e: NumberAlreadyExistsException) {
                _state.emit(state.value.copy(
                    errors = state.value.errors.copy(
                        numberError = e.message ?: ""
                    )
                ))
            }
        }
    }
}