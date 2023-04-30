package com.example.neverpidor.domain.use_cases.validation

class ValidationUseCases(
    val titleValidationUseCase: TitleValidationUseCase,
    val descriptionValidationUseCase: DescriptionValidationUseCase,
    val typeValidationUseCase: TypeValidationUseCase,
    val priceValidationUseCase: PriceValidationUseCase,
    val alcPercentageValidationUseCase: AlcPercentageValidationUseCase,
    val volumeValidationUseCase: VolumeValidationUseCase
) {
}