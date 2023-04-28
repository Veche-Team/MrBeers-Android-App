package com.example.neverpidor.data.database.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class UserAndItemsInCart(
    @Embedded
    val user: UserEntity,
    @Relation(
        parentColumn = "phoneNumber",
        entityColumn = "UID",
        associateBy = Junction(
            value = UserMenuItemCart::class,
            parentColumn = "phoneNumber",
            entityColumn = "UID"
        )
    )
    val menuItems: List<MenuItemEntity>
)
