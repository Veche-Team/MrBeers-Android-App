package com.example.neverpidor.data.database.type_converters

import android.util.Log
import androidx.room.TypeConverter
import com.example.neverpidor.data.providers.MenuCategory

class CategoryConverter {

    @TypeConverter
    fun categoryToString(category: MenuCategory): String {
        return when (category) {
            MenuCategory.BeerCategory -> "Beer"
            MenuCategory.SnackCategory -> "Snack"
        }
    }

    @TypeConverter
    fun stringToCategory(s: String): MenuCategory {
        Log.d("CONVERTER", s)
        return when {
            s.contains("Beer") -> MenuCategory.BeerCategory
            s.contains("Snack") -> MenuCategory.SnackCategory
            else -> throw Exception("Converter's gone mad")
        }
    }
}