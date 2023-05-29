package com.example.flowershop.domain.use_cases.UserUseCases

import com.example.flowershop.domain.model.Product
import com.example.flowershop.domain.repository.UserRepository
import javax.inject.Inject

class IsProductInFavouriteUseCase @Inject constructor(
    private val repository: UserRepository
){
    operator fun invoke(product: Product) = repository.isProductInFavourite(product)
}