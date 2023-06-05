package com.example.beers_app.domain.use_cases.menu_items

import android.content.Context
import com.example.beers.R
import com.example.beers_app.data.network.dto.beer.BeerRequest
import com.example.beers_app.domain.repositories.MenuItemsRepository

class AddBeerUseCase(
    private val repository: MenuItemsRepository,
    private val context: Context
) {
    suspend operator fun invoke(beerRequest: BeerRequest): String {
        val response = repository.addApiBeer(beerRequest)
        response?.let {
            repository.addBeerToDatabase(it)
            return it.msg
        } ?: return context.getString(R.string.check_connection)
    }
}