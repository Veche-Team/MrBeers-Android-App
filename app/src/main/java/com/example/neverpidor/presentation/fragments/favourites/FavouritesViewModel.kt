package com.example.neverpidor.presentation.fragments.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neverpidor.domain.model.InCartItem
import com.example.neverpidor.domain.use_cases.cart.CartUseCases
import com.example.neverpidor.domain.use_cases.likes.LikesUseCases
import com.example.neverpidor.domain.use_cases.menu_items.MenuItemsUseCases
import com.example.neverpidor.domain.use_cases.users.UserProfileUseCases
import com.example.neverpidor.presentation.fragments.favourites.util.FavouritesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val menuItemsUseCases: MenuItemsUseCases,
    private val likesUseCases: LikesUseCases,
    private val cartUseCases: CartUseCases,
    private val userProfileUseCases: UserProfileUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(FavouritesState())
    val state: StateFlow<FavouritesState> = _state

    fun getLikedItems() = viewModelScope.launch {
        val likes = likesUseCases.getLikesUseCase()
        _state.emit(
            state.value.copy(
                user = userProfileUseCases.getUserUseCase(),
                errorState = false
            )
        )
        try {
            val items = menuItemsUseCases.getAllItemsUseCases().first().filter {
                it.UID in likes
            }.sortedBy { it.category.name }
            val inCartItems = mutableListOf<InCartItem>()
            items.forEach { domainItem ->
                inCartItems.add(
                    domainItem.toInCartItem(
                        quantity = cartUseCases.isItemInCartUseCase(
                            domainItem.UID,
                            state.value.user.phoneNumber
                        )?.quantity ?: 0
                    )
                )
            }
            _state.emit(
                state.value.copy(
                    likedItems = items,
                    inCartItems = inCartItems
                )
            )
        } catch (e: Exception) {
            _state.emit(state.value.copy(errorState = true))
        }
    }

    fun likeOrDislike(domainItemId: String) = viewModelScope.launch {
        likesUseCases.likeOrDislikeUseCase(domainItemId, listOf(domainItemId))
        _state.emit(state.value.copy(likedItems = state.value.likedItems.filterNot { it.UID == domainItemId }))
    }

    fun plusCartItem(inCartItem: InCartItem) = viewModelScope.launch {
        cartUseCases.plusItemInCart(state.value.user.phoneNumber, inCartItem.UID, inCartItem.quantity)
        _state.emit(
            state.value.copy(
                inCartItems = state.value.inCartItems - inCartItem + inCartItem.copy(
                    quantity = inCartItem.quantity + 1
                )
            )
        )
    }

    fun minusCartItem(inCartItem: InCartItem) = viewModelScope.launch {
        cartUseCases.minusItemInCart(state.value.user.phoneNumber, inCartItem)
        _state.emit(
            state.value.copy(
                inCartItems = state.value.inCartItems - inCartItem + inCartItem.copy(
                    quantity = inCartItem.quantity - 1
                )
            )
        )
    }

    fun addItemToCart(id: String) = viewModelScope.launch {
        cartUseCases.addItemToCartUseCase(state.value.user.phoneNumber, id)
        val inCartItem = state.value.inCartItems.find { it.UID == id }!!
        _state.emit(
            state.value.copy(
                inCartItems = state.value.inCartItems - inCartItem + inCartItem.copy(quantity = inCartItem.quantity + 1)
            )
        )
    }
}