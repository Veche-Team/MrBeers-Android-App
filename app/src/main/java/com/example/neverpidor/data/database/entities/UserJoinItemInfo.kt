package com.example.neverpidor.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index

@Entity(
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
            entity = ItemInfo::class,
            parentColumns = arrayOf("UID"),
            childColumns = arrayOf("UID"),
            onDelete = CASCADE
        )
    ]
)
data class UserJoinItemInfo(
    val phoneNumber: Long,
    val UID: String
)
