package com.example.beers_app.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.beers_app.data.database.type_converters.CategoryConverter
import com.example.beers_app.data.common.MenuCategory
import com.example.beers_app.util.Constants.ITEMS_TABLE_NAME

@Entity(tableName = ITEMS_TABLE_NAME, indices = [Index("UID")])
@TypeConverters(CategoryConverter::class)
data class MenuItemEntity(
    @PrimaryKey(autoGenerate = false)
    val UID: String,
    val name: String,
    val description: String,
    val type: String,
    val price: Double,
    val alcPercentage: Double?,
    val category: MenuCategory,
    val salePercentage: Double = 0.0,
    val weight: Double = 100.0,
    val imageUrl: String?
)
