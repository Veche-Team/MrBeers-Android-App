package com.example.beers_app.presentation.fragments.profile.util

sealed class VMStringResource {
    object NameChanged: VMStringResource()
    object PasswordChanged: VMStringResource()
    object AccountDeleted: VMStringResource()
}
