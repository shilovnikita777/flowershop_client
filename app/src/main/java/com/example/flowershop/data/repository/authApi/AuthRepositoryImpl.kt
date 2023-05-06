package com.example.flowershop.data.repository.authApi

import com.example.flowershop.data.helpers.Response
import com.example.flowershop.data.helpers.apiRequestFlow
import com.example.flowershop.data.model.Request.LoginRequest
import com.example.flowershop.data.model.Request.RegisterRequest
import com.example.flowershop.data.network.AuthApiService
import com.example.flowershop.domain.repository.AuthenticationRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApiService: AuthApiService
) : AuthenticationRepository{
    override fun isUserAuthenticated(token: String) = apiRequestFlow {
        val headerToken = "Bearer $token"
        authApiService.isAuth(headerToken)
    }

    override fun signIn(email: String, password: String) = apiRequestFlow {
        val loginData = LoginRequest(
            email = email,
            password = password
        )
        authApiService.login(loginData)
    }

    override fun signUp(email: String, username: String, password: String) = apiRequestFlow {
        val registerData = RegisterRequest(
            username = username,
            email = email,
            password = password
        )
        authApiService.register(registerData)
    }
}