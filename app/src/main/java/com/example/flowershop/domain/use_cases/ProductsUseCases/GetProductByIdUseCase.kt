package com.example.flowershop.domain.use_cases.ProductsUseCases

import com.example.flowershop.data.helpers.Response
import com.example.flowershop.domain.model.ProductInBag
import com.example.flowershop.domain.model.ProductWithCount
import com.example.flowershop.domain.repository.ProductsRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetProductByIdUseCase @Inject constructor(
    private val repository : ProductsRepository
) {
    operator fun invoke(productId: Int, type: String) = repository.getProductById(productId, type).map {
        when(it) {
            is Response.Error -> {
                it
            }
            is Response.Loading -> {
                it
            }
            is Response.Success -> {
                Response.Success(ProductInBag(
                    productWithCount = it.data
                ))
            }
        }
    }
}