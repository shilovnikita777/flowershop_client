package com.example.flowershop.domain.use_cases.UserUseCases

import com.example.flowershop.data.helpers.Response
import com.example.flowershop.domain.model.ProductInBag
import com.example.flowershop.domain.model.User
import com.example.flowershop.domain.repository.UserRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetBagByUserIdUseCase @Inject constructor(
    private val repository: UserRepository
){
    operator fun invoke(id: Int) = repository.getBagByUserId(id).map { response ->
        when(response) {
            is Response.Loading -> {
                response
            }
            is Response.Error -> {
                response
            }
            is Response.Success -> {
                Response.Success(
                    User.Bag(
                        products = response.data.map { productWithCount ->
                            ProductInBag(
                                productWithCount = productWithCount
                            )
                        }.toMutableList()
                    )
                )
            }
        }
    }
}