package com.example.beers_app.domain.model

data class User(
    val phoneNumber: String = "",
    val name: String = "",
    val role: Role = Role.Customer
) {
    sealed class Role(val name: String) {
        object NoUser: Role("noUser")
        object Admin: Role("admin")
        object Customer: Role("customer")
    }
}
