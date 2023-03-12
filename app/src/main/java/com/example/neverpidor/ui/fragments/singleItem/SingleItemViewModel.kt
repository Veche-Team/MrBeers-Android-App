package com.example.neverpidor.ui.fragments.singleItem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neverpidor.data.MenuItemsRepository
import com.example.neverpidor.model.domain.DomainBeer
import com.example.neverpidor.model.domain.DomainSnack
import dagger.hilt.android.lifecycle.HiltViewModel
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

    fun getBeerById(beerId: String) = viewModelScope.launch {
        _beerLiveData.postValue(repository.getBeerById(beerId))
    }

    fun getSnackById(snackId: String) = viewModelScope.launch {
        _snackLiveData.postValue(repository.getSnackById(snackId))
    }

    fun getBeerSet() = viewModelScope.launch {
        val set = mutableSetOf<DomainBeer>()
        val allBeer = repository.getBeers()
        while (set.size < 3) {
            allBeer?.let {
                set.add(it.random())
            }
        }
        _beerListLiveData.postValue(set)
    }

    fun getSnackSet() = viewModelScope.launch {
        val set = mutableSetOf<DomainSnack>()
        val allSnacks = repository.getSnacks()
        while (set.size < 3) {
            allSnacks?.let {
                set.add(it.random())
            }
        }
        _snackListLiveData.postValue(set)
    }




}