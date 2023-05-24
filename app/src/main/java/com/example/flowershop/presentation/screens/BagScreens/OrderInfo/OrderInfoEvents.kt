package com.example.flowershop.presentation.screens.BagScreens.OrderInfo

sealed class OrderInfoEvents {
    data class EnterPhone(val value: String): OrderInfoEvents()
    data class EnterAddress(val value: String) : OrderInfoEvents()
    data class EnterFN(val value: String) : OrderInfoEvents()
    data class EnterPromocode(val value: String) : OrderInfoEvents()
}