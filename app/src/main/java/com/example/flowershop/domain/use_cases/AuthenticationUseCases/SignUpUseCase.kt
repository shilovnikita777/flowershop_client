package com.example.flowershop.domain.use_cases.AuthenticationUseCases

import com.example.flowershop.domain.repository.AuthenticationRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val repository : AuthenticationRepository
) {
    operator fun invoke(mail: String, username: String,password: String) = repository.signUp(mail,username,password)
}

