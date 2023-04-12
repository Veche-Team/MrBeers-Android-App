package com.example.neverpidor.presentation.fragments.itemlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neverpidor.data.providers.MenuCategory
import com.example.neverpidor.util.Event
import com.example.neverpidor.domain.model.DomainBeer
import com.example.neverpidor.domain.model.DomainSnack
import com.example.neverpidor.data.database.entities.BeerEntity
import com.example.neverpidor.data.database.entities.SnackEntity
import com.example.neverpidor.data.network.dto.beer.BeerResponse
import com.example.neverpidor.data.network.dto.snack.SnackResponse
import com.example.neverpidor.data.settings.AppSettings
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

    private val _snacks = MutableLiveData<List<DomainSnack>>()
    val snacks: LiveData<List<DomainSnack>> = _snacks

    private val _beers = MutableStateFlow<List<DomainBeer>>(emptyList())
    val beers: StateFlow<List<DomainBeer>> = _beers

    private val _beerResponse = MutableLiveData<Event<BeerResponse?>>()
    val beerResponse: LiveData<Event<BeerResponse?>> = _beerResponse

    private val _snackResponse = MutableLiveData<Event<SnackResponse?>>()
    val snackResponse: LiveData<Event<SnackResponse?>> = _snackResponse

    private val _shownEpoxyState = MutableLiveData<Set<String>>()
    val shownEpoxyState: LiveData<Set<String>> = _shownEpoxyState

    fun getSnacks() = viewModelScope.launch(Dispatchers.IO) {
        repository.getDatabaseSnacks().collect {
            _snacks.postValue(it)
        }
    }

    fun getBeers() {
        repository.getDatabaseBeers().onEach {
            _beers.value = it
        }.launchIn(viewModelScope)
    }

    fun deleteBeer(beerId: String) = viewModelScope.launch(Dispatchers.IO) {
        val response = repository.deleteApiBeer(beerId)
        response?.let {
            repository.deleteBeerFromDatabase(beerId)
            _beerResponse.postValue(Event(response))
        }
            ?: _beerResponse.postValue(Event(BeerResponse(msg = "Проверьте подключение к интернету!")))
    }

    fun deleteSnack(snackId: String) = viewModelScope.launch(Dispatchers.IO) {
        val response = repository.deleteApiSnack(snackId)
        response?.let {
            repository.deleteSnackFromDatabase(snackId)
            _snackResponse.postValue(Event(response))
        }
            ?: _snackResponse.postValue(Event(SnackResponse(msg = "Проверьте подключение к интернету!")))
    }

    fun getItem(): MenuCategory = appSettings.getCurrentItem()

    fun saveEpoxyState(set: Set<String>) {
        _shownEpoxyState.postValue(set)
    }

    fun faveSnack(domainSnack: DomainSnack) = viewModelScope.launch {
        repository.updateDatabaseSnack(
            snackEntity = SnackEntity(
                UID = domainSnack.UID,
                description = domainSnack.description,
                name = domainSnack.name,
                price = domainSnack.price,
                type = domainSnack.type,
                isFaved = !domainSnack.isFaved
            )
        )
    }

    fun faveBeer(domainBeer: DomainBeer) = viewModelScope.launch {
        repository.updateDatabaseBeer(
            beerEntity = BeerEntity(
                UID = domainBeer.UID,
                description = domainBeer.description,
                name = domainBeer.name,
                price = domainBeer.price,
                type = domainBeer.type,
                isFaved = !domainBeer.isFaved,
                alcPercentage = domainBeer.alcPercentage,
                volume = domainBeer.volume
            )
        )
    }
}