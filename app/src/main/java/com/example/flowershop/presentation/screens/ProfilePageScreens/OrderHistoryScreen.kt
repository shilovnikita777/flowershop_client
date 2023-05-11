package com.example.flowershop.presentation.screens.ProfilePageScreens

import android.graphics.Paint.Align
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.flowershop.R
import com.example.flowershop.data.helpers.Response
import com.example.flowershop.domain.model.User
import com.example.flowershop.presentation.model.PromocodeUI
import com.example.flowershop.presentation.navigation.ProfileNavRoute
import com.example.flowershop.presentation.screens.common.noRippleClickable
import com.example.flowershop.ui.theme.CustomGreen
import com.example.flowershop.ui.theme.CustomUltraLightGreen
import java.time.LocalDate
import java.util.*

@Composable
fun OrderHistoryScreen(navController: NavHostController) {
    val viewModel = hiltViewModel<OrderHistoryViewModel>()

    val orders = viewModel.orders.value

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        when (orders) {
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
                    text = orders.message,
                    color = MaterialTheme.colors.onError,
                    style = MaterialTheme.typography.subtitle1
                )
            }
            is Response.Success -> {
                if (orders.data.isEmpty()) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .align(Alignment.Center)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.car),
                            contentDescription = ""
                        )
                        Text(
                            text = "История заказов пока пуста!",
                            style = MaterialTheme.typography.subtitle1.copy(fontSize = 20.sp),
                            color = MaterialTheme.colors.onBackground,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                        )
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(all = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(orders.data.reversed()) {
                            OrderHistoryCard(
                                order = it,
                                onClick = {
                                    navController.navigate(route = ProfileNavRoute.ProfileOrder.passId(
                                        id = it.id
                                    ))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrderHistoryCard(
    order: User.Order,
    onClick : () -> Unit
) {
    Card(
        elevation = 2.dp,
        shape = RoundedCornerShape(6.dp),
        backgroundColor = CustomUltraLightGreen,
        modifier = Modifier
            .fillMaxWidth()
            .height(125.dp)
            .noRippleClickable {
                onClick()
            }
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 16.dp, top = 16.dp, bottom = 16.dp, end = 16.dp)
                    .weight(0.7f)
            ) {
                Text(
                    text = "Дата заказа: ${order.date}",
                    style = MaterialTheme.typography.h1.copy(fontSize = 18.sp),
                    color = CustomGreen
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Получатель: ${order.fullname}",
                    style = MaterialTheme.typography.h4.copy(fontSize = 16.sp),
                    color = CustomGreen
                )
            }
            Image(
                painter = painterResource(id = R.drawable.history_image),
                contentDescription = "image",
                modifier = Modifier
                    .weight(0.3f)
            )
        }
    }
}