package com.example.neverpidor.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import java.util.UUID

@Entity(
    tableName = "likes",
    primaryKeys = ["phoneNumber", "UID"],
    indices = [Index("phoneNumber"), Index("UID")],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = arrayOf("phoneNumber"),
            childColumns = arrayOf("phoneNumber"),
            onDelete = CASCADE
        ),
        ForeignKey(
            entity = MenuItemEntity::class,
            parentColumns = arrayOf("UID"),
            childColumns = arrayOf("UID"),
            onDelete = CASCADE
        )
    ]
)
data class UserMenuItemLikes(
    val phoneNumber: String = "",
    val UID: String = ""
)
