package com.example.neverpidor.ui.fragments.addbeer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neverpidor.data.Category
import com.example.neverpidor.data.repositories.MenuItemsRepository
import com.example.neverpidor.model.domain.DomainBeer
import com.example.neverpidor.model.domain.DomainSnack
import com.example.neverpidor.model.network.beer.BeerResponse
import com.example.neverpidor.model.network.beer.BeerRequest
import com.example.neverpidor.model.network.snack.SnackResponse
import com.example.neverpidor.model.network.snack.SnackRequest
import com.example.neverpidor.model.settings.AppSettings
import com.example.neverpidor.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddBeerViewModel @Inject constructor(
    private val repository: MenuItemsRepository,
    private val appSettings: AppSettings
) : ViewModel() {

    private val _beerLiveData = MutableLiveData<DomainBeer>()
    val beerLiveData: LiveData<DomainBeer> = _beerLiveData

    private val _snackLiveData = MutableLiveData<DomainSnack>()
    val snackLiveData: LiveData<DomainSnack> = _snackLiveData

    private val _beerResponse = MutableLiveData<Event<BeerResponse?>>()
    val beerResponse: LiveData<Event<BeerResponse?>> = _beerResponse

    private val _snackResponse = MutableLiveData<Event<SnackResponse?>>()
    val snackResponse: LiveData<Event<SnackResponse?>> = _snackResponse

    fun getBeerById(beerId: String) = viewModelScope.launch(Dispatchers.IO) {
        _beerLiveData.postValue(repository.getBeerById(beerId))
    }

    fun getSnackById(snackId: String) = viewModelScope.launch(Dispatchers.IO) {
        _snackLiveData.postValue(repository.getSnackById(snackId))
    }

    fun getItem(): Category = appSettings.getCurrentItem()

    fun handleInput(
        validationModel: ValidationModel,
        itemId: String? = null
    ): Boolean {
        validationModel.alc?.let {
            val beerRequest = BeerRequest(
                validationModel.alc.toDouble(),
                validationModel.description,
                validationModel.title,
                validationModel.price.toDouble(),
                validationModel.type,
                validationModel.volume!!.toDouble()
            )
            if (itemId != null) {
                updateBeer(itemId, beerRequest)
            } else {
                addBeer(beerRequest)
            }
            return true
        }
        val snackRequest = SnackRequest(
            validationModel.description,
            validationModel.title,
            validationModel.price.toDouble(),
            validationModel.type
        )
        if (itemId != null) {
            updateSnack(itemId, snackRequest)
        } else {
            addSnack(snackRequest)
        }
        return true
    }

    private fun addBeer(beerRequest: BeerRequest) = viewModelScope.launch(Dispatchers.IO) {
        val response = repository.addBeer(beerRequest)
        response?.let {
            repository.addBeerToDatabase(it)
            _beerResponse.postValue(Event(response))
        }
            ?: _beerResponse.postValue(Event(BeerResponse(msg = "Проверьте подключение к интернету!")))
    }

    private fun addSnack(snackRequest: SnackRequest) = viewModelScope.launch(Dispatchers.IO) {
        val response = repository.addSnack(snackRequest)
        response?.let {
            repository.addSnackToDatabase(it)
            _snackResponse.postValue(Event(response))
        }
            ?: _snackResponse.postValue(Event(SnackResponse(msg = "Проверьте подключение к интернету!")))
    }

    private fun updateBeer(beerId: String, beerRequest: BeerRequest) =
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.updateBeer(beerId, beerRequest)
            response?.let {
                _beerResponse.postValue(Event(response))
                repository.updateDatabaseBeer(beerId, beerRequest)
            } ?: _beerResponse.postValue(Event(BeerResponse(msg = "Проверьте подключение к интернету!")))
        }

    private fun updateSnack(snackId: String, snackRequest: SnackRequest) =
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.updateSnack(snackId, snackRequest)
            response?.let {
                _snackResponse.postValue(Event(response))
                repository.updateDatabaseSnack(snackId, snackRequest)
            } ?: _snackResponse.postValue(Event(SnackResponse(msg = "Проверьте подключение к интернету!")))
        }
}