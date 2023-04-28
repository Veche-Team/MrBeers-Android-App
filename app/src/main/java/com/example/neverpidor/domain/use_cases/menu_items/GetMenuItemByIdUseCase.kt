package com.example.neverpidor.domain.use_cases.menu_items

import com.example.neverpidor.domain.model.DomainItem
import com.example.neverpidor.domain.repositories.MenuItemsRepository

class GetMenuItemByIdUseCase(
    private val repository: MenuItemsRepository
) {
    suspend operator fun invoke(itemId: String): DomainItem {
        return repository.getMenuItemById(itemId)
    }
}