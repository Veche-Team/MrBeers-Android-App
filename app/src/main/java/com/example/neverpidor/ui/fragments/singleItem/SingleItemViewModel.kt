package com.example.neverpidor.ui.fragments.singleItem

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neverpidor.data.MenuItemsRepository
import com.example.neverpidor.model.domain.DomainBeer
import com.example.neverpidor.model.domain.DomainSnack
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
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
}