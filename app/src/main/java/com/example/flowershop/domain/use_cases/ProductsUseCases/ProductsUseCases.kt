package com.example.flowershop.domain.use_cases.ProductsUseCases

data class ProductsUseCases(
    val getAllProductsUseCase: GetAllProductsUseCase,
    val getCategoriesUseCase: GetCategoriesUseCase,
    val getNewProductsUseCase: GetNewProductsUseCase,
    val getPopularProductsUseCase: GetPopularProductsUseCase,
    val getPromocodesUseCase: GetPromocodesUseCase,
    val getProductsByCategoryUseCase: GetProductsByCategoryUseCase,
    val getProductByIdUseCase: GetProductByIdUseCase,
    val getDecorationsUseCase: GetDecorationsUseCase,
    val getTablesUseCase: GetTablesUseCase,
    val getFlowersUseCase: GetFlowersUseCase,
    val getProductsUseCase: GetProductsUseCase
)
