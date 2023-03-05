package com.example.neverpidor.data

import androidx.annotation.DrawableRes
import com.example.neverpidor.R

data class MenuCategoryEntity(
    val id: Int,
    val name: String,
    @DrawableRes
    val previewImage: Int
) {
    companion object {

        fun getCategories() = listOf(
            MenuCategoryEntity(0, "Пиво", R.drawable.beer_preview),
            MenuCategoryEntity(1, "Закуски", R.drawable.snacks_preview)
        )
    }
}