package com.example.flowershop.data.model.Request

class AddToBagRequest(
    val productId : Int,
    val count : Int,
    val decorationId : Int? = null,
    val tableId : Int? = null,
    val postcard : String? = null
)