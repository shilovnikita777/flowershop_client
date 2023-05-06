package com.example.flowershop.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.flowershop.presentation.screens.CatalogPageScreens.ProductsScreen
import com.example.flowershop.screens.CategoriesScreen
import com.example.flowershop.screens.ProductScreen
import com.example.flowershop.util.Constants
import com.example.flowershop.util.Constants.NO_PRODUCT_CONSTANT

fun NavGraphBuilder.catalogNavGraph(navController : NavHostController) {
    navigation(
        route = Graph.CATALOG.route,
        startDestination = CatalogNavRoute.Categories.route
    ) {
        composable(
            route = CatalogNavRoute.Categories.route
        ) {
            CategoriesScreen(navController)
        }

        composable(
            route = CatalogNavRoute.Products.route,
            arguments = listOf(
                navArgument(ARGUMENT_CATEGORY_NAME) {
                    type = NavType.StringType
                    defaultValue = "Все товары"
                },
                navArgument(ARGUMENT_CATEGORY_ID) {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) {
            ProductsScreen(
                navController = navController,
                categoryName = it.arguments?.getString(ARGUMENT_CATEGORY_NAME).toString(),
                categoryId = it.arguments?.getInt(ARGUMENT_CATEGORY_ID) ?: -1
            )
        }

        composable(
            route = CatalogNavRoute.Product.route,
            arguments = listOf(
                navArgument(ARGUMENT_PRODUCT_ID) {
                    type = NavType.IntType
                },
                navArgument(ARGUMENT_PRODUCT_TYPE) {
                    type = NavType.StringType
                }
            )
        ) {
            ProductScreen(
                navController = navController,
                productId = it.arguments?.getInt(ARGUMENT_PRODUCT_ID) ?: NO_PRODUCT_CONSTANT,
                productType = it.arguments?.getString(ARGUMENT_PRODUCT_TYPE) ?: "product"
            )
        }
    }
}

sealed class CatalogNavRoute(
    val route: String
) {
    object Categories : CatalogNavRoute(route = "categories")

    object Product : CatalogNavRoute(route = "product/{$ARGUMENT_PRODUCT_ID}/{$ARGUMENT_PRODUCT_TYPE}") {
        fun passIdAndType(id : Int, type : String): String = "product/$id/$type"
    }

    object Products : CatalogNavRoute(route = "products?name={$ARGUMENT_CATEGORY_NAME}&id={$ARGUMENT_CATEGORY_ID}"){
        fun passNameAndId(name: String,id: Int) = "products?name=$name&id=$id"
    }
}

const val ARGUMENT_CATEGORY_ID = "id"
const val ARGUMENT_CATEGORY_NAME = "name"

const val ARGUMENT_PRODUCT_ID = "id"
const val ARGUMENT_PRODUCT_TYPE = "type"
const val ARGUMENT_PRODUCT_FROM = "from"