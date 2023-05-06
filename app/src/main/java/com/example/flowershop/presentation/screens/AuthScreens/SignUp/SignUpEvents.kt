package com.example.flowershop.presentation.screens.AuthScreens.SignUp

sealed class SignUpEvents {
    data class EnterUsername(val value: String): SignUpEvents()
    data class EnterMail(val value: String) : SignUpEvents()
    data class EnterPassword(val value: String) : SignUpEvents()
    data class ChangePasswordVisibility(val visibility: Boolean) : SignUpEvents()
    data class EnterRepeatedPassword(val value: String) : SignUpEvents()
    data class ChangeRepeatedPasswordVisibility(val visibility: Boolean): SignUpEvents()
}
