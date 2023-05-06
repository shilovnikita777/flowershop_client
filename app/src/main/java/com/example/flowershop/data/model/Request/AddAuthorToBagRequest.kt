package com.example.flowershop.data.model.Request

import com.example.flowershop.domain.model.ProductWithCount

class AddAuthorToBagRequest(
    val productId : Int? = null,
    val flowers : List<ProductWithCount>,
    val count : Int,
    val decorationId : Int? = null,
    val tableId : Int? = null,
    val postcard : String? = null
)