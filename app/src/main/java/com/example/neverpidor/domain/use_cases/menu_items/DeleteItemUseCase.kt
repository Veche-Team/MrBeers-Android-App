package com.example.neverpidor.domain.use_cases.menu_items

import android.util.Log
import com.example.neverpidor.data.providers.MenuCategory
import com.example.neverpidor.domain.repositories.MenuItemsRepository

class DeleteItemUseCase(
    val repository: MenuItemsRepository
) {
    suspend operator fun invoke(category: MenuCategory, itemId: String): String {
        val response = if (category == MenuCategory.BeerCategory) {
            repository.deleteApiBeer(itemId)?.msg
        } else repository.deleteApiSnack(itemId)?.msg
        response?.let {
            Log.e("DELETE", response)
            repository.deleteMenuItemFromDatabase(itemId)
            return response
        } ?: return "Проверьте подключение к интернету!"
    }
}