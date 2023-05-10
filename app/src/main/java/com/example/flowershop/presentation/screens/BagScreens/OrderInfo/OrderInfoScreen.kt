package com.example.flowershop.presentation.screens.BagScreens.OrderInfo

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.flowershop.R
import com.example.flowershop.data.helpers.Response
import com.example.flowershop.presentation.navigation.AuthenticationNavRoute
import com.example.flowershop.presentation.screens.AuthScreens.SignUp.SignUpEvents
import com.example.flowershop.presentation.screens.BagScreens.BagViewModel
import com.example.flowershop.presentation.screens.common.CustomPasswordTextField
import com.example.flowershop.presentation.screens.common.CustomTextField
import com.example.flowershop.screens.ProductsInBag

@Composable
fun OrderInfoScreen(navController: NavHostController) {
    val viewModel = hiltViewModel<OrderInfoViewModel>()

    val userBagResponse = viewModel.userBagResponse.value

    val state = viewModel.state.value

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        when (userBagResponse) {
            is Response.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is Response.Error -> {
                Text(
                    text = userBagResponse.message,
                    color = MaterialTheme.colors.onError,
                    style = MaterialTheme.typography.subtitle1
                )
            }
            is Response.Success -> {
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
                        text = "Пожалуйста, заполните информацию о заказе",
                        style = MaterialTheme.typography.h2,
                        color = MaterialTheme.colors.primary,
                    )

                    Text(
                        text = "Телефон для связи",
                        style = MaterialTheme.typography.subtitle1,
                        color = MaterialTheme.colors.secondary,
                        modifier = Modifier
                            .padding(top = 48.dp)
                    )

                    CustomTextField(
                        value = state.phone.text,
                        placeholder = "+76666666666",
                        iconId = R.drawable.phone,
                        keyboardType = KeyboardType.Phone,
                        onValueChange = {
                            viewModel.onEvent(OrderInfoEvents.EnterPhone(it))
                        },
                        isError = !state.phone.isValid
                    )
                    if (!state.phone.isValid) {
                        Text(
                            text = state.phone.msg.orEmpty(),
                            style = MaterialTheme.typography.subtitle1.copy(
                                fontSize = 12.sp,
                                color = MaterialTheme.colors.onError
                            )
                        )
                    }

                    Text(
                        text = "Куда доставить заказ?",
                        style = MaterialTheme.typography.subtitle1,
                        color = MaterialTheme.colors.secondary,
                        modifier = Modifier
                            .padding(top = 20.dp)
                    )
                    CustomTextField(
                        value = state.address.text,
                        placeholder = "Адрес",
                        iconId = R.drawable.location,
                        keyboardType = KeyboardType.Text,
                        onValueChange = {
                            viewModel.onEvent(OrderInfoEvents.EnterAddress(it))
                        },
                        isError = !state.address.isValid
                    )
                    if (!state.address.isValid) {
                        Text(
                            text = state.address.msg.orEmpty(),
                            style = MaterialTheme.typography.subtitle1.copy(
                                fontSize = 12.sp,
                                color = MaterialTheme.colors.onError
                            )
                        )
                    }

                    Text(
                        text = "ФИО получателя",
                        style = MaterialTheme.typography.subtitle1,
                        color = MaterialTheme.colors.secondary,
                        modifier = Modifier
                            .padding(top = 20.dp)
                    )
                    CustomTextField(
                        value = state.fullname.text,
                        placeholder = "Иванов Иван Иванович",
                        iconId = R.drawable.profile,
                        keyboardType = KeyboardType.Text,
                        onValueChange = {
                            viewModel.onEvent(OrderInfoEvents.EnterFN(it))
                        },
                        isError = !state.fullname.isValid
                    )

                    if (!state.fullname.isValid) {
                        Text(
                            text = state.fullname.msg.orEmpty(),
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
                        enabled = viewModel.makeOrderResponse.value !is Response.Loading,
                        onClick = {
                            viewModel.pay(
                                onSuccess = {
                                    viewModel.makeOrder()
                                }
                            )
                        }
                    ) {
                        when (viewModel.makeOrderResponse.value) {
                            is Response.Loading -> {
                                CircularProgressIndicator()
                            }
                            else -> {
                                Text(
                                    text = "Оплатить",
                                    style = MaterialTheme.typography.h3,
                                    color = Color.White
                                )
                            }
                        }
                    }

//                    if (signUpState is Response.Error) {
//                        Text(
//                            text = signUpState.message,
//                            style = MaterialTheme.typography.subtitle1.copy(fontSize = 12.sp),
//                            color = MaterialTheme.colors.onError,
//                            modifier = Modifier.padding(top = 4.dp)
//                        )
//                    }
//
//                    Row(
//                        modifier = Modifier
//                            .padding(top = 24.dp)
//                            .align(Alignment.CenterHorizontally)
//                            .clickable(
//                                interactionSource = remember { MutableInteractionSource() },
//                                indication = null
//                            ) {
//                                navController.popBackStack()
//                                navController.navigate(AuthenticationNavRoute.SignIn.route) {
//                                    launchSingleTop = true
//                                }
//                            }
//                    ){
//                        Text(
//                            text = "Уже есть аккаунт? ",
//                            style = MaterialTheme.typography.h4,
//                            color = MaterialTheme.colors.secondary
//                        )
//                        Text(
//                            text = "Войдите",
//                            style = MaterialTheme.typography.h4,
//                            color = MaterialTheme.colors.primaryVariant
//                        )
//                    }
                }
            }
        }
    }
}