package com.example.flowershop.presentation.screens.BagScreens.OrderInfo

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.flowershop.data.helpers.Response
import com.example.flowershop.domain.model.Promocode
import com.example.flowershop.presentation.screens.AuthScreens.util.TextFieldState
import kotlinx.coroutines.Job

data class OrderInfoStates(
    val phone : TextFieldState = TextFieldState(),
    val address : TextFieldState = TextFieldState(),
    val fullname : TextFieldState = TextFieldState(),
    val promocode : TextFieldState = TextFieldState(),
    var searchJob: Job? = null,
    val usePromoResponse : Response<Promocode>? = null,
    var usedPromocode : String? = null,
    val orderSumm : Int = 0
)