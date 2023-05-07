package com.example.neverpidor.presentation.fragments.singleItem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neverpidor.domain.model.InCartItem
import com.example.neverpidor.domain.use_cases.cart.CartUseCases
import com.example.neverpidor.domain.use_cases.likes.LikesUseCases
import com.example.neverpidor.domain.use_cases.menu_items.MenuItemsUseCases
import com.example.neverpidor.domain.use_cases.users.UserProfileUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SingleItemViewModel @Inject constructor(
    private val likesUseCases: LikesUseCases,
    private val menuItemsUseCases: MenuItemsUseCases,
    private val userProfileUseCases: UserProfileUseCases,
    private val cartUseCases: CartUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(SingleItemState())
    val state: StateFlow<SingleItemState> = _state

    private val _likes = MutableStateFlow(0)
    val likes: StateFlow<Int> = _likes

    fun getMenuItemById(itemId: String) = viewModelScope.launch(Dispatchers.IO) {
        val item = menuItemsUseCases.getMenuItemByIdUseCase(itemId)
        val user = userProfileUseCases.getUserUseCase()
        _state.emit(
            state.value.copy(
                mainItem = item,
                inCartItem = item.toInCartItem(),
                user = user
            )
        )
        launch {
            getItemsSet()
        }
        isItemLiked()
        isItemInCart()
        likesUseCases.getItemLikesByIdUseCase(itemId).collect {
            _likes.emit(it)
        }
    }

    private fun getItemsSet() = viewModelScope.launch(Dispatchers.IO) {
        val allItems = menuItemsUseCases.getAllItemsUseCases()
        allItems.collect {
            val set = menuItemsUseCases.getItemsSetUseCase(it, state.value.mainItem.category)
            val inCartItems = mutableListOf<InCartItem>()
            set.forEach { domainItem ->
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
                    itemsSet = set,
                    inCartItemsSet = inCartItems
                )
            )
            val list = mutableListOf<String>()
            set.forEach { item ->
                val isLiked = likesUseCases.isItemLikedUseCase(item.UID)
                if (isLiked != null) {
                    list.add(item.UID)
                }
            }
            _state.value = state.value.copy(likedItems = list)
        }
    }

    private fun isItemLiked() = viewModelScope.launch {
        val like = likesUseCases.isItemLikedUseCase(state.value.mainItem.UID)
        like?.let {
            _state.value = state.value.copy(isMainItemLiked = true)
        } ?: kotlin.run {
            _state.value = state.value.copy(isMainItemLiked = false)
        }
    }

    fun likeOrDislikeMainItem() = viewModelScope.launch {
        val list = likesUseCases.likeOrDislikeUseCase(
            state.value.mainItem.UID,
            listOf(state.value.mainItem.UID)
        )
        if (list.isEmpty()) {
            _state.value = state.value.copy(isMainItemLiked = false)
        } else {
            _state.value = state.value.copy(isMainItemLiked = true)
        }
    }

    fun likeOrDislikeItemInSet(domainItemId: String) = viewModelScope.launch {
        _state.value = state.value.copy(
            likedItems = likesUseCases.likeOrDislikeUseCase(
                domainItemId,
                state.value.likedItems
            )
        )
    }

    private fun isItemInCart() = viewModelScope.launch {
        val currentUserNumber = state.value.user.phoneNumber
        val isInCart = cartUseCases.isItemInCartUseCase(state.value.mainItem.UID, currentUserNumber)
        isInCart?.let {
            _state.emit(
                state.value.copy(
                    isMainItemInCart = true,
                    inCartItem = state.value.inCartItem.copy(quantity = it.quantity)
                )
            )
        } ?: _state.emit(
            state.value.copy(
                isMainItemInCart = false,
                inCartItem = state.value.inCartItem.copy(quantity = 0)
            )
        )
    }

    fun addToCart() = viewModelScope.launch {
        _state.emit(
            state.value.copy(
                isMainItemInCart = !state.value.isMainItemInCart,
                inCartItem = state.value.inCartItem.copy(quantity = state.value.inCartItem.quantity + 1)
            )
        )
        val currentUser = state.value.user
        val id = state.value.mainItem.UID
        cartUseCases.addItemToCartUseCase(currentUser.phoneNumber, id)
    }

    fun plusItemInCart() = viewModelScope.launch {
        val currentUser = state.value.user
        cartUseCases.plusItemInCart(
            currentUser.phoneNumber,
            state.value.inCartItem.UID,
            state.value.inCartItem.quantity
        )
        _state.emit(state.value.copy(inCartItem = state.value.inCartItem.copy(quantity = state.value.inCartItem.quantity + 1)))
    }

    fun minusItemInCart() = viewModelScope.launch {
        val currentUser = state.value.user
        cartUseCases.minusItemInCart(currentUser.phoneNumber, state.value.inCartItem)
        _state.emit(state.value.copy(inCartItem = state.value.inCartItem.copy(quantity = state.value.inCartItem.quantity - 1)))
    }
    fun plusCartItem(inCartItem: InCartItem) = viewModelScope.launch {
        cartUseCases.plusItemInCart(state.value.user.phoneNumber, inCartItem.UID, inCartItem.quantity)
        _state.emit(
            state.value.copy(
                inCartItemsSet = state.value.inCartItemsSet - inCartItem + inCartItem.copy(
                    quantity = inCartItem.quantity + 1
                )
            )
        )
    }

    fun minusCartItem(inCartItem: InCartItem) = viewModelScope.launch {
        cartUseCases.minusItemInCart(state.value.user.phoneNumber, inCartItem)
        _state.emit(
            state.value.copy(
                inCartItemsSet = state.value.inCartItemsSet - inCartItem + inCartItem.copy(
                    quantity = inCartItem.quantity - 1
                )
            )
        )
    }

    fun addItemToCart(id: String) = viewModelScope.launch {
        cartUseCases.addItemToCartUseCase(state.value.user.phoneNumber, id)
        val inCartItem = state.value.inCartItemsSet.find { it.UID == id }!!
        _state.emit(
            state.value.copy(
                inCartItemsSet = state.value.inCartItemsSet - inCartItem + inCartItem.copy(
                    quantity = inCartItem.quantity + 1
                )
            )
        )
    }
}