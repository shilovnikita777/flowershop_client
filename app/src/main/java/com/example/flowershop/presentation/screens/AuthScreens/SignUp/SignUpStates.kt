package com.example.flowershop.presentation.screens.AuthScreens.SignUp

import com.example.flowershop.presentation.screens.AuthScreens.util.TextFieldState

data class SignUpStates(
    val username : TextFieldState = TextFieldState(),
    val mail : TextFieldState = TextFieldState(),
    val password : TextFieldState = TextFieldState(),
    val repeatedPassword : TextFieldState = TextFieldState()
)