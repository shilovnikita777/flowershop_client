package com.example.flowershop.domain.use_cases.UserUseCases

import com.example.flowershop.domain.repository.UserRepository
import javax.inject.Inject

class RemoveProductFromBagUseCase @Inject constructor(
    private val repository: UserRepository
){
    operator fun invoke(productId: Int, isAuthor : Boolean?) = repository.removeProductFromBag(productId,isAuthor)
}