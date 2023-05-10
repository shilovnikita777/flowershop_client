package com.example.flowershop.data.helpers

import android.util.Log
import com.example.flowershop.data.model.Response.ErrorResponse
import com.example.flowershop.util.Constants.API_STATUS_OK
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withTimeoutOrNull

fun<T> apiRequestFlow(call: suspend () -> retrofit2.Response<T>) : Flow<Response<T>> = flow {
    emit(Response.Loading)

    withTimeoutOrNull(10000L) {
        val response = call()
        try {
            Log.d("xd3","raw response : ${response}")
            if (response.isSuccessful) {
                response.body()?.let { data ->
                    emit(Response.Success(data))
                }
            } else {
                response.errorBody()?.let { error ->
                    error.close()
                    val parsedError: ErrorResponse = Gson().fromJson(error.charStream(),
                        ErrorResponse::class.java)
                    Log.d("xd3","error :" + parsedError.message)
                    emit(Response.Error(parsedError.message))
                }
            }
        } catch (e: Exception) {
            emit(Response.Error(e.localizedMessage ?: e.toString()))
        }
    } ?: emit(Response.Error("Нет интернет соединения"))
}.flowOn(Dispatchers.IO)