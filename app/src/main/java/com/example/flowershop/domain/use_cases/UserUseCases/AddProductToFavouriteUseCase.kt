package com.example.flowershop.domain.use_cases.UserUseCases

import com.example.flowershop.domain.model.Product
import com.example.flowershop.domain.model.ProductWithCount
import com.example.flowershop.domain.repository.UserRepository
import javax.inject.Inject

class AddProductToFavouriteUseCase @Inject constructor(
    private val repository: UserRepository
){
    operator fun invoke(product: Product, userId: Int) = repository.addProductToFavourite(product, userId)
}