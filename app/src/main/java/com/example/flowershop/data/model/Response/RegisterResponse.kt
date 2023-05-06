package com.example.flowershop.data.model.Response

import com.google.gson.annotations.SerializedName

class RegisterResponse(
    val token : String,
    @SerializedName("user")
    val userInfo: UserInfo
)
