package com.example.neverpidor.presentation.fragments.itemlist

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neverpidor.domain.model.InCartItem
import com.example.neverpidor.data.providers.MenuCategory
import com.example.neverpidor.domain.use_cases.cart.CartUseCases
import com.example.neverpidor.domain.use_cases.likes.LikesUseCases
import com.example.neverpidor.domain.use_cases.menu_items.MenuItemsUseCases
import com.example.neverpidor.domain.use_cases.users.UserProfileUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuItemListViewModel @Inject constructor(
    private val likesUseCases: LikesUseCases,
    private val menuItemsUseCases: MenuItemsUseCases,
    private val userProfileUseCases: UserProfileUseCases,
    private val cartUseCases: CartUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(MenuItemListState())
    val state: StateFlow<MenuItemListState> = _state

    private val _response = MutableSharedFlow<String>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val response: SharedFlow<String> = _response

    fun getItemList(category: MenuCategory) = viewModelScope.launch {
        launch {
            _state.emit(state.value.copy(errorState = false, user = userProfileUseCases.getUserUseCase()))
        }.join()
        launch {
            try {
                menuItemsUseCases.getAllItemsUseCases(category).collect {
                    val inCartItems = mutableListOf<InCartItem>()
                    it.forEach { domainItem ->
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
                            menuItems = it,
                            inCartItems = inCartItems
                        )
                    )
                }
            } catch (e: Exception) {
                _state.emit(state.value.copy(errorState = true))
            }
        }
    }

    fun deleteItem(itemId: String, category: MenuCategory) = viewModelScope.launch(Dispatchers.IO) {

        _response.emit(menuItemsUseCases.deleteItemUseCase(category, itemId))
        val item = state.value.menuItems.find { it.UID == itemId } ?: return@launch
        _state.emit(state.value.copy(menuItems = state.value.menuItems - item))
    }

    fun getLikes() = viewModelScope.launch {
        _state.emit(
            state.value.copy(
                likedItems = likesUseCases.getLikesUseCase()
            )
        )
    }

    fun isConnected(activity: Activity): Boolean {
        val connectivityManager =
            activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities =
            connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
    }

    fun likeOrDislike(domainItemId: String) = viewModelScope.launch {
        _state.emit(
            state.value.copy(
                likedItems = likesUseCases.likeOrDislikeUseCase(
                    domainItemId,
                    state.value.likedItems
                )
            )
        )
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
                inCartItems = state.value.inCartItems - inCartItem + inCartItem.copy(
                    quantity = inCartItem.quantity + 1
                )
            )
        )
    }
}