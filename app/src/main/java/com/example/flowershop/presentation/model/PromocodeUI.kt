package com.example.flowershop.presentation.model

import androidx.compose.ui.graphics.Color
import com.example.flowershop.domain.model.Image

data class PromocodeUI (
    val title: String,
    val description: String,
    val value: String,
    val amount: Int,
    val image : Image,

    val amountColor : Color,
    val titleColor : Color,
    val valueColor : Color
)