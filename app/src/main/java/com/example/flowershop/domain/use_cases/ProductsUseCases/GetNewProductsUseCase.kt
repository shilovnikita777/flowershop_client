package com.example.flowershop.domain.use_cases.ProductsUseCases

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.flowershop.domain.model.Product
import com.example.flowershop.domain.repository.ProductsRepository
import com.example.flowershop.data.helpers.Response
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetNewProductsUseCase @Inject constructor(
    private val repository : ProductsRepository
) {
    operator fun invoke() = repository.getNewProducts().map { response ->
        when(response) {
            is Response.Error -> {
                response
            }
            is Response.Loading -> {
                response
            }
            is Response.Success -> {
                val list = mutableListOf<Triple<Product, MutableState<Response<Boolean>>, MutableState<Response<Boolean>>>>()
                response.data.forEach {
                    list.add(
                        Triple(
                            first = it,
                            second = mutableStateOf(Response.Success(false)),
                            third = mutableStateOf(Response.Success(false))
                        )
                    )
                }
                Response.Success(list)
            }
        }
    }
}
