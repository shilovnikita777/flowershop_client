package com.example.flowershop.domain.use_cases.UserUseCases

import com.example.flowershop.data.helpers.Response
import com.example.flowershop.domain.model.ProductInBag
import com.example.flowershop.domain.model.User
import com.example.flowershop.domain.repository.UserRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAuthorBouquetByIdUseCase @Inject constructor(
    private val repository: UserRepository
){
    operator fun invoke(id: Int) = repository.getAuthorBouquetById(id).map {
        when(it) {
            is Response.Error -> {
                it
            }
            is Response.Loading -> {
                it
            }
            is Response.Success -> {
                Response.Success(
                    ProductInBag(
                    productWithCount = it.data
                ))
            }
        }
    }
}