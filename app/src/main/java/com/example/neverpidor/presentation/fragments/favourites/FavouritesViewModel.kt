package com.example.neverpidor.presentation.fragments.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neverpidor.domain.model.DomainItem
import com.example.neverpidor.domain.use_cases.likes.LikesUseCases
import com.example.neverpidor.domain.use_cases.menu_items.MenuItemsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val menuItemsUseCases: MenuItemsUseCases,
    private val likesUseCases: LikesUseCases
) : ViewModel() {

    private val _likedItems = MutableStateFlow<List<DomainItem>>(emptyList())
    val likedItems: StateFlow<List<DomainItem>> = _likedItems

    private val _errorState = MutableStateFlow(false)
    val errorState: StateFlow<Boolean> = _errorState

    fun getLikedItems() = viewModelScope.launch {
        val likes = likesUseCases.getLikesUseCase()
        _errorState.emit(false)
        try {
            val items = menuItemsUseCases.getAllItemsUseCases().first().filter {
                it.UID in likes
            }.sortedBy { it.category.name }
            _likedItems.emit(items)
        } catch (e: Exception) {
            _errorState.emit(true)
        }
    }

    fun likeOrDislike(domainItemId: String) = viewModelScope.launch {
        likesUseCases.likeOrDislikeUseCase(domainItemId, listOf(domainItemId))
        _likedItems.emit(
            likedItems.value.filterNot { it.UID == domainItemId }
        )
    }
}