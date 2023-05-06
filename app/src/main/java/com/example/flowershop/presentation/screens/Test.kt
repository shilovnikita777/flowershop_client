package com.example.flowershop.presentation.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.lang.reflect.Modifier

@Composable
fun Test() {

    val goals = remember { mutableStateListOf<MutableState<String>>() }

    Column() {
        goals.forEach { goal ->
            OutlinedTextField(
                value = goal.value,
                onValueChange = {
                    goal.value = it
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.Black
                )
            )
        }
        OutlinedButton(
            onClick = {
                goals.add(
                    mutableStateOf("")
                )
                Log.d("xd",goals.size.toString())
            },
            border = BorderStroke(2.dp, Color.Blue)
        ) {
            Text(
                text = "Добавить"
            )
        }
    }
}