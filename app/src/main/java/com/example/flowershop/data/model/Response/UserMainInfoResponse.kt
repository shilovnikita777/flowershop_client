package com.example.flowershop.data.model.Response

import com.google.gson.annotations.SerializedName

class UserMainInfoResponse(
    val username : String,
    val email: String,
    val image: String?
)