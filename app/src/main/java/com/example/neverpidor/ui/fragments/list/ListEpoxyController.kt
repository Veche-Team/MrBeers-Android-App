package com.example.neverpidor.ui.fragments.list

import com.airbnb.epoxy.EpoxyController
import com.example.neverpidor.R
import com.example.neverpidor.data.MenuCategoryEntity
import com.example.neverpidor.databinding.ModelPreviewBinding
import com.example.neverpidor.ui.epoxy.ViewBindingKotlinModel

class ListEpoxyController(private val onClick: (Int) -> Unit) : EpoxyController() {

    var list: List<MenuCategoryEntity> = listOf()

    override fun buildModels() {
        list.forEach {
            PreviewEpoxyModel(it, onClick).id(it.id).addTo(this)
        }
    }

    data class PreviewEpoxyModel(
        val menuCategoryEntity: MenuCategoryEntity,
        val onClick: (Int) -> Unit
    ) : ViewBindingKotlinModel<ModelPreviewBinding>(
        R.layout.model_preview
    ) {
        override fun ModelPreviewBinding.bind() {
            rootLayout.setBackgroundResource(menuCategoryEntity.previewImage)
            titleText.text = menuCategoryEntity.name
            rootLayout.setOnClickListener {
                onClick(menuCategoryEntity.id)
            }
        }

    }
}