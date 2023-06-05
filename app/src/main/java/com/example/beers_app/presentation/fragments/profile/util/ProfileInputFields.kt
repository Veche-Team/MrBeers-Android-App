package com.example.beers_app.presentation.fragments.profile.util

data class ProfileInputFields(
    val changeNameField: String = "",
    val oldPasswordField: CharArray = charArrayOf(),
    val newPasswordField: CharArray = charArrayOf(),
    val repeatNewPasswordField: CharArray = charArrayOf(),
    val deleteUserPasswordField: CharArray = charArrayOf()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProfileInputFields

        if (changeNameField != other.changeNameField) return false
        if (!oldPasswordField.contentEquals(other.oldPasswordField)) return false
        if (!newPasswordField.contentEquals(other.newPasswordField)) return false
        if (!repeatNewPasswordField.contentEquals(other.repeatNewPasswordField)) return false
        if (!deleteUserPasswordField.contentEquals(other.deleteUserPasswordField)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = changeNameField.hashCode()
        result = 31 * result + oldPasswordField.contentHashCode()
        result = 31 * result + newPasswordField.contentHashCode()
        result = 31 * result + repeatNewPasswordField.contentHashCode()
        result = 31 * result + deleteUserPasswordField.contentHashCode()
        return result
    }

}
