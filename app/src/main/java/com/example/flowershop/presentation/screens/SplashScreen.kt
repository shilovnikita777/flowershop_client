package com.example.flowershop.presentation.screens

import android.util.Log
import android.view.animation.OvershootInterpolator
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.flowershop.R
import com.example.flowershop.data.helpers.Response
import com.example.flowershop.presentation.navigation.Graph
import com.example.flowershop.util.Constants.NO_USER_CONSTANT
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(navController: NavHostController) {

    val viewModel = hiltViewModel<SplashViewModel>()

    val scale = remember { Animatable(0f) }

    val isAuthReponse = viewModel.isAuthResponse.value

    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 0.5f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = {
                    OvershootInterpolator(2f).getInterpolation(it)
                }
            )
        )
        delay(2500)

//        if (!viewModel.isChecking) {
//            viewModel.isUserAuthenticated()
//        }
        viewModel.isUserAuthenticated()
        //Log.d("xd3","UI: ${isAuthReponse}")

//        if (isAuthReponse is Response.Success) {
//            if (isAuthReponse.data.isAuth) {
//                navController.popBackStack()
//                navController.navigate(Graph.HOME.route)
//            } else {
//                navController.popBackStack()
//                navController.navigate(Graph.AUTHENTICATION.route)
//            }
//        } else if (isAuthReponse is Response.Error) {
//            Log.d("xd3","auth error")
//            Toast.makeText(context, "Ошибка авторизации", Toast.LENGTH_SHORT).show()
//        }
    }

    when(isAuthReponse) {
        is Response.Success -> {
            LaunchedEffect(key1 = true) {
                if (isAuthReponse.data.isAuth) {
                    navController.popBackStack()
                    navController.navigate(Graph.HOME.route)
                } else {
                    navController.popBackStack()
                    navController.navigate(Graph.AUTHENTICATION.route)
                }
            }
        }
        is Response.Error -> {
            LaunchedEffect(key1 = true) {
                Log.d("xd3", "auth error")
                Toast.makeText(context, isAuthReponse.message, Toast.LENGTH_SHORT).show()
                navController.popBackStack()
                navController.navigate(Graph.AUTHENTICATION.route)
            }
        }
        else -> {

        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.splash_image),
            contentDescription = "Splash Image",
            modifier = Modifier
                .scale(scale.value)
                .clip(CircleShape)
                .background(MaterialTheme.colors.primaryVariant)
                .padding(all = 30.dp)

        )
        Text(
            text = "Добро пожаловать!",
            style = MaterialTheme.typography.subtitle1.copy(
                fontSize = 24.sp
            ),
            color = MaterialTheme.colors.onBackground
        )
    }
}