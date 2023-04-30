package com.example.neverpidor.presentation.fragments.addbeer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neverpidor.data.network.dto.beer.BeerRequest
import com.example.neverpidor.data.network.dto.snack.SnackRequest
import com.example.neverpidor.data.providers.MenuCategory
import com.example.neverpidor.domain.model.DomainItem
import com.example.neverpidor.domain.use_cases.menu_items.MenuItemsUseCases
import com.example.neverpidor.domain.use_cases.validation.ValidationUseCases
import com.example.neverpidor.presentation.fragments.addbeer.util.AddUpdateMode
import com.example.neverpidor.presentation.fragments.addbeer.util.AddUpdateState
import com.example.neverpidor.util.EmptyFieldException
import com.example.neverpidor.util.InvalidAlcPercentageException
import com.example.neverpidor.util.InvalidPriceException
import com.example.neverpidor.util.InvalidVolumeException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AddBeerViewModel @Inject constructor(
    private val menuItemsUseCases: MenuItemsUseCases,
    private val validationUseCases: ValidationUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(AddUpdateState())
    val state: StateFlow<AddUpdateState> = _state

    private val _itemState = MutableSharedFlow<DomainItem>()
    val itemState: SharedFlow<DomainItem> = _itemState

    private val _response = MutableSharedFlow<String>()
    val response: SharedFlow<String> = _response

    fun getMenuItemById(itemId: String) = viewModelScope.launch(Dispatchers.IO) {
        val mainItem = menuItemsUseCases.getMenuItemByIdUseCase(itemId)
        launch {
            _itemState.emit(mainItem)
        }
        launch {
            _state.value = state.value.copy(
                mainItem = mainItem,
                mode = AddUpdateMode.UPDATE
            )
        }
    }

    fun onButtonClick(category: MenuCategory) = viewModelScope.launch {
        val mode = state.value.mode
        if (mode == AddUpdateMode.ADD) {
            if (category == MenuCategory.BeerCategory) addBeer() else addSnack()
        } else {
            if (category == MenuCategory.BeerCategory) updateBeer() else updateSnack()
        }
    }

    fun onTitleTextChanged(text: String) = viewModelScope.launch {
        _state.emit(
            state.value.copy(
                validationModel = state.value.validationModel.copy(title = text),
                addUpdateErrorFields = try {
                    validationUseCases.titleValidationUseCase(text)
                    state.value.addUpdateErrorFields.copy(titleError = "")
                } catch (e: EmptyFieldException) {
                    state.value.addUpdateErrorFields.copy(titleError = e.message ?: "")
                }
            )
        )
        enableButton()
    }

    fun onDescriptionTextChanged(text: String) = viewModelScope.launch {
        _state.emit(
            state.value.copy(
                validationModel = state.value.validationModel.copy(description = text),
                addUpdateErrorFields = try {
                    validationUseCases.descriptionValidationUseCase(text)
                    state.value.addUpdateErrorFields.copy(descriptionError = "")
                } catch (e: EmptyFieldException) {
                    state.value.addUpdateErrorFields.copy(descriptionError = e.message ?: "")
                }
            )
        )
        enableButton()
    }

    fun onTypeTextChanged(text: String) = viewModelScope.launch {
        _state.emit(
            state.value.copy(
                validationModel = state.value.validationModel.copy(type = text),
                addUpdateErrorFields = try {
                    validationUseCases.typeValidationUseCase(text)
                    state.value.addUpdateErrorFields.copy(typeError = "")
                } catch (e: EmptyFieldException) {
                    state.value.addUpdateErrorFields.copy(typeError = e.message ?: "")
                }
            )
        )
        enableButton()
    }

    fun onPriceTextChanged(text: String) = viewModelScope.launch {
        _state.emit(
            state.value.copy(
                validationModel = state.value.validationModel.copy(price = text),
                addUpdateErrorFields = try {
                    validationUseCases.priceValidationUseCase(text)
                    state.value.addUpdateErrorFields.copy(priceError = "")
                } catch (e: InvalidPriceException) {
                    state.value.addUpdateErrorFields.copy(priceError = e.message ?: "")
                }
            )
        )
        enableButton()
    }

    fun onAlcPercentageTextChanged(text: String) = viewModelScope.launch {
        _state.emit(
            state.value.copy(
                validationModel = state.value.validationModel.copy(alc = text),
                addUpdateErrorFields = try {
                    validationUseCases.alcPercentageValidationUseCase(text)
                    state.value.addUpdateErrorFields.copy(alcPercentageError = "")
                } catch (e: InvalidAlcPercentageException) {
                    state.value.addUpdateErrorFields.copy(alcPercentageError = e.message ?: "")
                }
            )
        )
        enableButton()
    }

    fun onVolumeTextChanged(text: String) = viewModelScope.launch {
        _state.emit(
            state.value.copy(
                validationModel = state.value.validationModel.copy(volume = text),
                addUpdateErrorFields = try {
                    validationUseCases.volumeValidationUseCase(text)
                    state.value.addUpdateErrorFields.copy(volumeError = "")
                } catch (e: InvalidVolumeException) {
                    state.value.addUpdateErrorFields.copy(volumeError = e.message ?: "")
                }
            )
        )
        enableButton()
    }

    private fun enableButton() = viewModelScope.launch {
        _state.emit(
            state.value.copy(
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
        )
    }

    private fun addBeer() = viewModelScope.launch(Dispatchers.IO) {
        val beerRequest = BeerRequest(
            name = state.value.validationModel.title,
            description = state.value.validationModel.description,
            type = state.value.validationModel.type,
            price = state.value.validationModel.price.toDouble(),
            alcPercentage = state.value.validationModel.alc.toDouble(),
            volume = state.value.validationModel.volume.toDouble()
        )
        withContext(viewModelScope.coroutineContext) {
            val response = menuItemsUseCases.addBeerUseCase(beerRequest)
            _response.emit(response)
        }
    }

    private fun addSnack() = viewModelScope.launch(Dispatchers.IO) {
        val snackRequest = SnackRequest(
            name = state.value.validationModel.title,
            description = state.value.validationModel.description,
            type = state.value.validationModel.type,
            price = state.value.validationModel.price.toDouble()
        )
        launch {
            val response = async {
                menuItemsUseCases.addSnackUseCase(snackRequest)
            }
            _response.emit(response.await())
        }
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
            val response = menuItemsUseCases.updateBeerUseCase(beerId, beerRequest)
            _response.emit(response)
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
            val response = menuItemsUseCases.updateSnackUseCase(snackId, snackRequest)
            _response.emit(response)
        }
}