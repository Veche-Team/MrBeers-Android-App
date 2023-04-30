package com.example.neverpidor.presentation.fragments.register.util

data class RegisterInputFields(
    val number: String = "",
    val name: String = "",
    val password: CharArray = charArrayOf(),
    val repeatPassword: CharArray = charArrayOf()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RegisterInputFields

        if (number != other.number) return false
        if (name != other.name) return false
        if (!password.contentEquals(other.password)) return false
        if (!repeatPassword.contentEquals(other.repeatPassword)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = number.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + password.contentHashCode()
        result = 31 * result + repeatPassword.contentHashCode()
        return result
    }
}