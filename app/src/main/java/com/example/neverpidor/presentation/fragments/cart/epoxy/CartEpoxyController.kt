package com.example.neverpidor.presentation.fragments.cart.epoxy

import com.airbnb.epoxy.EpoxyController
import com.example.neverpidor.domain.model.InCartItem
import com.example.neverpidor.presentation.fragments.cart.epoxy.models.ItemInCartModel
import com.example.neverpidor.util.epoxy.models.EmptyListModel
import com.example.neverpidor.util.epoxy.models.LoadingScreenEpoxyModel

class CartEpoxyController(
    val onAddClick: (InCartItem) -> Unit,
    val onRemoveClick: (InCartItem) -> Unit,
    val onToMenuClick: () -> Unit,
    val onItemClick: (String) -> Unit
) : EpoxyController() {

    private var isLoading = true

    var cartItems = listOf<InCartItem>()
        set(value) {
            field = value
            isLoading = false
            requestModelBuild()
        }

    override fun buildModels() {

        if (isLoading) {
            LoadingScreenEpoxyModel().id("Loading").addTo(this)
            return
        }
        if (cartItems.isEmpty()) {
            EmptyListModel(onToMenuClick).id("empty").addTo(this)
            return
        }

        cartItems.forEach {
            ItemInCartModel(
                it,
                onAddClick = { inCartItem ->
                    onAddClick(inCartItem)
                },
                onRemoveClick = { inCartItem ->
                   onRemoveClick(inCartItem)
                },
                onItemClick = { id ->
                    onItemClick(id)
                }
            ).id(it.UID).addTo(this)
        }
    }
}