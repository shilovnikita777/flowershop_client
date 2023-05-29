package com.example.flowershop.screens

import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flowershop.R
import androidx.compose.runtime.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.flowershop.domain.model.*
import com.example.flowershop.presentation.navigation.CatalogNavRoute
import com.example.flowershop.presentation.screens.ProfilePageScreens.FavouriteViewModel
import com.example.flowershop.presentation.screens.UserProductViewModel
import com.example.flowershop.data.helpers.Response

@Composable
fun FavouriteScreen(navController: NavHostController) {

    val favouriteViewModel = hiltViewModel<FavouriteViewModel>()

    val userProductViewModel = hiltViewModel<UserProductViewModel>()

    val userFavouriteResponse = favouriteViewModel.userFavouriteResponse.value

    val productsInFavourite = favouriteViewModel.productsInFavourite

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        when(userFavouriteResponse) {
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
                    text = userFavouriteResponse.message,
                    color = MaterialTheme.colors.onError,
                    style = MaterialTheme.typography.subtitle1
                )
            }
            is Response.Success -> {
                if (productsInFavourite.isEmpty()) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .align(Alignment.Center)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.bagflower),
                            contentDescription = "",
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                        )
                        Text(
                            text = "Вы ещё ничего не добавили в избранное",
                            style = MaterialTheme.typography.subtitle1.copy(fontSize = 20.sp),
                            color = MaterialTheme.colors.onBackground,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                        )
                    }
                } else {
                    ProductsInFavourite(
                        productsInFavourite = productsInFavourite,
                        userProductViewModel = userProductViewModel,
                        favouriteViewModel = favouriteViewModel,
                        navController = navController
                    )
                }
            }
        }
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            modifier = Modifier
//                .align(Alignment.Center)
//        ) {
//            Image(
//                painter = painterResource(R.drawable.bagflower),
//                contentDescription = "",
//                modifier = Modifier
//                    .clip(RoundedCornerShape(10.dp))
//            )
//            Text(
//                text = "Ваша корзина пуста",
//                style = MaterialTheme.typography.subtitle1.copy(fontSize = 20.sp),
//                color = MaterialTheme.colors.onBackground
//            )
//        }
//        LazyColumn(
//            contentPadding = PaddingValues(top = 16.dp,start = 24.dp, end = 24.dp),
//            verticalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//            items(14) {
//                BagCard(
//                    name = "Букет Дигиридай",
//                    count = 1,
//                    price = 4500,
//                    imageId = R.drawable.bouquet_2
//                )
//            }
//            item {
//                TextField(
//                    value = promocode,
//                    onValueChange = {
//                        promocode = it
//
//                    },
//                    shape = RoundedCornerShape(15.dp),
//                    singleLine = true,
//                    textStyle = MaterialTheme.typography.subtitle1
//                        .copy(
//                            color = MaterialTheme.colors.onSecondary,
//                            fontWeight = FontWeight.Medium
//                        ),
//                    colors = TextFieldDefaults.textFieldColors(
//                        backgroundColor = MaterialTheme.colors.secondaryVariant,
//                        focusedIndicatorColor = Color.Transparent,
//                        disabledIndicatorColor = Color.Transparent,
//                        unfocusedIndicatorColor = Color.Transparent
//                    ),
//                    placeholder = {
//                        Text(
//                            text = "Введите промокод",
//                            style = MaterialTheme.typography.subtitle1,
//                            color = MaterialTheme.colors.secondary
//                        )
//                    },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(50.dp)
//                )
//            }
//            item {
//                Row(
//                    modifier = Modifier
//                        .padding(top = 16.dp)
//                ) {
//                    Text(
//                        text = "Итого:",
//                        style = MaterialTheme.typography.h2.copy(fontSize = 20.sp),
//                        color = MaterialTheme.colors.primary,
//                    )
//                    Row(
//                        modifier = Modifier
//                            .padding(start = 16.dp)
//                            .weight(1f),
//                        horizontalArrangement = Arrangement.End
//                    ) {
//                        Text(
//                            text = "₽ ",
//                            style = MaterialTheme.typography.h2.copy(fontSize = 20.sp),
//                            color = MaterialTheme.colors.primaryVariant,
//                        )
//                        Text(
//                            text = "13500",
//                            style = MaterialTheme.typography.h2.copy(fontSize = 20.sp),
//                            color = MaterialTheme.colors.onBackground,
//                        )
//                    }
//                }
//            }
//            item {
//                Button(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(top = 24.dp, bottom = 16.dp)
//                        .height(65.dp),
//                    colors = ButtonDefaults.buttonColors(
//                        backgroundColor = MaterialTheme.colors.primaryVariant
//                    ),
//                    shape = RoundedCornerShape(56.dp),
//                    onClick = {
//
//                    }
//                ) {
//                    Text(
//                        text = "Оформить заказ",
//                        style = MaterialTheme.typography.h3,
//                        color = Color.White
//                    )
//                }
//            }
//        }
    }
}

