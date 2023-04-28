package com.example.neverpidor.presentation.fragments.itemlist

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neverpidor.data.cart.InCartItem
import com.example.neverpidor.data.providers.MenuCategory
import com.example.neverpidor.domain.model.User
import com.example.neverpidor.domain.use_cases.cart.CartUseCases
import com.example.neverpidor.domain.use_cases.likes.LikesUseCases
import com.example.neverpidor.domain.use_cases.menu_items.MenuItemsUseCases
import com.example.neverpidor.domain.use_cases.users.UserProfileUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    private lateinit var user: User

    fun getItemList() = viewModelScope.launch {
        user = userProfileUseCases.getUserUseCase()
        _state.emit(state.value.copy(errorState = false))
        val category = getCategory()
        try {
            menuItemsUseCases.getAllItemsUseCases(category).collect {
                val inCartItems = mutableListOf<InCartItem>()
                it.forEach { domainItem ->
                    inCartItems.add(
                        domainItem.toInCartItem(
                            quantity = cartUseCases.isItemInCartUseCase(
                                domainItem.UID,
                                user.phoneNumber
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

    fun deleteItem(itemId: String) = viewModelScope.launch(Dispatchers.IO) {
        val category = getCategory()
        _response.emit(menuItemsUseCases.deleteItemUseCase(category, itemId))
    }

    suspend fun getCategory(): MenuCategory =
        withContext(viewModelScope.coroutineContext) {
            menuItemsUseCases.getCurrentItemCategoryUseCase()
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
        cartUseCases.plusItemInCart(user.phoneNumber, inCartItem.UID, inCartItem.quantity)
        _state.emit(
            state.value.copy(
                inCartItems = state.value.inCartItems - inCartItem + inCartItem.copy(
                    quantity = inCartItem.quantity + 1
                )
            )
        )
    }

    fun minusCartItem(inCartItem: InCartItem) = viewModelScope.launch {
        cartUseCases.minusItemInCart(user.phoneNumber, inCartItem)
        _state.emit(
            state.value.copy(
                inCartItems = state.value.inCartItems - inCartItem + inCartItem.copy(
                    quantity = inCartItem.quantity - 1
                )
            )
        )
    }

    fun addItemToCart(id: String) = viewModelScope.launch {
        cartUseCases.addItemToCartUseCase(user.phoneNumber, id)
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