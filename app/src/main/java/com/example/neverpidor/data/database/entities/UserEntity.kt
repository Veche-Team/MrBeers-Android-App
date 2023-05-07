package com.example.neverpidor.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.neverpidor.domain.model.User
import com.example.neverpidor.util.Constants.USERS_TABLE_NAME

@Entity(tableName = USERS_TABLE_NAME, indices = [Index("phoneNumber")])
data class UserEntity(
    @PrimaryKey(autoGenerate = false)
    val phoneNumber: String = "",
    val name: String = "",
    val hash: String = "",
    val salt: String = ""
) {
    fun toUser(): User {
        return User(
            phoneNumber = phoneNumber,
            name = name,
            role = if (this.phoneNumber == "777") User.Role.Admin else User.Role.Customer
        )
    }
}