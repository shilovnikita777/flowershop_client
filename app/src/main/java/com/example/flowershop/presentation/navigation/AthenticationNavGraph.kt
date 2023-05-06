package com.example.flowershop.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.flowershop.presentation.screens.AuthScreens.SignIn.SignInScreen
import com.example.flowershop.screens.SignUpScreen

fun NavGraphBuilder.authenticationNavGraph(navController : NavHostController) {
    navigation(
        route = Graph.AUTHENTICATION.route,
        startDestination = AuthenticationNavRoute.SignIn.route
    ) {
        composable(
            route = AuthenticationNavRoute.SignIn.route
        ) {
            SignInScreen(navController)
        }

        composable(
            route = AuthenticationNavRoute.SignUp.route
        ) {
            SignUpScreen(navController)
        }
    }
}

sealed class AuthenticationNavRoute(
    val route: String
) {
    object SignIn : AuthenticationNavRoute(route = "sign_in")
    object SignUp : AuthenticationNavRoute(route = "sign_up")
}