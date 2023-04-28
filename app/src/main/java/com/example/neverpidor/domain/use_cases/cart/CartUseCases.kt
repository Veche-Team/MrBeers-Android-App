package com.example.neverpidor.domain.use_cases.cart

class CartUseCases(
    val getCartListUseCase: GetCartListUseCase,
    val getUserCartFlowUseCase: GetUserCartFlowUseCase,
    val plusItemInCart: PlusItemInCartUseCase,
    val minusItemInCart: MinusItemInCartUseCase,
    val clearUserCart: ClearUserCartUseCase,
    val isItemInCartUseCase: IsItemInCartUseCase,
    val addItemToCartUseCase: AddItemToCartUseCase
)