package com.example.beers_app.domain.use_cases.menu_items

import android.content.Context
import com.example.beers.R
import com.example.beers_app.data.network.dto.beer.BeerRequest
import com.example.beers_app.domain.repositories.MenuItemsRepository

class UpdateBeerUseCase(
    private val repository: MenuItemsRepository,
    private val context: Context
) {
    suspend operator fun invoke(beerId: String, beerRequest: BeerRequest): String {
        val response = repository.updateApiBeer(beerId, beerRequest)
        response?.let {
            repository.updateDatabaseBeer(beerId, beerRequest)
            return response.msg
        } ?: return context.getString(R.string.check_connection)
    }
}