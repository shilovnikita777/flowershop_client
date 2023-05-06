package com.example.flowershop.data.network

import com.example.flowershop.data.model.Request.LoginRequest
import com.example.flowershop.data.model.Request.RegisterRequest
import com.example.flowershop.data.model.Response.LoginResponse
import com.example.flowershop.data.model.Response.RegisterResponse
import com.example.flowershop.data.model.Response.isAuthResponse
import retrofit2.Response
import retrofit2.http.*

interface AuthApiService {
    @POST("auth/register")
    suspend fun register(
        @Body registerData : RegisterRequest
    ) : Response<RegisterResponse>

    @POST("auth/login")
    suspend fun login(
        @Body loginData : LoginRequest
    ) : Response<LoginResponse>

    @GET("auth/isauth")
    suspend fun isAuth(
        @Header("Authorization") token : String
    ) : Response<isAuthResponse>
}