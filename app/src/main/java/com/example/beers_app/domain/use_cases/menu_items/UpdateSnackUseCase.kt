package com.example.beers_app.domain.use_cases.menu_items

import android.content.Context
import com.example.beers.R
import com.example.beers_app.data.network.dto.snack.SnackRequest
import com.example.beers_app.domain.repositories.MenuItemsRepository

class UpdateSnackUseCase(
    private val repository: MenuItemsRepository,
    private val context: Context
) {
    suspend operator fun invoke(snackId: String, snackRequest: SnackRequest): String {
        val response = repository.updateApiSnack(snackId, snackRequest)

        response?.let {
            repository.updateDatabaseSnack(snackId, snackRequest)
            return it.msg
        } ?: return context.getString(R.string.check_connection)
    }
}