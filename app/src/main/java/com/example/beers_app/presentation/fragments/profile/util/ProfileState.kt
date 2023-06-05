package com.example.beers_app.presentation.fragments.profile.util

data class ProfileState(
    val inputFields: ProfileInputFields = ProfileInputFields(),
    val fieldErrors: ProfileFieldErrors = ProfileFieldErrors(),
    val isButtonsEnabled: ProfileButtonsEnabled = ProfileButtonsEnabled()
)
