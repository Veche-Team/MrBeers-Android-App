package com.example.neverpidor.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neverpidor.data.database.entities.UserEntity
import com.example.neverpidor.domain.model.User
import com.example.neverpidor.domain.use_cases.users.UserProfileUseCases
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val userProfileUseCases: UserProfileUseCases
) : ViewModel() {

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName

    init {
        viewModelScope.launch {
            userProfileUseCases.addUserListenerUseCase().onEach {
                val user = GsonBuilder().create().fromJson(it, UserEntity::class.java) ?: UserEntity()
                _userName.emit(user.name)
            }.launchIn(viewModelScope)
        }
    }

    fun logout() {
        viewModelScope.launch {
            userProfileUseCases.setCurrentUserUseCase(User(role = User.Role.NoUser))
        }
    }
}