package com.example.neverpidor.model.snack

data class SnackResponse(
    val deletedSnack: DeletedSnack = DeletedSnack(),
    val msg: String = ""
)