package com.example.neverpidor.domain.use_cases.menu_items

class MenuItemsUseCases(
    val addBeerUseCase: AddBeerUseCase,
    val addSnackUseCase: AddSnackUseCase,
    val getCurrentItemCategoryUseCase: GetCurrentItemCategoryUseCase,
    val getMenuItemByIdUseCase: GetMenuItemByIdUseCase,
    val updateBeerUseCase: UpdateBeerUseCase,
    val updateSnackUseCase: UpdateSnackUseCase,
    val getAllItemsUseCases: GetAllItemsUseCases,
    val deleteItemUseCase: DeleteItemUseCase,
    val getItemsSetUseCase: GetItemsSetUseCase
)