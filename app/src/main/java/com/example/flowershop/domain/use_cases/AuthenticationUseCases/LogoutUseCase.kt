package com.example.flowershop.domain.use_cases.AuthenticationUseCases

import com.example.flowershop.domain.repository.AuthenticationRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val repository : AuthenticationRepository
) {
    operator fun invoke(token : String) = repository.logout(token)
}