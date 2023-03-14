package com.example.neverpidor.ui.fragments.addbeer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neverpidor.util.Event
import com.example.neverpidor.R
import com.example.neverpidor.data.MenuItemsRepository
import com.example.neverpidor.model.domain.DomainBeer
import com.example.neverpidor.model.domain.DomainSnack
import com.example.neverpidor.model.network.beer.BeerResponse
import com.example.neverpidor.model.network.beer.BeerRequest
import com.example.neverpidor.model.network.snack.SnackResponse
import com.example.neverpidor.model.network.snack.SnackRequest
import com.example.neverpidor.model.settings.AppSettings
import com.example.neverpidor.util.InvalidFields
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

    private val _currentLiveState = MutableLiveData<InvalidFields>()
    val currentLiveState: LiveData<InvalidFields>
        get() = _currentLiveState

    private fun addBeer(beerRequest: BeerRequest) = viewModelScope.launch(Dispatchers.IO) {
        _beerResponse.postValue(Event(repository.addBeer(beerRequest)))
    }

    private fun addSnack(snackRequest: SnackRequest) = viewModelScope.launch(Dispatchers.IO) {
        _snackResponse.postValue(
            Event(repository.addSnack(snackRequest))
        )
    }

    fun getBeerById(beerId: String) = viewModelScope.launch(Dispatchers.IO) {
        _beerLiveData.postValue(repository.getBeerById(beerId))
    }

    fun getSnackById(snackId: String) = viewModelScope.launch(Dispatchers.IO) {
        _snackLiveData.postValue(repository.getSnackById(snackId))
    }

    private fun updateBeer(beerId: String, beerRequest: BeerRequest) =
        viewModelScope.launch(Dispatchers.IO) {
            _beerResponse.postValue(Event(repository.updateBeer(beerId, beerRequest)))
        }

    private fun updateSnack(snackId: String, snackRequest: SnackRequest) =
        viewModelScope.launch(Dispatchers.IO) {
            _snackResponse.postValue(Event(repository.updateSnack(snackId, snackRequest)))
        }

    fun getItem(): Int = appSettings.getCurrentItem()

    fun validateFields(
        title: String,
        description: String,
        type: String,
        price: String,
        alc: String? = null,
        volume: String? = null,
        itemId: String? = null
    ): Boolean {
        val invalidFields = arrayListOf<Pair<String, Int>>()
        if (title.isEmpty()) invalidFields.add(INPUT_TITLE)
        if (description.isEmpty()) invalidFields.add(INPUT_DESCRIPTION)
        if (type.isEmpty()) invalidFields.add(INPUT_TYPE)
        if (price.isEmpty()) invalidFields.add(EMPTY_PRICE)
        if (price.isNotEmpty() && price.toDouble() < 50.0) invalidFields.add(LOW_PRICE)
        if (price.isNotEmpty() && price.toDouble() > 500.0) invalidFields.add(HIGH_PRICE)
        alc?.let {
            if (it.isEmpty()) invalidFields.add(EMPTY_ALC)
            if (it.isNotEmpty() && it.toDouble() > 20.0) invalidFields.add(HIGH_ALC)
        }
        volume?.let {
            if (it.isEmpty()) invalidFields.add(EMPTY_VOLUME)
            if (it.isNotEmpty() && it.toDouble() < 0.25) invalidFields.add(LOW_VOLUME)
            if (it.isNotEmpty() && it.toDouble() > 5.00) invalidFields.add(HIGH_VOLUME)
        }

        if (invalidFields.isNotEmpty()) {
            _currentLiveState.value = InvalidFields(invalidFields)
            return false
        }
        _currentLiveState.value = InvalidFields(emptyList())
        alc?.let {
            val beerRequest = BeerRequest(
                alc.toDouble(),
                description,
                title,
                price.toDouble(),
                type,
                volume!!.toDouble()
            )
            if (itemId != null) {
                updateBeer(itemId, beerRequest)
            } else {
                addBeer(beerRequest)
            }
            return true
        }
        val snackRequest = SnackRequest(description, title, price.toDouble(), type)
        if (itemId != null) {
            updateSnack(itemId, snackRequest)
        } else {
            addSnack(snackRequest)
        }
        return true
    }

    companion object {
        val INPUT_TITLE = "INPUT_TITLE" to R.string.empty_name_field
        val INPUT_DESCRIPTION = "INPUT_DESCRIPTION" to R.string.empty_description_field
        val INPUT_TYPE = "INPUT_TYPE" to R.string.empty_type_field
        val EMPTY_PRICE = "EMPTY_PRICE" to R.string.empty_price_field
        val LOW_PRICE = "LOW_PRICE" to R.string.low_price_field
        val HIGH_PRICE = "HIGH_PRICE" to R.string.high_price_field
        val EMPTY_ALC = "EMPTY_ALC" to R.string.empty_alc_field
        val HIGH_ALC = "HIGH_ALC" to R.string.high_alc_field
        val EMPTY_VOLUME = "EMPTY_VOLUME" to R.string.empty_volume_field
        val LOW_VOLUME = "LOW_VOLUME" to R.string.low_volume_field
        val HIGH_VOLUME = "HIGH_VOLUME" to R.string.high_volume_field
    }
}