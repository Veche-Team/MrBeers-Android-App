package com.example.neverpidor.presentation.fragments.itemlist

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neverpidor.data.providers.MenuCategory
import com.example.neverpidor.data.settings.AppSettings
import com.example.neverpidor.domain.repositories.MenuItemsRepository
import com.example.neverpidor.domain.use_cases.LikesUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuItemListViewModel @Inject constructor(
    private val menuRepository: MenuItemsRepository,
    private val appSettings: AppSettings,
    private val likesUseCases: LikesUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(MenuItemListState())
    val state: StateFlow<MenuItemListState> = _state

    private val _response = MutableSharedFlow<String>()
    val response: SharedFlow<String> = _response

    fun getItemList() {
        val category = getCategory()
        menuRepository.getDatabaseMenuItems().onEach {
            _state.value = state.value.copy(
                menuItems = it.filter { item ->
                    if (category == MenuCategory.BeerCategory)
                        item.category == MenuCategory.BeerCategory
                    else item.category == MenuCategory.SnackCategory
                }
            )
        }.launchIn(viewModelScope)
    }

    fun deleteItem(itemId: String) = viewModelScope.launch(Dispatchers.IO) {
        val category = getCategory()
        val response = if (category == MenuCategory.BeerCategory) {
            menuRepository.deleteApiBeer(itemId)?.msg
        } else menuRepository.deleteApiSnack(itemId)?.msg
        response?.let {
            menuRepository.deleteMenuItemFromDatabase(itemId)
            _response.emit(response)
        }
            ?: _response.emit("Проверьте подключение к интернету!")
    }

    fun getCategory(): MenuCategory {
        return appSettings.getCurrentCategory()
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
}