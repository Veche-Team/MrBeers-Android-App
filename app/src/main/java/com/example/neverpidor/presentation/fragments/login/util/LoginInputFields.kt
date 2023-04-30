package com.example.neverpidor.presentation.fragments.login.util

data class LoginInputFields(
    val number: String = "",
    val password: CharArray = charArrayOf()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LoginInputFields

        if (number != other.number) return false
        if (!password.contentEquals(other.password)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = number.hashCode()
        result = 31 * result + password.contentHashCode()
        return result
    }
}