package com.example.neverpidor.domain.use_cases.menu_items

import android.util.Log
import com.example.neverpidor.data.network.dto.snack.SnackRequest
import com.example.neverpidor.domain.repositories.MenuItemsRepository

class AddSnackUseCase(
    val repository: MenuItemsRepository
) {
    suspend operator fun invoke(snackRequest: SnackRequest): String {
        val response = repository.addApiSnack(snackRequest)
        response?.let {
            repository.addSnackToDatabase(it)
            Log.e("ADD", "Snack added!!")
            return it.msg
        } ?: return "Проверьте подключение к интернету!"
    }
}