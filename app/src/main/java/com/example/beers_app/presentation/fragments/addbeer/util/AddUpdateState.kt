package com.example.beers_app.presentation.fragments.addbeer.util

import com.example.beers_app.domain.model.DomainItem

data class AddUpdateState(
    val mode: AddUpdateMode = AddUpdateMode.ADD,
    val validationModel: ValidationModel = ValidationModel(),
    val addUpdateErrorFields: AddUpdateErrorFields = AddUpdateErrorFields(),
    val isButtonEnabled: Boolean = false,
    val mainItem: DomainItem = DomainItem()
)