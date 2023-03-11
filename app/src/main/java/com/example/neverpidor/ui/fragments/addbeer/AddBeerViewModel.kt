package com.example.neverpidor.ui.fragments.addbeer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neverpidor.Event
import com.example.neverpidor.Repositories
import com.example.neverpidor.data.MenuItemsRepository
import com.example.neverpidor.model.network.beer.Beer
import com.example.neverpidor.model.network.beer.BeerResponse
import com.example.neverpidor.model.network.beer.BeerRequest
import com.example.neverpidor.model.network.snack.Snack
import com.example.neverpidor.model.network.snack.SnackResponse
import com.example.neverpidor.model.network.snack.SnackRequest
import com.example.neverpidor.model.settings.AppSettings
import kotlinx.coroutines.launch
import retrofit2.Response

class AddBeerViewModel: ViewModel() {

    private val appSettings: AppSettings by lazy {
        Repositories.appSettings
    }

    private val repository = MenuItemsRepository()

    private val _beerLiveData = MutableLiveData<Beer>()
    val beerLiveData: LiveData<Beer> = _beerLiveData

    private val _snackLiveData = MutableLiveData<Snack>()
    val snackLiveData: LiveData<Snack> = _snackLiveData

    private val _beerResponse = MutableLiveData<Event<BeerResponse?>>()
    val beerResponse: LiveData<Event<BeerResponse?>> = _beerResponse

    private val _snackResponse = MutableLiveData<Event<SnackResponse?>>()
    val snackResponse: LiveData<Event<SnackResponse?>> = _snackResponse

    fun addBeer(beerRequest: BeerRequest) = viewModelScope.launch {
        _beerResponse.value = Event(repository.addBeer(beerRequest))
    }
    fun addSnack(snackRequest: SnackRequest) = viewModelScope.launch {
        _snackResponse.postValue(
            Event(repository.addSnack(snackRequest))
        )
    }

    fun getBeerById(beerId: String) = viewModelScope.launch {
        _beerLiveData.postValue(repository.getBeerById(beerId))
    }

    fun getSnackById(snackId: String) = viewModelScope.launch {
        _snackLiveData.postValue(repository.getSnackById(snackId))
    }

    fun updateBeer(beerId: String, beerRequest: BeerRequest) = viewModelScope.launch {
        _beerResponse.postValue(Event(repository.updateBeer(beerId, beerRequest)))
    }

    fun updateSnack(snackId: String, snackRequest: SnackRequest) = viewModelScope.launch {
        _snackResponse.postValue(Event(repository.updateSnack(snackId, snackRequest)))
    }

    fun getItem(): Int = appSettings.getCurrentItem()
}