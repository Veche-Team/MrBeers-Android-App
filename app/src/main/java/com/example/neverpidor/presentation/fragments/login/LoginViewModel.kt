package com.example.neverpidor.presentation.fragments.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neverpidor.data.settings.AppSettings
import com.example.neverpidor.domain.repositories.UserRepository
import com.example.neverpidor.presentation.fragments.login.util.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val appSettings: AppSettings,
    val repository: UserRepository
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
                password = text
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
        val users = repository.getAllUsers()
        val user = users.find {
            it.phoneNumber.contains(state.value.inputFields.number)
        }
        if (user == null) {
            _state.emit(
                state.value.copy(
                    errors = state.value.errors.copy(
                        numberError = "User with this number doesn't exist"
                    )
                )
            )
            return@launch
        }
        if (user.password != state.value.inputFields.password) {
            _state.emit(
                state.value.copy(
                    errors = state.value.errors.copy(
                        passwordError = "Wrong password"
                    )
                )
            )
            return@launch
        }
        appSettings.setCurrentUser(user)
        _loginEvent.emit(user.name)
    }
}