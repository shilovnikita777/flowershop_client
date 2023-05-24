package com.example.flowershop.domain.model

import java.time.LocalDate
import java.util.*

class User(
    val id: Int,
    var data: Data,
    val bag: Bag = Bag(),
    val order: Order? = null,
    val orderHistory: OrderHistory = OrderHistory(),
    val favourite: Favourite = Favourite(),
    val promocodes: Promocodes = Promocodes()
){

    data class Data(
        val username: String,
        val email: String,
        val image: String
    )

    class Bag(
        var products: MutableList<ProductInBag> = emptyList<ProductInBag>().toMutableList(),
    ) {
        val total : Int
            get() = products.sumOf {
                it.totalPrice
            }
    }

    class Order(
        val id: Int = -1,
        val products: MutableList<ProductInBag>,
        val date: LocalDate,
        val phone: String,
        val address: String,
        val fullname: String,
        val summ : Int,
        val promocodeId : Int? = null
    )

    class OrderHistory(
        val orders: MutableList<Order> = emptyList<Order>().toMutableList()
    )

    class Favourite(
        val products: MutableList<Product> = emptyList<Product>().toMutableList()
    )

    class Promocodes(
        val promocodes: MutableList<Promocode> = emptyList<Promocode>().toMutableList()
    )
}
