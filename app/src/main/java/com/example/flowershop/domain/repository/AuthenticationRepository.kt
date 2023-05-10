package com.example.flowershop.domain.repository

import com.example.flowershop.data.helpers.Response
import com.example.flowershop.data.model.Response.LoginResponse
import com.example.flowershop.data.model.Response.RegisterResponse
import com.example.flowershop.data.model.Response.isAuthResponse
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {

    fun isUserAuthenticated(token : String) : Flow<Response<isAuthResponse>>

    fun signIn(email: String, password: String) : Flow<Response<LoginResponse>>

    fun signUp(email: String, username: String,password: String) : Flow<Response<RegisterResponse>>

    fun logout(token : String) : Flow<Response<Boolean>>
}