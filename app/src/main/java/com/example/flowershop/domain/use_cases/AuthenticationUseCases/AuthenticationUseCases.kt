package com.example.flowershop.domain.use_cases.AuthenticationUseCases

data class AuthenticationUseCases(
    val signInUseCase: SignInUseCase,
    val signUpUseCase: SignUpUseCase,
    val isUserAuthenticatedUseCase: IsUserAuthenticatedUseCase
)