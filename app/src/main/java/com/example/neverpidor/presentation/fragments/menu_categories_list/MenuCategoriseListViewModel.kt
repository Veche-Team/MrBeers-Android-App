package com.example.neverpidor.presentation.fragments.menu_categories_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neverpidor.data.providers.MenuCategory
import com.example.neverpidor.domain.use_cases.menu_items.SetCurrentCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MenuCategoriseListViewModel @Inject constructor(
    private val setCurrentCategoryUseCase: SetCurrentCategoryUseCase
) : ViewModel() {

    suspend fun setItem(menuCategory: MenuCategory) =
        withContext(viewModelScope.coroutineContext) {
            setCurrentCategoryUseCase(menuCategory)
        }
}