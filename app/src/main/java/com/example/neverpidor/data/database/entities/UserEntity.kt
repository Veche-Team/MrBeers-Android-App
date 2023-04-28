package com.example.neverpidor.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.neverpidor.domain.model.User

@Entity(tableName = "users", indices = [Index("phoneNumber")])
data class UserEntity(
    @PrimaryKey(autoGenerate = false)
    val phoneNumber: String = "",
    val name: String = "",
    val password: String = ""
) {
    fun toUser(): User {
        return User(
            phoneNumber = phoneNumber,
            name = name
        )
    }
}