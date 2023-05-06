package com.example.flowershop.domain.use_cases.UserUseCases

import com.example.flowershop.domain.repository.UserRepository
import javax.inject.Inject

class IsAuthorBouquetInBagUseCase @Inject constructor(
    private val repository: UserRepository
){
    operator fun invoke(productId: Int) = repository.isAuthorInBag(productId)
}