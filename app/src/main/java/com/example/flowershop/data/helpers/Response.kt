package com.example.flowershop.data.helpers

sealed class Response<out T>{
    object Loading : Response<Nothing>()

    class Success<out T>(
        val data : T
    ): Response<T>()

    data class Error(
        val message :String
    ): Response<Nothing>()
}

