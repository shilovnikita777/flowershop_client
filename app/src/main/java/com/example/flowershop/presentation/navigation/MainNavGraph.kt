package com.example.flowershop.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.flowershop.screens.ConstructorScreen
import com.example.flowershop.presentation.screens.MainPageScreens.HomeScreen
import com.example.flowershop.util.Constants.NO_PRODUCT_CONSTANT

fun NavGraphBuilder.mainNavGraph(navController : NavHostController) {
    navigation(
        route = Graph.MAIN.route,
        startDestination = MainNavRoute.MainInfo.route
    ) {
        composable(
            route = MainNavRoute.MainInfo.route
        ) {
            HomeScreen(navController)
        }

        composable(
            route = MainNavRoute.Constructor.route,
            arguments = listOf(
                navArgument(ARGUMENT_PRODUCT_ID) {
                    type = NavType.IntType
                    defaultValue = NO_PRODUCT_CONSTANT
                }
            )
        ) {
            ConstructorScreen(
                navController = navController,
                productId = it.arguments?.getInt(ARGUMENT_PRODUCT_ID) ?: NO_PRODUCT_CONSTANT
            )
        }
    }
}

sealed class MainNavRoute(
    val route: String
) {
    object MainInfo : MainNavRoute(route = "main_info")
    //object Constructor : MainNavRoute(route = "constructor")

    object Constructor : MainNavRoute(route = "constructor/{$ARGUMENT_PRODUCT_ID}") {
        fun passId(id : Int = NO_PRODUCT_CONSTANT): String = "constructor/$id"
    }
}