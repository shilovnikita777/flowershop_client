package com.example.flowershop.domain.use_cases.ProductsUseCases

import com.example.flowershop.domain.repository.ProductsRepository
import javax.inject.Inject

class GetFlowersUseCase @Inject constructor(
    private val repository: ProductsRepository
){
    operator fun invoke() = repository.getFlowers()
}