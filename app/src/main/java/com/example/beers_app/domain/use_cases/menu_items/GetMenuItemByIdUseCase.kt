package com.example.beers_app.domain.use_cases.menu_items

import com.example.beers_app.domain.model.DomainItem
import com.example.beers_app.domain.repositories.MenuItemsRepository

class GetMenuItemByIdUseCase(
    private val repository: MenuItemsRepository
) {
    suspend operator fun invoke(itemId: String): DomainItem {
        return repository.getMenuItemById(itemId)
    }
}