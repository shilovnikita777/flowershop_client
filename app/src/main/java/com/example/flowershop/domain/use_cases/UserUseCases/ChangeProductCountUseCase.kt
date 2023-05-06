package com.example.flowershop.domain.use_cases.UserUseCases

import com.example.flowershop.domain.model.ProductWithCount
import com.example.flowershop.domain.repository.UserRepository
import javax.inject.Inject

class ChangeProductCountUseCase @Inject constructor(
    private val repository: UserRepository
){
    operator fun invoke(product: ProductWithCount, count: Int, isAuthor: Boolean?) = repository.changeProductCount(product, count, isAuthor)
}