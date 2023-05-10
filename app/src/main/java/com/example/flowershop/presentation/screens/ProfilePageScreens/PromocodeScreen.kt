package com.example.flowershop.presentation.screens.ProfilePageScreens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.flowershop.R
import com.example.flowershop.presentation.model.PromocodeUI
import com.example.flowershop.presentation.screens.common.noRippleClickable
import com.example.flowershop.data.helpers.Response

@Composable
fun PromocodeScreen() {
    val viewModel = hiltViewModel<PromocodesViewModel>()

    val promocodes = viewModel.promocodes.value

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        when (promocodes) {
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
                    text = promocodes.message,
                    color = MaterialTheme.colors.onError,
                    style = MaterialTheme.typography.subtitle1
                )
            }
            is Response.Success -> {
                if (promocodes.data.isEmpty()) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .align(Alignment.Center)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.promocodes_empty),
                            contentDescription = ""
                        )
                        Text(
                            text = "Пока доступных промокодов нет!",
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
                        items(promocodes.data) {
                            PromocodesCards(
                                promocodeUI = it,
                                onClick = {
                                    viewModel.onPromocodeClicked(it)
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    if (viewModel.isDialogShown) {
        PromocodeDescriptionDialog(
            text = viewModel.description,
            onDismiss = {
                viewModel.onDismissDialog()
            }
        )
    }
}

@Composable
fun PromocodesCards(
    promocodeUI: PromocodeUI,
    onClick : (String) -> Unit
) {
    Card(
        elevation = 2.dp,
        shape = RoundedCornerShape(6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(125.dp)
            .noRippleClickable {
                onClick(promocodeUI.description)
            }
    ) {
        if (promocodeUI.image.getPath().isDigitsOnly()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(promocodeUI.image.getPath().toInt())
                    .build(),
                contentDescription = "Discount value",
                placeholder = painterResource(id = R.drawable.promocode_placeholder),
                error = painterResource(id = R.drawable.escanor),
                onError = {
                    Log.d("xd", it.result.throwable.message!!)
                },
                alignment = Alignment.BottomEnd,
                contentScale = ContentScale.FillWidth
            )
        } else AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(promocodeUI.image.getPath())
                .build(),
            contentDescription = "Discount value",
            placeholder = painterResource(id = R.drawable.promocode_placeholder),
            error = painterResource(id = R.drawable.escanor),
            onError = {
                Log.d("xd", it.result.throwable.message!!)
            },
            alignment = Alignment.BottomEnd,
            contentScale = ContentScale.FillWidth
        )
        Column(
            modifier = Modifier
                .padding(start = 14.dp,top = 8.dp)
        ) {
            Text(
                text = "${promocodeUI.amount}%",
                style = MaterialTheme.typography.h1.copy(fontSize = 48.sp),
                color = promocodeUI.amountColor
            )
            Text(
                text = promocodeUI.title,
                style = MaterialTheme.typography.h4.copy(fontSize = 15.sp),
                color = promocodeUI.titleColor,
                modifier = Modifier
                    .offset(y = (-12).dp)
            )
            Text(
                text = promocodeUI.value,
                style = MaterialTheme.typography.h1.copy(fontSize = 24.sp),
                color = promocodeUI.valueColor,
                modifier = Modifier
                    .padding(bottom = 8.dp)
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PromocodeDescriptionDialog(
    text : String,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            usePlatformDefaultWidth = true
        )
    ) {
        Card(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth(0.95f)
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(all = 24.dp)
            )
        }
    }
}