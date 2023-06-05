package com.example.flowershop.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.flowershop.R
import com.example.flowershop.screens.BagScreen

@Composable
fun HomeNavGraph(nestedNavController: NavHostController, externalNavController: NavHostController) {

    NavHost(
        navController = nestedNavController,
        startDestination = Graph.MAIN.route
    ) {
        mainNavGraph(navController = nestedNavController)

        catalogNavGraph(navController = nestedNavController)

        bagNavGraph(navController = nestedNavController)

        profileNavGraph(
            nestedNavController = nestedNavController,
            externalNavController = externalNavController
        )
    }
}

sealed class BottomBarNavRoute(
    val route: String,
    val title: String,
    val selectedIcon: Int,
    val unselectedIcon: Int
) {
    object Home : BottomBarNavRoute(
        route = Graph.MAIN.route,
        title = "Главная",
        selectedIcon = R.drawable.home_selected,
        unselectedIcon = R.drawable.home
    )

    object Catalog : BottomBarNavRoute(
        route = Graph.CATALOG.route,
        title = "Каталог",
        selectedIcon = R.drawable.search_selected,
        unselectedIcon = R.drawable.search
    )

    object Bag : BottomBarNavRoute(
        route = Graph.BAG.route,
        title = "Корзина",
        selectedIcon = R.drawable.buy_selected,
        unselectedIcon = R.drawable.buy
    )

    object Profile : BottomBarNavRoute(
        route = Graph.PROFILE.route,
        title = "Профиль",
        selectedIcon = R.drawable.profile_selected,
        unselectedIcon = R.drawable.profile
    )
}