package com.example.flowershop.domain.use_cases.UserUseCases

import com.example.flowershop.domain.model.ProductInBag
import com.example.flowershop.domain.model.ProductWithCount
import com.example.flowershop.domain.repository.UserRepository
import javax.inject.Inject

class UpdateProductInBagUseCase @Inject constructor(
    private val repository: UserRepository
){
    operator fun invoke(product: ProductWithCount, userId: Int) = repository.updateProductInBag(product, userId)
}