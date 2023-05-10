package com.example.flowershop.presentation.screens.BagScreens.OrderInfo

import com.example.flowershop.presentation.screens.AuthScreens.util.TextFieldState

data class OrderInfoStates(
    val phone : TextFieldState = TextFieldState(),
    val address : TextFieldState = TextFieldState(),
    val fullname : TextFieldState = TextFieldState()
)