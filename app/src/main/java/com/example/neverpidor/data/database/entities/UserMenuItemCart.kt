package com.example.neverpidor.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.example.neverpidor.util.Constants.CART_TABLE_NAME

@Entity(
    tableName = CART_TABLE_NAME,
    primaryKeys = ["phoneNumber", "UID"],
    indices = [Index("phoneNumber"), Index("UID")],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = arrayOf("phoneNumber"),
            childColumns = arrayOf("phoneNumber"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MenuItemEntity::class,
            parentColumns = arrayOf("UID"),
            childColumns = arrayOf("UID"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UserMenuItemCart(
    val phoneNumber: String = "",
    val UID: String = "",
    val quantity: Int = 1
)