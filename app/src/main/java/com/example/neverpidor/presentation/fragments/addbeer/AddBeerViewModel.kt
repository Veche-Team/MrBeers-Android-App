package com.example.neverpidor.presentation.fragments.addbeer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neverpidor.data.providers.MenuCategory
import com.example.neverpidor.data.network.dto.beer.BeerRequest
import com.example.neverpidor.data.network.dto.snack.SnackRequest
import com.example.neverpidor.data.settings.AppSettings
import com.example.neverpidor.domain.model.DomainItem
import com.example.neverpidor.domain.repositories.MenuItemsRepository
import com.example.neverpidor.presentation.fragments.addbeer.util.AddUpdateMode
import com.example.neverpidor.presentation.fragments.addbeer.util.AddUpdateState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddBeerViewModel @Inject constructor(
    private val repository: MenuItemsRepository,
    private val appSettings: AppSettings
) : ViewModel() {

    private val _state = MutableStateFlow(AddUpdateState())
    val state: StateFlow<AddUpdateState> = _state

    private val _itemState = MutableSharedFlow<DomainItem>()
    val itemState: SharedFlow<DomainItem> = _itemState

    private val _response = MutableSharedFlow<String>()
    val response: SharedFlow<String> = _response

    init {
        _state.value = state.value.copy(mainItem = DomainItem(category = getCategory()))
    }

    fun getMenuItemById(itemId: String) = viewModelScope.launch(Dispatchers.IO) {
        val mainItem = repository.getMenuItemById(itemId)
        _state.emit(state.value.copy(mainItem = mainItem,
            mode = AddUpdateMode.UPDATE))
        _itemState.emit(mainItem)
    }

    fun onButtonClick() {
        val category = getCategory()
        val mode = state.value.mode
        if (mode == AddUpdateMode.ADD) {
            if (category == MenuCategory.BeerCategory) addBeer() else addSnack()
        } else {
            if (category == MenuCategory.BeerCategory) updateBeer() else updateSnack()
        }
    }

    fun onTitleTextChanged(text: String) {
        _state.value = state.value.copy(
            validationModel = state.value.validationModel.copy(title = text),
            addUpdateErrorFields = if (text.isEmpty()) state.value.addUpdateErrorFields.copy(
                titleError = "Title can't be empty"
            ) else state.value.addUpdateErrorFields.copy(
                titleError = ""
            )
        )
        enableButton()
    }

    fun onDescriptionTextChanged(text: String) {
        _state.value = state.value.copy(
            validationModel = state.value.validationModel.copy(description = text),
            addUpdateErrorFields = if (text.isEmpty()) state.value.addUpdateErrorFields.copy(
                descriptionError = "Description can't be empty"
            ) else state.value.addUpdateErrorFields.copy(
                descriptionError = ""
            )
        )
        enableButton()
    }

    fun onTypeTextChanged(text: String) {
        _state.value = state.value.copy(
            validationModel = state.value.validationModel.copy(type = text),
            addUpdateErrorFields = if (text.isEmpty()) state.value.addUpdateErrorFields.copy(
                typeError = "Type can't be empty"
            ) else state.value.addUpdateErrorFields.copy(
                typeError = ""
            )
        )
        enableButton()
    }

    fun onPriceTextChanged(text: String) {
        _state.value = state.value.copy(
            validationModel = state.value.validationModel.copy(price = text),
            addUpdateErrorFields = if (text.isEmpty()) state.value.addUpdateErrorFields.copy(
                priceError = "Price can't be empty"
            ) else try {
                val price = text.toDouble()
                if (price > 500.0) {
                    state.value.addUpdateErrorFields.copy(priceError = "Price is too high")
                } else if (price < 50.0) {
                    state.value.addUpdateErrorFields.copy(priceError = "Price is too low")
                } else {
                    state.value.addUpdateErrorFields.copy(priceError = "")
                }
            } catch (e: java.lang.Exception) {
                state.value.addUpdateErrorFields.copy(priceError = "Invalid input")
            }
        )
        enableButton()
    }

    fun onAlcPercentageTextChanged(text: String) {
        _state.value = state.value.copy(
            validationModel = state.value.validationModel.copy(alc = text),
            addUpdateErrorFields = if (text.isEmpty()) state.value.addUpdateErrorFields.copy(
                alcPercentageError = "Alc percentage can't be empty"
            ) else try {
                val alc = text.toDouble()
                if (alc > 20) {
                    state.value.addUpdateErrorFields.copy(alcPercentageError = "Too much alcohol for a beer")
                } else {
                    state.value.addUpdateErrorFields.copy(alcPercentageError = "")
                }
            } catch (e: java.lang.Exception) {
                state.value.addUpdateErrorFields.copy(alcPercentageError = "Invalid input")
            }
        )
        enableButton()
    }

    fun onVolumeTextChanged(text: String) {
        _state.value = state.value.copy(
            validationModel = state.value.validationModel.copy(volume = text),
            addUpdateErrorFields = if (text.isEmpty()) state.value.addUpdateErrorFields.copy(
                volumeError = "Volume can't be empty"
            ) else try {
                val volume = text.toDouble()
                if (volume > 5) {
                    state.value.addUpdateErrorFields.copy(volumeError = "Too much volume")
                } else if (volume < 0.25) {
                    state.value.addUpdateErrorFields.copy(volumeError = "Not enough volume")
                } else {
                    state.value.addUpdateErrorFields.copy(volumeError = "")
                }
            } catch (e: java.lang.Exception) {
                state.value.addUpdateErrorFields.copy(volumeError = "Invalid input")
            }
        )
        enableButton()
    }

    private fun enableButton() {
        _state.value = state.value.copy(
            isButtonEnabled = if (state.value.mainItem.category == MenuCategory.BeerCategory) {
                state.value.validationModel.title.isNotEmpty()
                        && state.value.validationModel.description.isNotEmpty()
                        && state.value.validationModel.type.isNotEmpty()
                        && state.value.validationModel.price.isNotEmpty()
                        && state.value.validationModel.alc.isNotEmpty()
                        && state.value.validationModel.volume.isNotEmpty()
                        && state.value.addUpdateErrorFields.titleError.isEmpty()
                        && state.value.addUpdateErrorFields.descriptionError.isEmpty()
                        && state.value.addUpdateErrorFields.typeError.isEmpty()
                        && state.value.addUpdateErrorFields.priceError.isEmpty()
                        && state.value.addUpdateErrorFields.alcPercentageError.isEmpty()
                        && state.value.addUpdateErrorFields.volumeError.isEmpty()
            } else {
                state.value.validationModel.title.isNotEmpty()
                        && state.value.validationModel.description.isNotEmpty()
                        && state.value.validationModel.type.isNotEmpty()
                        && state.value.validationModel.price.isNotEmpty()
                        && state.value.addUpdateErrorFields.titleError.isEmpty()
                        && state.value.addUpdateErrorFields.descriptionError.isEmpty()
                        && state.value.addUpdateErrorFields.typeError.isEmpty()
                        && state.value.addUpdateErrorFields.priceError.isEmpty()
            }
        )
    }

    private fun getCategory(): MenuCategory = appSettings.getCurrentCategory()

    private fun addBeer() = viewModelScope.launch(Dispatchers.IO) {
        val beerRequest = BeerRequest(
            name = state.value.validationModel.title,
            description = state.value.validationModel.description,
            type = state.value.validationModel.type,
            price = state.value.validationModel.price.toDouble(),
            alcPercentage = state.value.validationModel.alc.toDouble(),
            volume = state.value.validationModel.volume.toDouble()
        )
        val response = repository.addApiBeer(beerRequest)
        response?.let {
            repository.addBeerToDatabase(it)
            _response.emit(response.msg)
        }
            ?: _response.emit("Проверьте подключение к интернету!")
    }

    private fun addSnack() = viewModelScope.launch(Dispatchers.IO) {
        val snackRequest = SnackRequest(
            name = state.value.validationModel.title,
            description = state.value.validationModel.description,
            type = state.value.validationModel.type,
            price = state.value.validationModel.price.toDouble()
        )
        val response = repository.addApiSnack(snackRequest)
        response?.let {
            repository.addSnackToDatabase(it)
            _response.emit(response.msg)
        }
            ?: _response.emit("Проверьте подключение к интернету!")
    }

    private fun updateBeer() =
        viewModelScope.launch(Dispatchers.IO) {
            val beerRequest = BeerRequest(
                name = state.value.validationModel.title,
                description = state.value.validationModel.description,
                type = state.value.validationModel.type,
                price = state.value.validationModel.price.toDouble(),
                alcPercentage = state.value.validationModel.alc.toDouble(),
                volume = state.value.validationModel.volume.toDouble()
            )
            val beerId = state.value.mainItem.UID
            val response = repository.updateApiBeer(beerId, beerRequest)
            response?.let {
                _response.emit(response.msg)
                repository.updateDatabaseBeer(beerId, beerRequest)
            }
                ?: _response.emit("Проверьте подключение к интернету!")
        }

    private fun updateSnack() =
        viewModelScope.launch(Dispatchers.IO) {
            val snackRequest = SnackRequest(
                name = state.value.validationModel.title,
                description = state.value.validationModel.description,
                type = state.value.validationModel.type,
                price = state.value.validationModel.price.toDouble()
            )
            val snackId = state.value.mainItem.UID
            val response = repository.updateApiSnack(snackId, snackRequest)
            response?.let {
                _response.emit(response.msg)
                repository.updateDatabaseSnack(snackId, snackRequest)
            }
                ?: _response.emit("Проверьте подключение к интернету!")
        }
}