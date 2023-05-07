package com.example.neverpidor.presentation.fragments.favourites.epoxy

import com.airbnb.epoxy.EpoxyController
import com.example.neverpidor.domain.model.DomainItem
import com.example.neverpidor.domain.model.InCartItem
import com.example.neverpidor.presentation.fragments.singleItem.epoxy.models.SingleItemOffersEpoxyModel
import com.example.neverpidor.util.epoxy.models.EmptyListModel
import com.example.neverpidor.util.epoxy.models.ErrorModel
import com.example.neverpidor.util.epoxy.models.LoadingScreenEpoxyModel

class FavouritesEpoxyController(
    val onItemClick: (DomainItem) -> Unit,
    val onFavClick: (DomainItem) -> Unit,
    val onRetry: () -> Unit,
    val onToMenuClick: () -> Unit,
    private val onPlusClick: (InCartItem) -> Unit,
    private val onMinusClick: (InCartItem) -> Unit,
    private val onAddToCartClick: (String) -> Unit,
) : EpoxyController() {

    private var isLoading = true
    var isError = false
        set(value) {
            field = value
            isLoading = false
            requestModelBuild()
        }

    var items = listOf<DomainItem>()
        set(value) {
            if (value.isNotEmpty()) {
                field = value
                isLoading = false
                requestModelBuild()
            }
        }

    var inCartState = listOf<InCartItem>()
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
                onRetry()
                requestModelBuild()
            }.id("error").addTo(this)
        }
        if (items.isEmpty()) {
            EmptyListModel { onToMenuClick() }.id("empty").addTo(this)
            return
        }
        items.forEach { domainItem ->
            SingleItemOffersEpoxyModel(
                domainItem = domainItem,
                onItemClick = { item ->
                    onItemClick(item)
                },
                isLiked = true,
                onFavClick = { item ->
                    onFavClick(item)
                },
                isUserLogged = true,
                onNoUserClick = {},
                inCartState = inCartState.find { it.UID == domainItem.UID }
                    ?: InCartItem(),
                onMinusClick = onMinusClick::invoke,
                onAddToCartClick = onAddToCartClick::invoke,
                onPlusClick = onPlusClick::invoke
            ).id(domainItem.UID).addTo(this)
        }
    }
}