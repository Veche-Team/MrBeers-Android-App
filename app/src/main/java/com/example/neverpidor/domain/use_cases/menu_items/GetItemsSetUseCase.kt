package com.example.neverpidor.domain.use_cases.menu_items

import com.example.neverpidor.data.providers.MenuCategory
import com.example.neverpidor.domain.model.DomainItem
import com.example.neverpidor.util.Constants.SET_OFFERS_SIZE

class GetItemsSetUseCase {

     operator fun invoke(
        items: List<DomainItem>,
        category: MenuCategory
    ): Set<DomainItem> {
        val set = mutableSetOf<DomainItem>()
        while (set.size != SET_OFFERS_SIZE) {
            if (category == MenuCategory.BeerCategory) {
                set.add(items.filter { it.category == MenuCategory.SnackCategory }.random())
            } else {
                set.add(items.filter { it.category == MenuCategory.BeerCategory }.random())
            }
        }
        return set
    }
}