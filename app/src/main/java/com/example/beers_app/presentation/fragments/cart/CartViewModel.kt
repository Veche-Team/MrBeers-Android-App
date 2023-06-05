package com.example.beers_app.presentation.fragments.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beers_app.domain.model.InCartItem
import com.example.beers_app.domain.model.User
import com.example.beers_app.domain.use_cases.cart.CartUseCases
import com.example.beers_app.domain.use_cases.users.UserProfileUseCases
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

    private val _price = MutableStateFlow(0.0)
    val price: StateFlow<Double> = _price

    private val _isButtonEnabled = MutableStateFlow(false)
    val isButtonEnabled: StateFlow<Boolean> = _isButtonEnabled

    private lateinit var user: User

    fun getUsersCart() = viewModelScope.launch {
        user = userProfileUseCases.getUserUseCase()
        cartUseCases.getUserCartFlowUseCase(user.phoneNumber).collect {
            val list = cartUseCases.getCartListUseCase(it)
            _state.emit(list)
            _price.emit(list.sumOf { inCartItem -> inCartItem.price * inCartItem.quantity })
        }
    }

    fun addItem(inCartItem: InCartItem) = viewModelScope.launch {
        cartUseCases.plusItemInCart(user.phoneNumber, inCartItem.UID, inCartItem.quantity)
    }

    fun removeItem(inCartItem: InCartItem) = viewModelScope.launch {
        cartUseCases.minusItemInCart(user.phoneNumber, inCartItem)
    }

    fun onOrderClick() = viewModelScope.launch {
        cartUseCases.clearUserCart(user.phoneNumber)
    }
}