package com.example.flowershop.domain.use_cases.AuthenticationUseCases

import com.example.flowershop.domain.repository.AuthenticationRepository
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val repository : AuthenticationRepository
) {
    operator fun invoke(mail: String, password: String) = repository.signIn(mail, password)
}