package com.example.neverpidor.presentation.fragments.singleItem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neverpidor.data.providers.MenuCategory
import com.example.neverpidor.domain.model.DomainItem
import com.example.neverpidor.domain.repositories.MenuItemsRepository
import com.example.neverpidor.domain.use_cases.LikesUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SingleItemViewModel @Inject constructor(
    private val repository: MenuItemsRepository,
    private val likesUseCases: LikesUseCases,
) : ViewModel() {

    private val _state = MutableStateFlow(SingleItemState())
    val state: StateFlow<SingleItemState> = _state

    fun getMenuItemById(itemId: String) = viewModelScope.launch(Dispatchers.IO) {
        _state.value = state.value.copy(mainItem = repository.getMenuItemById(itemId))
        isItemLiked()
    }

    fun getItemsSet() = viewModelScope.launch(Dispatchers.IO) {
        val set = mutableSetOf<DomainItem>()
        val allItems = repository.getDatabaseMenuItems()
        allItems.collect {
            while (set.size < 3) {
                if (state.value.mainItem.category == MenuCategory.BeerCategory) {
                    set.add(it.filter { item -> item.category == MenuCategory.SnackCategory }
                        .random())
                } else {
                    set.add(it.filter { item -> item.category == MenuCategory.BeerCategory }
                        .random())
                }
            }
            _state.value = state.value.copy(itemsSet = set)
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
}