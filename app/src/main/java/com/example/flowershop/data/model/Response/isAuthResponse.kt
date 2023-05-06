package com.example.flowershop.data.model.Response

import com.google.gson.annotations.SerializedName

class isAuthResponse(
    val isAuth : Boolean,
    val userId: Int
)