@Composable
fun ProductsInFavourite(
    productsInFavourite: List<Triple<Product,MutableState<Response<Boolean>>,MutableState<Response<Boolean>>>>,
    userProductViewModel: UserProductViewModel,
    favouriteViewModel: FavouriteViewModel,
    navController: NavHostController
) {
    LaunchedEffect(key1 = Unit) {
        productsInFavourite.forEach { product->
            userProductViewModel.isProductInBag(product.first) {
                product.second.value = it
            }
        }
    }
    LazyColumn(
        contentPadding = PaddingValues(top = 16.dp,bottom = 12.dp, start = 24.dp, end = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(productsInFavourite) { productInFavourite ->
            FavouriteCard(
                productInFavourite = productInFavourite,
                userProductViewModel = userProductViewModel,
                favouriteViewModel = favouriteViewModel,
                navController = navController
            )
        }
    }
}

@Composable
fun FavouriteCard(
    productInFavourite: Triple<Product,MutableState<Response<Boolean>>,MutableState<Response<Boolean>>>,
    userProductViewModel: UserProductViewModel,
    favouriteViewModel: FavouriteViewModel,
    navController: NavHostController
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        elevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                navController.navigate(CatalogNavRoute.Product.passIdAndType(
                    productInFavourite.first.id,
                    productInFavourite.first.type
                ))
            },
    ) {
        Row(
            modifier = Modifier
                .padding(start = 10.dp,end = 14.dp,top = 10.dp, bottom = 10.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(productInFavourite.first.image.getPath())
                    .build(),
                contentDescription = productInFavourite.first.name,
                placeholder = painterResource(id = R.drawable.bouquet_placeholder),
                error = painterResource(id = R.drawable.escanor),
                onError = {
                    Log.d("xd", it.result.throwable.message!!)
                },
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
            Column(
                modifier = Modifier
                    .padding(start = 10.dp)
            ) {
                Row {
                    Text(
                        text = productInFavourite.first.name,
                        style = MaterialTheme.typography.h3.copy(fontSize = 14.sp),
                        color = MaterialTheme.colors.onBackground,
                        modifier = Modifier
                            .padding(top = 4.dp, end = 12.dp)
                            .weight(1f)
                    )
                    when(val response = productInFavourite.third.value) {
                        is Response.Loading -> {
                            CircularProgressIndicator(
                                strokeWidth = 2.dp,
                                modifier = Modifier
                                    .size(16.dp)
                            )
                        }
                        is Response.Error -> {
                            Text(
                                text = response.message,
                                color = MaterialTheme.colors.onError,
                                style = MaterialTheme.typography.subtitle1
                            )
                        }
                        is Response.Success -> {
                            Image(
                                imageVector = ImageVector
                                    .vectorResource(
                                        id = R.drawable.x
                                    ),
                                contentDescription = "",
                                modifier = Modifier
                                    .clickable {
                                        userProductViewModel.removeProductFromFavourite(
                                            product = productInFavourite.first,
                                            onValueChanged = {
                                                productInFavourite.third.value = it
                                                if (it is Response.Success && !it.data) {
                                                    favouriteViewModel.deleteProductFromLocalFavourite(productInFavourite.first)
                                                }
                                            }
                                        )
                                    }
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .weight(1f)
                ) {
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    when(val isInBagResponse = productInFavourite.second.value) {
                        is Response.Loading -> {
                            Box(
                                modifier = Modifier
                                    .width(100.dp)
                                    .height(30.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .size(25.dp),
                                    strokeWidth = 2.dp
                                )
                            }
                        }
                        is Response.Error -> {
                            Text(
                                text = isInBagResponse.message,
                                color = MaterialTheme.colors.onError,
                                style = MaterialTheme.typography.subtitle1
                            )
                        }
                        is Response.Success -> {
                            if (isInBagResponse.data) {
                                Button(
                                    modifier = Modifier
                                        .width(100.dp)
                                        .height(30.dp),
                                    contentPadding = PaddingValues(0.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = MaterialTheme.colors.primaryVariant
                                    ),
                                    shape = RoundedCornerShape(6.dp),
                                    onClick = {
                                        userProductViewModel.removeProductFromBag(productInFavourite.first) {
                                            productInFavourite.second.value = it
                                        }
                                    }
                                ) {
                                    Text(
                                        text = "Добавлено",
                                        style = MaterialTheme.typography.h3.copy(fontSize = 12.sp),
                                        color = MaterialTheme.colors.background
                                    )
                                }
                            } else {
                                Button(
                                    modifier = Modifier
                                        .width(100.dp)
                                        .height(30.dp),
                                    contentPadding = PaddingValues(0.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = MaterialTheme.colors.primaryVariant
                                    ),
                                    shape = RoundedCornerShape(6.dp),
                                    onClick = {
                                        val productWithCount = if (productInFavourite.first is Flower) FlowersWithDecoration(
                                            product = productInFavourite.first as Flower,
                                            count = 1,
                                            decoration = Decoration()
                                        ) else {
                                            ProductWithCount(
                                                product = productInFavourite.first,
                                                count = 1
                                            )
                                        }
                                        userProductViewModel.addProductToBag(
                                            productWithCount = productWithCount
                                        ) {
                                            productInFavourite.second.value = it
                                        }
                                    }
                                ) {
                                    Text(
                                        text = "В корзину",
                                        style = MaterialTheme.typography.h3.copy(fontSize = 12.sp),
                                        color = MaterialTheme.colors.background
                                    )
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .weight(1f),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = "₽ ",
                            style = MaterialTheme.typography.h3,
                            color = MaterialTheme.colors.primaryVariant
                        )
                        Text(
                            text = "${productInFavourite.first.price}",
                            style = MaterialTheme.typography.h3,
                            color = MaterialTheme.colors.onBackground)
                    }
                }
            }

        }
    }
}