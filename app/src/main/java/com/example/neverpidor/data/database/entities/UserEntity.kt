package com.example.neverpidor.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = false)
    val phoneNumber: Long,
    val name: String,
    val password: String
)
