package com.example.flowershop.presentation.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.flowershop.domain.model.Product

class ProductWithCountState(
    val product: Product = Product(),
    val count: MutableState<Int> = mutableStateOf(1)
)