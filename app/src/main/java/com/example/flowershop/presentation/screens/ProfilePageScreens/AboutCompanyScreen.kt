package com.example.flowershop.presentation.screens.ProfilePageScreens

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flowershop.presentation.screens.common.h2

@Composable
fun AboutCompanyScreen() {
    Column(
        modifier = Modifier
            .padding(top = 48.dp, start = 24.dp, end = 24.dp, bottom = 24.dp)
    ) {
        h2(
            text = "О компании",
            color = MaterialTheme.colors.onBackground
        )
        Text(
            text = "Данный проект представляет собой выпускную квалификационную работу студента " +
                    "Института компьютерных технологий и информационной безопасности кафедры МОП ЭВМ гр. КТбо4-7 Шилова Никиты Андреевича",
            style = MaterialTheme.typography.subtitle1.copy(fontSize = 16.sp),
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier
                .padding(top = 16.dp)
        )
    }
}