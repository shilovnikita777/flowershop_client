package com.example.flowershop

import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.flowershop.presentation.navigation.RootNavGraph
import com.example.flowershop.presentation.screens.CatalogPageScreens.ProductsScreen
import com.example.flowershop.screens.ConstructorScreen
import com.example.flowershop.screens.ProductScreen
import com.example.flowershop.presentation.screens.ProfilePageScreens.EditProfileScreen
import com.example.flowershop.presentation.screens.ScaffoldScreen
import com.example.flowershop.presentation.screens.SplashScreen
import com.example.flowershop.presentation.screens.Test
import com.example.flowershop.ui.theme.FlowerShopTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //val window: Window = this.window
            //window.navigationBarColor = MaterialTheme.colors.background.toArgb()

            FlowerShopTheme {
                //EditProfileScreen()
                //SplashScreen(navController = rememberNavController())
                RootNavGraph(navController = rememberNavController())
                //Test()
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FlowerShopTheme {
        Greeting("Android")
    }
}