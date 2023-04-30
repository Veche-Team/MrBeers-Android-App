package com.example.neverpidor.domain.use_cases.menu_items

import com.example.neverpidor.data.providers.MenuCategory
import com.example.neverpidor.domain.model.DomainItem

class GetItemsSetUseCase {

     operator fun invoke(
        items: List<DomainItem>,
        category: MenuCategory
    ): Set<DomainItem> {
        val set = mutableSetOf<DomainItem>()
        while (set.size != 3) {
            if (category == MenuCategory.BeerCategory) {
                set.add(items.filter { it.category == MenuCategory.SnackCategory }.random())
            } else {
                set.add(items.filter { it.category == MenuCategory.BeerCategory }.random())
            }
        }
        return set
    }
}