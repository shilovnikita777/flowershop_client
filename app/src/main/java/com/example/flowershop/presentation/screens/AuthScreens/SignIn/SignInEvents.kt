package com.example.flowershop.presentation.screens.AuthScreens.SignIn

sealed class SignInEvents {
    data class EnterMail(val value: String) : SignInEvents()
    data class EnterPassword(val value: String) : SignInEvents()
    data class ChangePasswordVisibility(val visibility: Boolean) : SignInEvents()
}
