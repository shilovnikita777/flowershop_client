package com.example.flowershop.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.flowershop.presentation.screens.ScaffoldScreen
import com.example.flowershop.presentation.screens.SplashScreen

@Composable
fun RootNavGraph(navController: NavHostController){
    NavHost(
        navController = navController,
        route = Graph.ROOT.route,
        startDestination = RootNavRoute.Splash.route
    ) {
        composable(
            route = RootNavRoute.Splash.route
        ) {
            SplashScreen(navController = navController)
        }

        authenticationNavGraph(navController = navController)

        composable(
            route = Graph.HOME.route
        ) {
            ScaffoldScreen(
                externalNavController = navController
            )
        }
    }
}

sealed class Graph(
    val route: String
) {
    object ROOT: Graph("root_graph")
    object AUTHENTICATION: Graph("authentication_graph")

    object HOME: Graph("home_graph/{$ARGUMENT_USER_ID}") {
        fun passId(id: Int) = "home_graph/$id"
    }

    object MAIN: Graph("main_graph")
    object CATALOG: Graph("catalog_graph")
    object PROFILE: Graph("profile_graph")
    object BAG: Graph("bag_graph")
}

sealed class RootNavRoute(
    val route : String
) {
    object Splash: RootNavRoute(route = "splash")
}

const val ARGUMENT_USER_ID = "id"