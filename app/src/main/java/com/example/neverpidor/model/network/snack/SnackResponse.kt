package com.example.neverpidor.model.network.snack

data class SnackResponse(
    val deletedSnack: DeletedSnack = DeletedSnack(),
    val msg: String = ""
)