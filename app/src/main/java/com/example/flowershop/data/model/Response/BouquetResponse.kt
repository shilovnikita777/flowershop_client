package com.example.flowershop.data.model.Response

import com.example.flowershop.domain.model.Decoration
import com.example.flowershop.domain.model.ProductWithCount
import com.example.flowershop.domain.model.Rating
import com.example.flowershop.domain.model.Table

class BouquetResponse (
    val id: Int,
    val price: Int,
    val image: String ,
    val name: String,
    val description: String,
    val categoriesIds: List<Int>,
    val type : String = "bouquet",
    val flowers: List<ProductWithCount> ,
    var decoration: Decoration,
    var table: Table,
    var postcard: String
)