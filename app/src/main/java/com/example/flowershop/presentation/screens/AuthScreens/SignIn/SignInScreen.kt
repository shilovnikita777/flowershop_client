package com.example.flowershop.presentation.screens.AuthScreens.SignIn

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.flowershop.R
import com.example.flowershop.presentation.navigation.AuthenticationNavRoute
import com.example.flowershop.presentation.navigation.Graph
import com.example.flowershop.presentation.screens.common.CustomPasswordTextField
import com.example.flowershop.presentation.screens.common.CustomTextField
import com.example.flowershop.util.Constants.NO_USER_CONSTANT
import com.example.flowershop.data.helpers.Response

@Composable
fun SignInScreen(navController: NavHostController) {

    val viewModel = hiltViewModel<SignInViewModel>()

    val state = viewModel.state.value

    val signInState = viewModel.signInState.value

    when (signInState) {
        is Response.Success -> {
            if (signInState.data != null) {
                LaunchedEffect(key1 = true) {
                    //viewModel.saveUserId(signInState.data.userInfo.id)
                    //viewModel.saveUserId(0)
                    viewModel.saveToken(signInState.data.token)
                    navController.popBackStack()
                    navController.navigate(Graph.HOME.route)
                }
            }
        }
        else -> {

        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = 24.dp,
                end = 24.dp,
                top = 56.dp
            )
    ) {
        Text(
            text = "Добро пожаловать !",
            style = MaterialTheme.typography.h2,
            color = MaterialTheme.colors.primary,
        )

        Text(
            text = "Почта",
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.secondary,
            modifier = Modifier
                .padding(top = 48.dp)
        )
        CustomTextField(
            value = state.mail.text,
            placeholder = "user@mail.ru",
            iconId = R.drawable.message,
            keyboardType = KeyboardType.Email,
            onValueChange = {
                viewModel.onEvent(SignInEvents.EnterMail(it))
            }
        )

        Text(
            text = "Пароль",
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.secondary,
            modifier = Modifier
                .padding(top = 20.dp)
        )
        CustomPasswordTextField(
            value = state.password.text,
            placeholder = "секретный пароль",
            isPasswordHidden = state.password.isPasswordHidden,
            onValueChange = {
                viewModel.onEvent(SignInEvents.EnterPassword(it))
            },
            onPasswordHiddenChange = {
                viewModel.onEvent(SignInEvents.ChangePasswordVisibility(state.password.isPasswordHidden))
            }
        )

//        Text(
//            text = "Забыли пароль?",
//            style = MaterialTheme.typography.subtitle1,
//            color = MaterialTheme.colors.secondary,
//            modifier = Modifier
//                .padding(top = 6.dp)
//                .align(Alignment.End)
//        )

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
                .height(65.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.primaryVariant
            ),
            shape = RoundedCornerShape(56.dp),
            enabled = signInState !is Response.Loading,
            onClick = {
                viewModel.signIn()
            }
        ) {
            when (signInState) {
                is Response.Loading -> {
                    CircularProgressIndicator()
                }
                else -> {
                    Text(
                        text = "Войти",
                        style = MaterialTheme.typography.h3,
                        color = Color.White
                    )
                }
            }
        }

//        when (signInState) {
//            is Response.Success -> {
//                LaunchedEffect(key1 = true) {
//                    navController.popBackStack()
//                    navController.navigate(
//                        Graph.HOME.passToken(
//                            token = signInState.data
//                        )
//                    )
//                }
//            }
//            is Response.Error -> {
//                Text(
//                    text = signInState.message,
//                    style = MaterialTheme.typography.subtitle1.copy(fontSize = 12.sp),
//                    color = MaterialTheme.colors.onError,
//                    modifier = Modifier.padding(top = 4.dp)
//                )
//            }
//            else -> {
//
//            }
//        }
        if (signInState is Response.Error) {
            Text(
                text = signInState.message,
                style = MaterialTheme.typography.subtitle1.copy(fontSize = 12.sp),
                color = MaterialTheme.colors.onError,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Row(
            modifier = Modifier
                .padding(top = 24.dp)
                .align(Alignment.CenterHorizontally)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    navController.popBackStack()
                    navController.navigate(AuthenticationNavRoute.SignUp.route) {
                        launchSingleTop = true
                    }
                }
        ){
            Text(
                text = "Ещё нет аккаунта? ",
                style = MaterialTheme.typography.h4,
                color = MaterialTheme.colors.secondary
            )
            Text(
                text = "Зарегистрируйтесь",
                style = MaterialTheme.typography.h4,
                color = MaterialTheme.colors.primaryVariant
            )
        }
    }
}