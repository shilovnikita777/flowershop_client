package com.example.flowershop.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.flowershop.presentation.navigation.BottomBarNavRoute
import com.example.flowershop.presentation.navigation.HomeNavGraph

@Composable
fun ScaffoldScreen(
    nestedNavController: NavHostController = rememberNavController(),
    externalNavController: NavHostController
) {
    Scaffold(
        bottomBar = {
            BottomBar(navController = nestedNavController)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            HomeNavGraph(
                nestedNavController = nestedNavController,
                externalNavController = externalNavController
            )
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarNavRoute.Home,
        BottomBarNavRoute.Catalog,
        BottomBarNavRoute.Bag,
        BottomBarNavRoute.Profile
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    
    BottomNavigation(
        backgroundColor = MaterialTheme.colors.background
    ) {
        screens.forEach { screen ->
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                navController = navController
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomBarNavRoute,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    val selected = currentDestination?.hierarchy?.any {
        it.route == screen.route
    } == true

    BottomNavigationItem(
        label = {
            Text(
                text = screen.title,
                style = MaterialTheme.typography.h4.copy(fontSize = 10.sp),
                color = if (selected) MaterialTheme.colors.primaryVariant else MaterialTheme.colors.onSecondary,
                modifier = Modifier.padding(top = 2.dp)
            )
        },
        icon = {
            Image(
                imageVector = ImageVector
                    .vectorResource(
                        id = if (selected) screen.selectedIcon else screen.unselectedIcon
                    ),
                contentDescription = "${screen.title} Icon"
            )
        },
        selected = selected,
        onClick = {
            navController.navigate(screen.route)
        }
    )
}