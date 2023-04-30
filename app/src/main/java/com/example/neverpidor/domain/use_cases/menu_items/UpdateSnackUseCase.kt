package com.example.neverpidor.domain.use_cases.menu_items

import com.example.neverpidor.data.network.dto.snack.SnackRequest
import com.example.neverpidor.domain.repositories.MenuItemsRepository

class UpdateSnackUseCase(
    private val repository: MenuItemsRepository
) {
    suspend operator fun invoke(snackId: String, snackRequest: SnackRequest): String {
        val response = repository.updateApiSnack(snackId, snackRequest)

        response?.let {
            repository.updateDatabaseSnack(snackId, snackRequest)
            return it.msg
        } ?: return "Проверьте подключение к интернету!"
    }
}