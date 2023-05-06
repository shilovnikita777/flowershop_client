package com.example.flowershop.domain.use_cases.UserUseCases

import com.example.flowershop.domain.repository.UserRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetUserMainInfoUseCase @Inject constructor(
    private val repository: UserRepository
){
    operator fun invoke(userId: Int) = repository.getUserMainInfo(userId)
}