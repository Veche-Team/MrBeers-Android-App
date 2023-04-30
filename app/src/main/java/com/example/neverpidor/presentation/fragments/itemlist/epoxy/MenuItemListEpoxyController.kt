package com.example.neverpidor.presentation.fragments.itemlist.epoxy

import com.airbnb.epoxy.EpoxyController
import com.example.neverpidor.R
import com.example.neverpidor.domain.model.InCartItem
import com.example.neverpidor.data.providers.MenuCategory
import com.example.neverpidor.domain.model.DomainItem
import com.example.neverpidor.domain.model.User
import com.example.neverpidor.util.epoxy.models.LoadingScreenEpoxyModel
import com.example.neverpidor.presentation.fragments.itemlist.epoxy.models.DividerEpoxy
import com.example.neverpidor.presentation.fragments.itemlist.epoxy.models.ItemTypeEpoxyModel
import com.example.neverpidor.presentation.fragments.itemlist.epoxy.models.MenuItemEpoxyModel
import com.example.neverpidor.util.epoxy.models.ErrorModel

class MenuItemListEpoxyController(
    val category: MenuCategory,
    private val onEditClick: (String) -> Unit,
    private val onItemClick: (DomainItem) -> Unit,
    private val onFavClick: (String) -> Unit,
    private val onRetry: (MenuCategory) -> Unit,
    private val onPlusClick: (InCartItem) -> Unit,
    private val onMinusClick: (InCartItem) -> Unit,
    private val onAddToCartClick: (String) -> Unit,
    private val onNoUserClick: () -> Unit
) :
    EpoxyController() {

    private var isLoading = true

    var isError = false
        set(value) {
            field = value
            isLoading = false
            requestModelBuild()
        }

    var searchInput = ""
        set(value) {
            field = value.lowercase()
            requestModelBuild()
        }

    var items = listOf<DomainItem>()
        set(value) {
            if (value.isNotEmpty()) {
                field = value
                isLoading = false
                isError = false
                requestModelBuild()
            }
        }
    var likes = listOf<String>()
        set(value) {
            field = value
            requestModelBuild()
        }

    var inCartState = listOf<InCartItem>()

    var userRole: User.Role = User.Role.NoUser
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {

        if (isLoading) {
            LoadingScreenEpoxyModel().id("Loading").addTo(this)
            return
        }
        if (isError) {
            ErrorModel {
                isLoading = true
                onRetry(category)
                requestModelBuild()
            }.id("error").addTo(this)
            return
        }
        items.filter {
            it.name.lowercase().contains(searchInput) ||
                    it.type.lowercase().contains(searchInput)
        }.groupBy { it.type }
            .forEach { map ->
                ItemTypeEpoxyModel(
                    map.key,
                ).id(map.key.hashCode()).addTo(this)
                DividerEpoxy(R.color.amber_dark).id(map.key.hashCode()).addTo(this)
                map.value.forEach { data ->
                    MenuItemEpoxyModel(
                        data,
                        likes.any { it == data.UID },
                        onEditClick,
                        onItemClick,
                        onFavClick,
                        inCartState = inCartState.find { it.UID == data.UID }
                            ?: InCartItem(),
                        onMinusClick = { onMinusClick(it) },
                        onPlusClick = { onPlusClick(it) },
                        onAddToCartClick = { onAddToCartClick(it) },
                        userRole = userRole,
                        onNoUserClick = { onNoUserClick() }
                    ).id(data.UID)
                        .addTo(this)
                }
            }
    }
}