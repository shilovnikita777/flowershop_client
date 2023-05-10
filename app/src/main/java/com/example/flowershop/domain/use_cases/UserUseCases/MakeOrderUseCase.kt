package com.example.flowershop.domain.use_cases.UserUseCases

import com.example.flowershop.domain.model.ProductWithCount
import com.example.flowershop.domain.model.User
import com.example.flowershop.domain.repository.UserRepository
import javax.inject.Inject

class MakeOrderUseCase @Inject constructor(
    private val repository: UserRepository
){
    operator fun invoke(orderData: User.Order) = repository.makeOrder(orderData)
}