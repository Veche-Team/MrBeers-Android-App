package com.example.neverpidor.presentation.fragments.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neverpidor.data.cart.InCartItem
import com.example.neverpidor.data.database.entities.UserEntity
import com.example.neverpidor.domain.use_cases.cart.CartUseCases
import com.example.neverpidor.domain.use_cases.users.UserProfileUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val userProfileUseCases: UserProfileUseCases,
    private val cartUseCases: CartUseCases
) : ViewModel() {

    private val _state = MutableStateFlow<List<InCartItem>>(emptyList())
    val state: StateFlow<List<InCartItem>> = _state

    private val _price = MutableStateFlow(0)
    val price: StateFlow<Int> = _price

    private lateinit var user: UserEntity

    fun getUsersCart() = viewModelScope.launch {
        user = userProfileUseCases.getUserUseCase()
        cartUseCases.getUserCartFlowUseCase(user).collect {
            val list = cartUseCases.getCartListUseCase(it)
            _state.emit(list)
            _price.emit(list.sumOf { inCartItem -> inCartItem.price * inCartItem.quantity }.toInt())
        }
    }

    fun addItem(inCartItem: InCartItem) = viewModelScope.launch {
        cartUseCases.plusItemInCart(user, inCartItem.UID, inCartItem.quantity)
    }

    fun removeItem(inCartItem: InCartItem) = viewModelScope.launch {
        cartUseCases.minusItemInCart(user, inCartItem)
    }

    fun onOrderClick() = viewModelScope.launch {
        cartUseCases.clearUserCart(user)
    }
}