package com.example.neverpidor.presentation.fragments.addbeer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neverpidor.data.providers.MenuCategory
import com.example.neverpidor.data.network.dto.beer.BeerResponse
import com.example.neverpidor.data.network.dto.beer.BeerRequest
import com.example.neverpidor.data.network.dto.snack.SnackResponse
import com.example.neverpidor.data.network.dto.snack.SnackRequest
import com.example.neverpidor.data.settings.AppSettings
import com.example.neverpidor.domain.model.DomainItem
import com.example.neverpidor.domain.repository.MenuItemsRepository
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

    private val _menuItemLiveData = MutableLiveData<DomainItem>()
    val menuItemLiveData: LiveData<DomainItem> = _menuItemLiveData

    private val _beerResponse = MutableLiveData<Event<BeerResponse?>>()
    val beerResponse: LiveData<Event<BeerResponse?>> = _beerResponse

    private val _snackResponse = MutableLiveData<Event<SnackResponse?>>()
    val snackResponse: LiveData<Event<SnackResponse?>> = _snackResponse

    fun getMenuItemById(itemId: String) = viewModelScope.launch(Dispatchers.IO) {
        _menuItemLiveData.postValue(repository.getMenuItemById(itemId))
    }

    fun getItem(): MenuCategory = appSettings.getCurrentItem()

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
        val response = repository.addApiBeer(beerRequest)
        response?.let {
            repository.addBeerToDatabase(it)
            _beerResponse.postValue(Event(response))
        }
            ?: _beerResponse.postValue(Event(BeerResponse(msg = "Проверьте подключение к интернету!")))
    }

    private fun addSnack(snackRequest: SnackRequest) = viewModelScope.launch(Dispatchers.IO) {
        val response = repository.addApiSnack(snackRequest)
        response?.let {
            repository.addSnackToDatabase(it)
            _snackResponse.postValue(Event(response))
        }
            ?: _snackResponse.postValue(Event(SnackResponse(msg = "Проверьте подключение к интернету!")))
    }

    private fun updateBeer(beerId: String, beerRequest: BeerRequest) =
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.updateApiBeer(beerId, beerRequest)
            response?.let {
                _beerResponse.postValue(Event(response))
                repository.updateDatabaseBeer(beerId, beerRequest)
            } ?: _beerResponse.postValue(Event(BeerResponse(msg = "Проверьте подключение к интернету!")))
        }

    private fun updateSnack(snackId: String, snackRequest: SnackRequest) =
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.updateApiSnack(snackId, snackRequest)
            response?.let {
                _snackResponse.postValue(Event(response))
                repository.updateDatabaseSnack(snackId, snackRequest)
            } ?: _snackResponse.postValue(Event(SnackResponse(msg = "Проверьте подключение к интернету!")))
        }
}