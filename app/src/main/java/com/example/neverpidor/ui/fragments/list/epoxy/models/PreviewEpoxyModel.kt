package com.example.neverpidor.ui.fragments.list.epoxy.models

import com.example.neverpidor.R
import com.example.neverpidor.data.Category
import com.example.neverpidor.data.MenuCategoryEntity
import com.example.neverpidor.databinding.ModelPreviewBinding
import com.example.neverpidor.ui.epoxy.ViewBindingKotlinModel

data class PreviewEpoxyModel(
    val menuCategoryEntity: MenuCategoryEntity,
    val onClick: (Category) -> Unit
) : ViewBindingKotlinModel<ModelPreviewBinding>(
    R.layout.model_preview
) {
    override fun ModelPreviewBinding.bind() {
        rootLayout.setBackgroundResource(menuCategoryEntity.previewImage)
        titleText.text = root.context.getString(menuCategoryEntity.name)
        rootLayout.setOnClickListener {
            onClick(menuCategoryEntity.category)
        }
    }
}