package com.example.neverpidor.presentation.fragments.singleItem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neverpidor.domain.model.DomainBeer
import com.example.neverpidor.domain.model.DomainSnack
import com.example.neverpidor.data.database.entities.BeerEntity
import com.example.neverpidor.data.database.entities.SnackEntity
import com.example.neverpidor.domain.repository.MenuItemsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SingleItemViewModel @Inject constructor(
    private val repository: MenuItemsRepository,
) : ViewModel() {

    private val _beerLiveData = MutableLiveData<DomainBeer>()
    val beerLiveData: LiveData<DomainBeer> = _beerLiveData

    private val _snackLiveData = MutableLiveData<DomainSnack>()
    val snackLiveData: LiveData<DomainSnack> = _snackLiveData

    private val _beerListLiveData = MutableLiveData<Set<DomainBeer>>()
    val beerListLiveData: LiveData<Set<DomainBeer>> = _beerListLiveData

    private val _snackListLiveData = MutableLiveData<Set<DomainSnack>>()
    val snackListLiveData: LiveData<Set<DomainSnack>> = _snackListLiveData

    fun getBeerById(beerId: String) = viewModelScope.launch(Dispatchers.IO) {
        _beerLiveData.postValue(repository.getBeerById(beerId))
    }

    fun getSnackById(snackId: String) = viewModelScope.launch(Dispatchers.IO) {
        _snackLiveData.postValue(repository.getSnackById(snackId))
    }

    fun getBeerSet() = viewModelScope.launch(Dispatchers.IO) {
        val set = mutableSetOf<DomainBeer>()
        val allBeer = repository.getDatabaseBeers()
        allBeer.collect {
            while (set.size < 3) {
                set.add(it.random())
            }
            _beerListLiveData.postValue(set)
        }
    }

    fun getSnackSet() = viewModelScope.launch(Dispatchers.IO) {
        val set = mutableSetOf<DomainSnack>()
        val allSnacks = repository.getDatabaseSnacks()
        allSnacks.collect {
            while (set.size < 3) {
                set.add(it.random())
            }
            _snackListLiveData.postValue(set)
        }
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
        _snackLiveData.value = DomainSnack(
            category = domainSnack.category,
            UID = domainSnack.UID,
            description = domainSnack.description,
            name = domainSnack.name,
            price = domainSnack.price,
            type = domainSnack.type,
            image = domainSnack.image,
            isFaved = !domainSnack.isFaved
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
        _beerLiveData.value = DomainBeer(
            category = domainBeer.category,
            UID = domainBeer.UID,
            description = domainBeer.description,
            name = domainBeer.name,
            price = domainBeer.price,
            type = domainBeer.type,
            image = domainBeer.image,
            isFaved = !domainBeer.isFaved,
            alcPercentage = domainBeer.alcPercentage,
            volume = domainBeer.volume
        )
    }
}