package com.example.flowershop.presentation.mapper

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.graphics.Color
import com.example.flowershop.domain.mapper.Mapper
import com.example.flowershop.domain.model.Promocode
import com.example.flowershop.presentation.model.PromocodeUI
import com.example.flowershop.ui.theme.*
import javax.inject.Inject

class PromocodeMapper @Inject constructor(): Mapper<Promocode, PromocodeUI>() {
    override fun map(from: Promocode) = from.run {
        val colors = textColors.first()
        PromocodeUI(
            title = title,
            description = description,
            value = value,
            amount = amount,
            image = image,

            amountColor = colors.first,
            titleColor = colors.second,
            valueColor = colors.first
        )
    }
}

val textColors = listOf(
    Pair(CustomDarkGrey, CustomDarkGrey),
    Pair(CustomGreen, CustomLightGreen),
    Pair(CustomRed, CustomLightRed),
    Pair(CustomCyan, CustomLightCyan),
    Pair(CustomPurple, CustomLightPurple),
    Pair(CustomOrange, CustomLightOrange)
)