package com.example.neverpidor.domain.use_cases.menu_items

import com.example.neverpidor.data.network.dto.beer.BeerRequest
import com.example.neverpidor.domain.repositories.MenuItemsRepository

class AddBeerUseCase(
    private val repository: MenuItemsRepository
) {
    suspend operator fun invoke(beerRequest: BeerRequest): String {
        val response = repository.addApiBeer(beerRequest)
        response?.let {
            repository.addBeerToDatabase(it)
            return it.msg
        } ?: return "Проверьте подключение к интернету!"
    }
}