package com.example.flowershop.domain.use_cases.ProductsUseCases

import com.example.flowershop.domain.repository.ProductsRepository
import com.example.flowershop.presentation.model.SearchConditions
import javax.inject.Inject

class GetFlowersUseCase @Inject constructor(
    private val repository: ProductsRepository
){
    operator fun invoke(searchConditions: SearchConditions) = repository.getFlowers(searchConditions)
}