package com.example.beers_app.domain.use_cases.menu_items

import android.content.Context
import com.example.beers.R
import com.example.beers_app.data.common.MenuCategory
import com.example.beers_app.domain.repositories.MenuItemsRepository

class DeleteItemUseCase(
    private val repository: MenuItemsRepository,
    private val context: Context
) {
    suspend operator fun invoke(category: MenuCategory, itemId: String): String {
        val response = if (category == MenuCategory.BeerCategory) {
            repository.deleteApiBeer(itemId)?.msg
        } else repository.deleteApiSnack(itemId)?.msg
        response?.let {
            repository.deleteMenuItemFromDatabase(itemId)
            return response
        } ?: return context.getString(R.string.check_connection)
    }
}