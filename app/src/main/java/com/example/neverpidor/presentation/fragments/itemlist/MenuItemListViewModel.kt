package com.example.neverpidor.presentation.fragments.itemlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neverpidor.data.providers.MenuCategory
import com.example.neverpidor.util.Event
import com.example.neverpidor.domain.model.DomainBeer
import com.example.neverpidor.domain.model.DomainSnack
import com.example.neverpidor.data.database.entities.MenuItemEntity
import com.example.neverpidor.data.network.dto.beer.BeerResponse
import com.example.neverpidor.data.network.dto.snack.SnackResponse
import com.example.neverpidor.data.settings.AppSettings
import com.example.neverpidor.domain.model.DomainItem
import com.example.neverpidor.domain.repository.MenuItemsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuItemListViewModel @Inject constructor(
    private val repository: MenuItemsRepository,
    private val appSettings: AppSettings
) : ViewModel() {

    private val _menuItems = MutableStateFlow<List<DomainItem>>(emptyList())
    val menuItems: StateFlow<List<DomainItem>> = _menuItems

    private val _beerResponse = MutableLiveData<Event<BeerResponse?>>()
    val beerResponse: LiveData<Event<BeerResponse?>> = _beerResponse

    private val _snackResponse = MutableLiveData<Event<SnackResponse?>>()
    val snackResponse: LiveData<Event<SnackResponse?>> = _snackResponse

    private val _shownEpoxyState = MutableLiveData<Set<String>>()
    val shownEpoxyState: LiveData<Set<String>> = _shownEpoxyState

    fun getSnacks() {
        repository.getDatabaseMenuItems().onEach {
            _menuItems.value = it.filter { item -> item.category == MenuCategory.SnackCategory }
        }.launchIn(viewModelScope)
    }

    fun getBeers() {
        repository.getDatabaseMenuItems().onEach {
            _menuItems.value = it.filter { item -> item.category == MenuCategory.BeerCategory }
        }.launchIn(viewModelScope)
    }

    fun deleteBeer(beerId: String) = viewModelScope.launch(Dispatchers.IO) {
        val response = repository.deleteApiBeer(beerId)
        response?.let {
            repository.deleteMenuItemFromDatabase(beerId)
            _beerResponse.postValue(Event(response))
        }
            ?: _beerResponse.postValue(Event(BeerResponse(msg = "Проверьте подключение к интернету!")))
    }

    fun deleteSnack(snackId: String) = viewModelScope.launch(Dispatchers.IO) {
        val response = repository.deleteApiSnack(snackId)
        response?.let {
            repository.deleteMenuItemFromDatabase(snackId)
            _snackResponse.postValue(Event(response))
        }
            ?: _snackResponse.postValue(Event(SnackResponse(msg = "Проверьте подключение к интернету!")))
    }

    fun getItem(): MenuCategory = appSettings.getCurrentItem()

    fun saveEpoxyState(set: Set<String>) {
        _shownEpoxyState.postValue(set)
    }

    fun faveSnack(domainSnack: DomainSnack) = viewModelScope.launch {
        repository.updateDatabaseMenuItem(
            itemEntity = MenuItemEntity(
                UID = domainSnack.UID,
                description = domainSnack.description,
                name = domainSnack.name,
                price = domainSnack.price,
                type = domainSnack.type,
                isFaved = !domainSnack.isFaved,
                category = MenuCategory.SnackCategory,
                alcPercentage = null,
                volume = null
            )
        )
    }

    fun faveBeer(domainBeer: DomainBeer) = viewModelScope.launch {
        repository.updateDatabaseMenuItem(
            itemEntity = MenuItemEntity(
                UID = domainBeer.UID,
                description = domainBeer.description,
                name = domainBeer.name,
                price = domainBeer.price,
                type = domainBeer.type,
                isFaved = !domainBeer.isFaved,
                alcPercentage = domainBeer.alcPercentage,
                volume = domainBeer.volume,
                category = MenuCategory.BeerCategory
            )
        )
    }
}