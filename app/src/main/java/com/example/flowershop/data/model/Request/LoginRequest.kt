package com.example.flowershop.data.model.Request

import com.example.flowershop.data.model.Response.UserInfo
import com.google.gson.annotations.SerializedName

class LoginRequest(
    val email: String,
    val password: String
)