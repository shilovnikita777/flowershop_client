package com.example.flowershop.presentation.navigation

import androidx.navigation.*
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.flowershop.presentation.screens.ProfilePageScreens.*
import com.example.flowershop.screens.FavouriteScreen
import com.example.flowershop.screens.ProfileScreen
import com.example.flowershop.util.Constants.NO_ORDER_CONSTANT

fun NavGraphBuilder.profileNavGraph(
    nestedNavController : NavHostController,
    externalNavController: NavHostController
) {
    navigation(
        route = Graph.PROFILE.route,
        startDestination = ProfileNavRoute.ProfileMain.route
    ) {
        composable(
            route = ProfileNavRoute.ProfileMain.route
        ) {
            ProfileScreen(
                nestedNavController = nestedNavController,
                externalNavController = externalNavController
            )
        }

        composable(
            route = ProfileNavRoute.ProfileEdit.route
        ) {
            EditProfileScreen(nestedNavController)
        }

        composable(
            route = ProfileNavRoute.ProfileHistory.route
        ) {
            OrderHistoryScreen(nestedNavController)
        }

        composable(
            route = ProfileNavRoute.ProfileOrder.route,
            arguments = listOf(
                navArgument(ARGUMENT_ORDER_ID) {
                    type = NavType.IntType
                }
            )
        ) {
            OrderHistoryItemScreen(
                orderId = it.arguments?.getInt(ARGUMENT_ORDER_ID) ?: NO_ORDER_CONSTANT
            )
        }
        
        composable(
            route = ProfileNavRoute.ProfileFavourite.route
        ) {
            FavouriteScreen(nestedNavController)
        }

        composable(
            route = ProfileNavRoute.ProfilePromocodes.route
        ) {
            PromocodeScreen()
        }

        composable(
            route = ProfileNavRoute.ProfileAboutCompany.route
        ) {
            AboutCompanyScreen()
        }

        composable(
            route = ProfileNavRoute.ProfilePolicy.route
        ) {
            Policy()
        }

        composable(
            route = ProfileNavRoute.ProfileContacts.route
        ) {
            Contacts()
        }
    }
}

sealed class ProfileNavRoute(
    val route: String
) {
    object ProfileMain : ProfileNavRoute(route = "profile_main")

    object ProfileEdit : ProfileNavRoute(route = "profile_edit")

    object ProfileHistory : ProfileNavRoute(route = "profile_history")

    object ProfileFavourite : ProfileNavRoute(route = "profile_favourite")

    object ProfilePromocodes : ProfileNavRoute(route = "profile_promocodes")

    object ProfileAboutCompany : ProfileNavRoute(route = "profile_about")

    object ProfilePolicy : ProfileNavRoute(route = "profile_policy")

    object ProfileContacts : ProfileNavRoute(route = "profile_contacts")

    object ProfileOrder : ProfileNavRoute(route = "profile_order/{$ARGUMENT_ORDER_ID}") {
        fun passId(id : Int): String = "profile_order/$id"
    }
}

const val ARGUMENT_ORDER_ID = "id"