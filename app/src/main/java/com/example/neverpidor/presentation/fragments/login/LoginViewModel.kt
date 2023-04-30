package com.example.neverpidor.presentation.fragments.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neverpidor.domain.use_cases.users.UserProfileUseCases
import com.example.neverpidor.presentation.fragments.login.util.LoginState
import com.example.neverpidor.util.PasswordException
import com.example.neverpidor.util.UserDoesntExistException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userProfileUseCases: UserProfileUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state

    private val _loginEvent = MutableSharedFlow<String>()
    val loginEvent: SharedFlow<String> = _loginEvent

    fun onNumberInput(text: String) {
        _state.value = state.value.copy(
            inputFields = state.value.inputFields.copy(
                number = text
            ),
            errors = state.value.errors.copy(
                numberError = ""
            )
        )
        enableLoginButton()
    }

    fun onPasswordInput(text: String) {
        _state.value = state.value.copy(
            inputFields = state.value.inputFields.copy(
                password = text.toCharArray()
            ),
            errors = state.value.errors.copy(
                passwordError = ""
            )
        )
        enableLoginButton()
    }

    private fun enableLoginButton() {
        _state.value = state.value.copy(
            isButtonEnabled = state.value.inputFields.number.isNotEmpty()
                    && state.value.inputFields.password.isNotEmpty()
        )
    }

    fun login() = viewModelScope.launch {
        try {
            val user = userProfileUseCases.findUserByNumberUseCase(
                state.value.inputFields.number,
                state.value.inputFields.password
            )
            userProfileUseCases.setCurrentUserUseCase(user)
            _loginEvent.emit(user.name)
        } catch (e: UserDoesntExistException) {
            _state.emit(
                state.value.copy(
                    errors = state.value.errors.copy(
                        numberError = e.message ?: ""
                    )
                )
            )
        } catch (e: PasswordException) {
            _state.emit(
                state.value.copy(
                    errors = state.value.errors.copy(
                        passwordError = e.message ?: ""
                    )
                )
            )
        }
    }
}