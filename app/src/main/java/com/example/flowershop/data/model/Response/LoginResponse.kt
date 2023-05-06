package com.example.flowershop.data.model.Response

import com.google.gson.annotations.SerializedName


class LoginResponse(
    val token : String,
    @SerializedName("user")
    val userInfo: UserInfo
)