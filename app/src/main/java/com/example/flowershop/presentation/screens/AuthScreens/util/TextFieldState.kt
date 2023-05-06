package com.example.flowershop.presentation.screens.AuthScreens.util

data class TextFieldState(
    var text: String = "",
    var isValid : Boolean = true,
    var msg : String? = null,
    var isPasswordHidden: Boolean = true
)