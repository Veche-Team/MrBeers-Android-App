package com.example.beers_app.presentation.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beers_app.data.settings.UserRoleTypeAdapter
import com.example.beers_app.domain.model.User
import com.example.beers_app.domain.use_cases.cart.CartUseCases
import com.example.beers_app.domain.use_cases.users.UserProfileUseCases
import com.example.beers_app.util.mapper.MenuItemMapper
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val userProfileUseCases: UserProfileUseCases,
    val cartUseCases: CartUseCases,
    val mapper: MenuItemMapper
) : ViewModel() {

    private val _state = MutableStateFlow(MainActivityState())
    val state: StateFlow<MainActivityState> = _state

    init {
        viewModelScope.launch {
            userProfileUseCases.addUserListenerUseCase().onEach {
                val gson = GsonBuilder().registerTypeAdapter(User.Role::class.java, UserRoleTypeAdapter())
                val user = gson.create().fromJson(it, User::class.java) ?: User(role = User.Role.NoUser)
                _state.value = state.value.copy(user = user)
                if (user.name != "") {
                    getUsersCart()
                }
            }.launchIn(viewModelScope)
        }
    }
    private fun getUsersCart() = viewModelScope.launch {
        cartUseCases.getUserCartFlowUseCase(state.value.user.phoneNumber).collect { inCartItems ->
            val list = cartUseCases.getCartListUseCase(inCartItems)
            _state.value = state.value.copy(
                inCartQuantity = list.sumOf { it.quantity },

            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            userProfileUseCases.setCurrentUserUseCase(User(role = User.Role.NoUser, name = ""))
            _state.emit(state.value.copy(user = User()))
        }
    }
}