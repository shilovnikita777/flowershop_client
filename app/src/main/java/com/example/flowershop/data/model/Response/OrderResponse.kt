package com.example.flowershop.data.model.Response

import com.example.flowershop.domain.model.ProductInBag
import com.example.flowershop.domain.model.Promocode
import java.time.LocalDate

class OrderResponse (
    val id: Int,
    val products: MutableList<ProductInBag>,
    val date: LocalDate,
    val phone: String,
    val address: String,
    val fullname: String,
    val summ : Int,
    val promocode : Promocode?
)