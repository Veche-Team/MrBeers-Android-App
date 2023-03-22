package com.example.neverpidor.ui.fragments.itemlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neverpidor.util.Event
import com.example.neverpidor.data.MenuItemsRepository
import com.example.neverpidor.model.domain.DomainBeer
import com.example.neverpidor.model.domain.DomainSnack
import com.example.neverpidor.model.mapper.SnackMapper
import com.example.neverpidor.model.network.beer.BeerResponse
import com.example.neverpidor.model.network.snack.SnackList
import com.example.neverpidor.model.network.snack.SnackResponse
import com.example.neverpidor.model.settings.AppSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuItemListViewModel @Inject constructor(
    private val repository: MenuItemsRepository,
    private val appSettings: AppSettings,
    private val snackMapper: SnackMapper
) : ViewModel() {

    init {
        refillSnacks()
    }

    private val _snacks = MutableLiveData<List<DomainSnack>>()
    val snacks: LiveData<List<DomainSnack>> = _snacks

    private val _beers = MutableLiveData<List<DomainBeer>>()
    val beers: LiveData<List<DomainBeer>> = _beers

    private val _beerResponse = MutableLiveData<Event<BeerResponse?>>()
    val beerResponse: LiveData<Event<BeerResponse?>> = _beerResponse

    private val _snackResponse = MutableLiveData<Event<SnackResponse?>>()
    val snackResponse: LiveData<Event<SnackResponse?>> = _snackResponse

    private val _shownEpoxyState = MutableLiveData<Set<String>>()
    val shownEpoxyState: LiveData<Set<String>> = _shownEpoxyState

    fun getSnacks() = viewModelScope.launch(Dispatchers.IO) {
        repository.getDatabaseSnacks().collect {
            _snacks.postValue(it.map { snackMapper.buildDomainFromEntity(it) })
        }
    }

    fun getBeers() = viewModelScope.launch(Dispatchers.IO) {
        _beers.postValue(repository.getBeers())
    }

    fun deleteBeer(beerId: String) = viewModelScope.launch(Dispatchers.IO) {
        _beerResponse.postValue(Event(repository.deleteBeer(beerId)))
    }

    fun deleteSnack(snackId: String) = viewModelScope.launch(Dispatchers.IO) {
        _snackResponse.postValue(Event(repository.deleteSnack(snackId)))
        delay(1000)
        refillSnacks()
    }

    fun getItem(): Int = appSettings.getCurrentItem()

    fun saveEpoxyState(set: Set<String>) {
        _shownEpoxyState.postValue(set)
    }

    fun refillSnacks() = viewModelScope.launch(Dispatchers.IO) {
        val snacks = repository.getSnacks()!!
        repository.fillDatabaseWithSnacks(snacks.data.map {
            snackMapper.buildEntityFromNetwork(it)
        })
    }
}