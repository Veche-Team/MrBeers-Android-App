package com.example.neverpidor.data.database.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class MenuItemAndUsers(
    @Embedded
    val menuItem: MenuItemEntity,
    @Relation(
        parentColumn = "UID",
        entityColumn = "phoneNumber",
        associateBy = Junction(
            value = UserMenuItemLikes::class,
            parentColumn = "UID",
            entityColumn = "phoneNumber"
        )
    )
    val users: List<UserEntity>
)
