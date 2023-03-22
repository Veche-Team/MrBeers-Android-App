package com.example.neverpidor.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "beers")
data class BeerEntity(
    @PrimaryKey(autoGenerate = false)
    val UID: String,
    val alcPercentage: Double,
    val description: String,
    val name: String,
    val price: Double,
    val type: String,
    val volume: Double,
    val isInCart: Boolean = false,
    val isFaved: Boolean = false
)