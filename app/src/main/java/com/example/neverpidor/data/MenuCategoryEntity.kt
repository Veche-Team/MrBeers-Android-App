package com.example.neverpidor.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.neverpidor.R

data class MenuCategoryEntity(
    val id: Int,
    @StringRes
    val name: Int,
    @DrawableRes
    val previewImage: Int,
    val category: Category
) {
    companion object {

        fun getCategories() = listOf(
            MenuCategoryEntity(0, R.string.beer, R.drawable.beer_preview, Category.Beer),
            MenuCategoryEntity(1, R.string.snacks, R.drawable.snacks_preview, Category.Snack)
        )
    }
}
enum class Category {
    Beer, Snack;

    companion object {
        fun toCategory(categoryString: String): Category {
            return if (categoryString == Beer.toString()) Beer else Snack

        }
    }
}