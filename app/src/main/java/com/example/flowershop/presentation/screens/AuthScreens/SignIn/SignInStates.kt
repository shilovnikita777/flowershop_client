package com.example.flowershop.presentation.screens.AuthScreens.SignIn

import com.example.flowershop.presentation.screens.AuthScreens.util.TextFieldState

data class SignInStates(
    val mail : TextFieldState = TextFieldState(),
    val password : TextFieldState = TextFieldState()
)