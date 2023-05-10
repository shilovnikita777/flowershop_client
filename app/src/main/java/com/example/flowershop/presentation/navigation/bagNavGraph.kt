package com.example.flowershop.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.flowershop.presentation.screens.BagScreens.OrderInfo.OrderInfoScreen
import com.example.flowershop.presentation.screens.ProfilePageScreens.*
import com.example.flowershop.screens.BagScreen
import com.example.flowershop.screens.FavouriteScreen
import com.example.flowershop.screens.ProfileScreen

fun NavGraphBuilder.bagNavGraph(
    navController : NavHostController
) {
    navigation(
        route = Graph.BAG.route,
        startDestination = BagNavRoute.BagProducts.route
    ) {
        composable(
            route = BagNavRoute.BagProducts.route
        ) {
            BagScreen(
                navController = navController
            )
        }

        composable(
            route = BagNavRoute.BagOrderInfo.route
        ) {
            OrderInfoScreen(navController)
        }
    }
}

sealed class BagNavRoute(
    val route: String
) {
    object BagProducts : BagNavRoute(route = "bag_products")

    object BagOrderInfo : BagNavRoute(route = "bag_orderinfo")
}