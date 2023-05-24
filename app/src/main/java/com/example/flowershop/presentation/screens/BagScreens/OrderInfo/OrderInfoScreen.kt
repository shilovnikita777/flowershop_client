package com.example.flowershop.presentation.screens.BagScreens.OrderInfo

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.flowershop.R
import com.example.flowershop.data.helpers.Response
import com.example.flowershop.domain.model.Promocode
import com.example.flowershop.presentation.navigation.AuthenticationNavRoute
import com.example.flowershop.presentation.navigation.ProfileNavRoute
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
            .verticalScroll(rememberScrollState())
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
                Log.d("xd11", state.toString())
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

                    Promocode(
                        promocode = state.promocode.text,
                        promoResponse = state.usePromoResponse,
                        onPromocodeChange = {
                        viewModel.onEvent(OrderInfoEvents.EnterPromocode(it))
                    })

                    val discount = if (state.usePromoResponse is Response.Success)
                                       state.usePromoResponse.data.amount
                                   else
                                        0
                    Summ(
                        total = state.orderSumm
                    )

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp, bottom = 12.dp)
                            .height(65.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.primaryVariant
                        ),
                        shape = RoundedCornerShape(56.dp),
                        enabled = viewModel.makeOrderResponse.value !is Response.Loading,
                        onClick = {
                            viewModel.pay(
                                onSuccess = {
                                    viewModel.makeOrder {
                                        navController.navigate(ProfileNavRoute.ProfileHistory.route)
                                    }
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

@Composable
fun Promocode(
    promocode: String,
    promoResponse : Response<Promocode>?,
    onPromocodeChange : (String) -> Unit) {
    TextField(
        value = promocode,
        onValueChange = {
            onPromocodeChange(it)
        },
        shape = RoundedCornerShape(15.dp),
        singleLine = true,
        textStyle = MaterialTheme.typography.subtitle1
            .copy(
                color = MaterialTheme.colors.onSecondary,
                fontWeight = FontWeight.Medium
            ),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.secondaryVariant,
            focusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        placeholder = {
            Text(
                text = "Введите промокод",
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.secondary
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
            .height(50.dp)
    )

    Row(
        modifier = Modifier
            .padding(top = 8.dp)
    ) {
        when (promoResponse) {
            is Response.Loading -> {
                CircularProgressIndicator(
                    strokeWidth = 2.dp,
                    modifier = Modifier
                        .size(24.dp)
                )
            }
            is Response.Error -> {
                Text(
                    text = promoResponse.message,
                    color = MaterialTheme.colors.onError,
                    style = MaterialTheme.typography.subtitle1
                )
            }
            is Response.Success -> {
                Text(
                    text = "-${promoResponse.data.amount}%",
                    style = MaterialTheme.typography.h4.copy(
                        fontSize = 20.sp
                    ),
                    color = MaterialTheme.colors.onBackground
                )
            }
            else -> {

            }
        }
    }
}

@Composable
fun Summ(
    total : Int
) {
    Row(
        modifier = Modifier
            .padding(top = 8.dp)
    ) {
        Text(
            text = "Итого:",
            style = MaterialTheme.typography.h4.copy(fontSize = 20.sp),
            color = MaterialTheme.colors.onBackground,
        )
        Row(
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "₽ ",
                style = MaterialTheme.typography.h4.copy(fontSize = 20.sp),
                color = MaterialTheme.colors.primaryVariant,
            )
            Text(
                text = total.toString(),
                style = MaterialTheme.typography.h4.copy(fontSize = 20.sp),
                color = MaterialTheme.colors.onBackground,
            )
        }
    }
}