package com.example.beers_app.presentation.fragments.profile.util

data class ProfileButtonsEnabled(
    val isChangeNameEnabled: Boolean = false,
    val isChangePasswordEnabled: Boolean = false,
    val isDeleteUserEnabled: Boolean = false
)