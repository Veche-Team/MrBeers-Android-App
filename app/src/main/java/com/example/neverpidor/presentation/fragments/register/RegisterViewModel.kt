package com.example.neverpidor.presentation.fragments.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neverpidor.data.database.entities.UserEntity
import com.example.neverpidor.data.settings.AppSettings
import com.example.neverpidor.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    val appSettings: AppSettings,
  //  val repository: UserRepository
) : ViewModel() {

    private val _registerErrors = MutableLiveData(RegisterErrors())
    val registerErrors: LiveData<RegisterErrors> = _registerErrors

    private val _inputFields = MutableLiveData(RegisterModel())
    val inputFields: LiveData<RegisterModel> = _inputFields

    private val _isButtonEnabled = MutableLiveData(false)
    val isButtonEnabled: LiveData<Boolean> = _isButtonEnabled

    private val _goBackEvent = MutableLiveData<Event<Boolean>>()
    val goBackEvent: LiveData<Event<Boolean>> = _goBackEvent

    fun onPhoneInput(text: String) {
        if (text.isEmpty()) {
            _registerErrors.value = registerErrors.value?.copy(numberError = "Number can't be empty")
        }  else _registerErrors.value = registerErrors.value?.copy(numberError = "")
        _inputFields.value = inputFields.value?.copy(number = text)
        enableRegisterButton()
    }

    fun onNameInput(text: String) {
        if (text.isEmpty()) {
            _registerErrors.value = registerErrors.value?.copy(nameError = "Name can't be empty")
        } else _registerErrors.value = registerErrors.value?.copy(nameError = "")
        _inputFields.value = inputFields.value?.copy(name = text)
        enableRegisterButton()
    }
    fun onPasswordInput(text: String) {
        if (text.isEmpty()) {
            _registerErrors.value = registerErrors.value?.copy(passwordError = "Password can't be empty")
        } else if (text.length < 6) {
            _registerErrors.value = registerErrors.value?.copy(passwordError = "Password's too short")
        } else _registerErrors.value = registerErrors.value?.copy(passwordError = "")
        _inputFields.value = inputFields.value?.copy(password = text)
        enableRegisterButton()
    }
    fun onRepeatPasswordInput(text: String) {
        _inputFields.value = inputFields.value?.copy(repeatPassword = text)
        _registerErrors.value = registerErrors.value?.copy(passwordRepeatError = "")
        enableRegisterButton()
    }

    private fun enableRegisterButton() {
        _isButtonEnabled.value = inputFields.value!!.password.isNotEmpty() &&
                inputFields.value!!.name.isNotEmpty() &&
                inputFields.value!!.password.isNotEmpty() &&
                inputFields.value!!.repeatPassword.isNotEmpty() &&
                registerErrors.value!!.numberError.isEmpty() &&
                registerErrors.value!!.nameError.isEmpty() &&
                registerErrors.value!!.passwordError.isEmpty() &&
                registerErrors.value!!.passwordRepeatError.isEmpty()
    }
    fun register() {
        if (inputFields.value?.password != inputFields.value?.repeatPassword) {
            _registerErrors.value = registerErrors.value?.copy(passwordRepeatError = "Passwords should match")
            return
        }
        val user = UserEntity(
            phoneNumber = inputFields.value!!.number.toLong(),
            name = inputFields.value!!.name,
            password = inputFields.value!!.password
        )
        viewModelScope.launch {
       //     repository.addUser(user)
        }
        _goBackEvent.value = Event(true)
    }

    fun validateRegisterInput(registerModel: RegisterModel) {
        if (registerModel.number.isEmpty()) {
            _registerErrors.value = registerErrors.value?.copy(numberError = "Number can't be empty")
        }
        if (registerModel.name.isEmpty()) {
            _registerErrors.value = registerErrors.value?.copy(nameError = "Name can't be empty")
        }
        if (registerModel.password.isEmpty()) {
            _registerErrors.value = registerErrors.value?.copy(passwordError = "Password can't be empty")
            return
        }
        if (registerModel.password.length < 6) {
            _registerErrors.value = registerErrors.value?.copy(passwordError = "Password's too short")
        }
        if (registerModel.repeatPassword != registerModel.password) {
            _registerErrors.value = registerErrors.value?.copy(passwordRepeatError = "Passwords not matching")
        }
    }
}