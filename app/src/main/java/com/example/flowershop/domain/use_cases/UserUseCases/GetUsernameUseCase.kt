package com.example.flowershop.domain.use_cases.UserUseCases

import com.example.flowershop.domain.repository.UserRepository
import javax.inject.Inject

class GetUsernameUseCase @Inject constructor(
    private val repository: UserRepository
){
    operator fun invoke(userId: Int) = repository.getUsername(userId)
}