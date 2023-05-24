package com.example.flowershop.screens

import android.util.Log
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
import com.example.flowershop.presentation.screens.AuthScreens.SignUp.SignUpEvents
import com.example.flowershop.presentation.screens.AuthScreens.SignUp.SignUpViewModel
import com.example.flowershop.presentation.screens.common.CustomPasswordTextField
import com.example.flowershop.presentation.screens.common.CustomTextField
import com.example.flowershop.util.Constants.NO_USER_CONSTANT
import com.example.flowershop.data.helpers.Response

@Composable
fun SignUpScreen(navController: NavHostController) {

    val viewModel = hiltViewModel<SignUpViewModel>()

    val state = viewModel.state.value

    val signUpState = viewModel.signUpState.value

    when (signUpState) {
        is Response.Success -> {
            if (signUpState.data != null) {
                Log.d("xd",signUpState.data.toString())
                LaunchedEffect(key1 = true) {
                    viewModel.saveUserId(signUpState.data.userInfo.id)
                    viewModel.saveToken(signUpState.data.token)
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
            text = "Имя пользователя",
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.secondary,
            modifier = Modifier
                .padding(top = 48.dp)
        )
        CustomTextField(
            value = state.username.text,
            placeholder = "Ваше имя",
            iconId = R.drawable.profile,
            keyboardType = KeyboardType.Text,
            onValueChange = {
                viewModel.onEvent(SignUpEvents.EnterUsername(it))
            },
            isError = !state.username.isValid
        )
        if (!state.username.isValid) {
            Text(
                text = state.username.msg.orEmpty(),
                style = MaterialTheme.typography.subtitle1.copy(
                    fontSize = 12.sp,
                    color = MaterialTheme.colors.onError
                )
            )
        }

        Text(
            text = "Почта",
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.secondary,
            modifier = Modifier
                .padding(top = 20.dp)
        )
        CustomTextField(
            value = state.mail.text,
            placeholder = "user@mail.ru",
            iconId = R.drawable.message,
            keyboardType = KeyboardType.Email,
            onValueChange = {
                viewModel.onEvent(SignUpEvents.EnterMail(it))
            },
            isError = !state.mail.isValid
        )
        if (!state.mail.isValid) {
            Text(
                text = state.mail.msg.orEmpty(),
                style = MaterialTheme.typography.subtitle1.copy(
                    fontSize = 12.sp,
                    color = MaterialTheme.colors.onError
                )
            )
        }

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
                viewModel.onEvent(SignUpEvents.EnterPassword(it))
            },
            onPasswordHiddenChange = {
                viewModel.onEvent(
                    SignUpEvents.ChangePasswordVisibility(
                    visibility = state.password.isPasswordHidden
                ))
            },
            isError = !state.password.isValid
        )

        if (!state.password.isValid) {
            Text(
                text = state.password.msg.orEmpty(),
                style = MaterialTheme.typography.subtitle1.copy(
                    fontSize = 12.sp,
                    color = MaterialTheme.colors.onError
                )
            )
        }

        Text(
            text = "Повторите пароль",
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.secondary,
            modifier = Modifier
                .padding(top = 20.dp)
        )
        CustomPasswordTextField(
            value = state.repeatedPassword.text,
            placeholder = "секретный пароль",
            isPasswordHidden = state.repeatedPassword.isPasswordHidden,
            onValueChange = {
                viewModel.onEvent(SignUpEvents.EnterRepeatedPassword(it))
            },
            onPasswordHiddenChange = {
                viewModel.onEvent(
                    SignUpEvents.ChangeRepeatedPasswordVisibility(
                    visibility = state.repeatedPassword.isPasswordHidden
                ))
            },
            isError = !state.repeatedPassword.isValid
        )
        if (!state.repeatedPassword.isValid) {
            Text(
                text = state.repeatedPassword.msg.orEmpty(),
                style = MaterialTheme.typography.subtitle1.copy(
                    fontSize = 12.sp,
                    color = MaterialTheme.colors.onError
                )
            )
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
                .height(65.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.primaryVariant
            ),
            shape = RoundedCornerShape(56.dp),
            enabled = (signUpState !is Response.Loading),
            onClick = {
                viewModel.signUp()
            }
        ) {
            when (signUpState) {
                is Response.Loading -> {
                    CircularProgressIndicator()
                }
                else -> {
                    Text(
                        text = "Зарегистрироваться",
                        style = MaterialTheme.typography.h3,
                        color = Color.White
                    )
                }
            }
        }

        if (signUpState is Response.Error) {
            Text(
                text = signUpState.message,
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
                    navController.navigate(AuthenticationNavRoute.SignIn.route) {
                        launchSingleTop = true
                    }
                }
        ){
            Text(
                text = "Уже есть аккаунт? ",
                style = MaterialTheme.typography.h4,
                color = MaterialTheme.colors.secondary
            )
            Text(
                text = "Войдите",
                style = MaterialTheme.typography.h4,
                color = MaterialTheme.colors.primaryVariant
            )
        }
    }
}