package com.example.neverpidor.domain.use_cases.menu_validation

class MenuValidationUseCases(
    val titleValidationUseCase: TitleValidationUseCase,
    val descriptionValidationUseCase: DescriptionValidationUseCase,
    val typeValidationUseCase: TypeValidationUseCase,
    val priceValidationUseCase: PriceValidationUseCase,
    val alcPercentageValidationUseCase: AlcPercentageValidationUseCase,
    val salePercentageValidationUseCase: SalePercentageValidationUseCase,
    val weightValidationUseCase: WeightValidationUseCase
)