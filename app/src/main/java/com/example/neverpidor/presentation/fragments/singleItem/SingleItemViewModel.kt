package com.example.neverpidor.presentation.fragments.singleItem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neverpidor.data.database.entities.MenuItemEntity
import com.example.neverpidor.data.providers.MenuCategory
import com.example.neverpidor.domain.model.DomainBeer
import com.example.neverpidor.domain.model.DomainSnack
import com.example.neverpidor.domain.model.DomainItem
import com.example.neverpidor.domain.repository.MenuItemsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SingleItemViewModel @Inject constructor(
    private val repository: MenuItemsRepository,
) : ViewModel() {

    private val _menuItemLiveData = MutableLiveData<DomainItem>()
    val menuItemLiveData: LiveData<DomainItem> = _menuItemLiveData

    private val _itemListLiveData = MutableLiveData<Set<DomainItem>>()
    val itemListLiveData: LiveData<Set<DomainItem>> = _itemListLiveData


    fun getMenuItemById(itemId: String) = viewModelScope.launch(Dispatchers.IO) {
        _menuItemLiveData.postValue(repository.getMenuItemById(itemId))
    }

    fun getBeerSet() = viewModelScope.launch(Dispatchers.IO) {
        val set = mutableSetOf<DomainItem>()
        val allBeer = repository.getDatabaseMenuItems()
        allBeer.collect {
            while (set.size < 3) {
                set.add(it.filter { item -> item.category == MenuCategory.BeerCategory }.random())
            }
            _itemListLiveData.postValue(set)
        }
    }

    fun getSnackSet() = viewModelScope.launch(Dispatchers.IO) {
        val set = mutableSetOf<DomainItem>()
        val allSnacks = repository.getDatabaseMenuItems()
        allSnacks.collect {
            while (set.size < 3) {
                set.add(it.filter { item -> item.category == MenuCategory.SnackCategory }.random())
            }
            _itemListLiveData.postValue(set)
        }
    }

    fun faveSnack(domainSnack: DomainSnack) = viewModelScope.launch {
        repository.updateDatabaseMenuItem(
            itemEntity = MenuItemEntity(
                UID = domainSnack.UID,
                description = domainSnack.description,
                name = domainSnack.name,
                price = domainSnack.price,
                type = domainSnack.type,
                isFaved = !domainSnack.isFaved,
                category = MenuCategory.SnackCategory,
                alcPercentage = null,
                volume = null
            )
        )
        _menuItemLiveData.value = DomainSnack(
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
        repository.updateDatabaseMenuItem(
            itemEntity = MenuItemEntity(
                UID = domainBeer.UID,
                description = domainBeer.description,
                name = domainBeer.name,
                price = domainBeer.price,
                type = domainBeer.type,
                isFaved = !domainBeer.isFaved,
                alcPercentage = domainBeer.alcPercentage,
                volume = domainBeer.volume,
                category = MenuCategory.BeerCategory
            )
        )
        _menuItemLiveData.value = DomainBeer(
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