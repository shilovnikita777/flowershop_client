package com.example.flowershop.screens

import android.util.Log
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flowershop.R
import androidx.compose.runtime.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.flowershop.presentation.navigation.CatalogNavRoute
import com.example.flowershop.presentation.screens.BagScreens.BagViewModel
import com.example.flowershop.presentation.screens.UserProductViewModel
import com.example.flowershop.data.helpers.Response
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.core.text.isDigitsOnly
import com.example.flowershop.domain.model.Bouquet
import com.example.flowershop.domain.model.FlowersWithDecoration
import com.example.flowershop.domain.model.ProductInBag
import com.example.flowershop.presentation.navigation.MainNavRoute
import com.example.flowershop.util.Constants.AUTHOR_BOUQUET_NAME

@Composable
fun BagScreen(navController: NavHostController) {

    val bagViewModel = hiltViewModel<BagViewModel>()

    val userProductViewModel = hiltViewModel<UserProductViewModel>()

    val userBagResponse = bagViewModel.userBagResponse.value

    val productsInBag = bagViewModel.userBagState.products
    val total = bagViewModel.userBagState.total.value

    Log.d("xd1",bagViewModel.userBagState.total.toString())

    var promocode by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        when(userBagResponse) {
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
                if (productsInBag.isEmpty()) {
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
                            text = "Ваша корзина пуста",
                            style = MaterialTheme.typography.subtitle1.copy(fontSize = 20.sp),
                            color = MaterialTheme.colors.onBackground
                        )
                    }
                } else {
                    ProductsInBag(
                        productsInBag = productsInBag,
                        total = total,
                        promocode = promocode,
                        userProductViewModel = userProductViewModel,
                        bagViewModel = bagViewModel,
                        navController = navController,
                        onPromocodeChange = {
                            promocode = it
                        }
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
fun ProductsInBag(
    productsInBag: List<Triple<ProductInBag,MutableState<Response<Boolean>>,MutableState<Response<Boolean>>>>,
    total: Int,
    promocode: String,
    userProductViewModel: UserProductViewModel,
    bagViewModel: BagViewModel,
    navController: NavHostController,
    onPromocodeChange: (String) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(top = 16.dp,start = 24.dp, end = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(productsInBag) { productInBag ->
            BagCard(
                productInBag = productInBag,
                userProductViewModel = userProductViewModel,
                bagViewModel = bagViewModel,
                navController = navController
            )
        }
        item {
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
                    .height(50.dp)
            )
        }
        item {
            Row(
                modifier = Modifier
                    .padding(top = 16.dp)
            ) {
                Text(
                    text = "Итого:",
                    style = MaterialTheme.typography.h2.copy(fontSize = 20.sp),
                    color = MaterialTheme.colors.primary,
                )
                Row(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .weight(1f),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "₽ ",
                        style = MaterialTheme.typography.h2.copy(fontSize = 20.sp),
                        color = MaterialTheme.colors.primaryVariant,
                    )
                    Text(
                        text = total.toString(),
                        style = MaterialTheme.typography.h2.copy(fontSize = 20.sp),
                        color = MaterialTheme.colors.onBackground,
                    )
                }
            }
        }
        item {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, bottom = 16.dp)
                    .height(65.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.primaryVariant
                ),
                shape = RoundedCornerShape(56.dp),
                onClick = {

                }
            ) {
                Text(
                    text = "Оформить заказ",
                    style = MaterialTheme.typography.h3,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun BagCard(
    productInBag: Triple<ProductInBag,MutableState<Response<Boolean>>,MutableState<Response<Boolean>>>,
    userProductViewModel: UserProductViewModel,
    bagViewModel: BagViewModel,
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
                if (productInBag.first.productWithCount.product.name == AUTHOR_BOUQUET_NAME)
                    navController.navigate(MainNavRoute.Constructor.passId(productInBag.first.productWithCount.product.id))
                else
                    navController.navigate(CatalogNavRoute.Product.passIdAndType(
                        id = productInBag.first.productWithCount.product.id,
                        type = productInBag.first.productWithCount.product.type
                    ))
            },
    ) {
        Row(
            modifier = Modifier
                .padding(start = 10.dp,end = 14.dp,top = 10.dp, bottom = 10.dp)
        ) {
            if (productInBag.first.productWithCount.product.image.getPath().isDigitsOnly()){
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(productInBag.first.productWithCount.product.image.getPath().toInt())
                        .build(),
                    contentDescription = productInBag.first.productWithCount.product.name,
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
            } else AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(productInBag.first.productWithCount.product.image.getPath())
                    .build(),
                contentDescription = productInBag.first.productWithCount.product.name,
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
                        text = productInBag.first.productWithCount.product.name,
                        style = MaterialTheme.typography.h3.copy(fontSize = 14.sp),
                        color = MaterialTheme.colors.onBackground,
                        modifier = Modifier
                            .padding(top = 4.dp, end = 12.dp)
                            .weight(1f)
                    )
                    when(val response = productInBag.third.value) {
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
                                        userProductViewModel.removeProductFromBag(
                                            product = productInBag.first.productWithCount.product,
                                            onValueChanged = {
                                                productInBag.third.value = it
                                                if (it is Response.Success && !it.data) {
                                                    bagViewModel.deleteProductFromLocalBag(productInBag.first)
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
                    if (productInBag.first.productWithCount is FlowersWithDecoration) {
                        Text(
                            text = (productInBag.first.productWithCount as FlowersWithDecoration).decoration.title,
                            color = MaterialTheme.colors.secondary,
                            style = MaterialTheme.typography.subtitle1.copy(fontSize = 10.sp),
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    } else if (productInBag.first.productWithCount.product is Bouquet) {
                        val decorText = (productInBag.first.productWithCount.product as Bouquet).decoration.title
                        val postcardText = if ((productInBag.first.productWithCount.product as Bouquet).postcard.isNotEmpty())
                                               "Открытка"
                                           else
                                               "Без открытки"
                        val tableText = (productInBag.first.productWithCount.product as Bouquet).table.text
                        Text(
                            text = "$decorText • $postcardText • $tableText",
                            color = MaterialTheme.colors.secondary,
                            style = MaterialTheme.typography.subtitle1.copy(fontSize = 10.sp),
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
                Row {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.defaultMinSize(
                            minWidth = 60.dp
                        )
                    ) {
                        when(val response = productInBag.second.value) {
                            is Response.Loading -> {
                                CircularProgressIndicator(
                                    strokeWidth = 2.dp,
                                    modifier = Modifier
                                        .size(18.dp)
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
                                Button(
                                    contentPadding = PaddingValues(0.dp),
                                    shape = RoundedCornerShape(15.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = Color(0xFFE3E3E3)
                                    ),
                                    elevation = ButtonDefaults.elevation(
                                        defaultElevation = 0.dp,
                                        pressedElevation = 4.dp
                                    ),
                                    modifier = Modifier
                                        .size(20.dp),
                                    onClick = {
                                        if (productInBag.first.productWithCount.count > 1)
                                            bagViewModel.changeCount(productInBag, productInBag.first.productWithCount.count - 1)
                                        else
                                            userProductViewModel.removeProductFromBag(
                                                product = productInBag.first.productWithCount.product,
                                                onValueChanged = {
                                                    productInBag.second.value = it
                                                    if (it is Response.Success && !it.data) {
                                                        bagViewModel.deleteProductFromLocalBag(productInBag.first)
                                                    }
                                                }
                                            )
                                    }
                                ) {
                                    Image(
                                        imageVector = ImageVector
                                            .vectorResource(
                                                id = R.drawable.buttonbagminus
                                            ),
                                        contentDescription = "",
                                    )
                                }
                                Text(
                                    text = "${productInBag.first.productWithCount.count}",
                                    style = MaterialTheme.typography.h4.copy(fontSize = 12.sp),
                                    color = MaterialTheme.colors.onBackground,
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp)
                                )
                                Button(
                                    contentPadding = PaddingValues(0.dp),
                                    shape = RoundedCornerShape(15.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = Color(0xFFE3E3E3)
                                    ),
                                    elevation = ButtonDefaults.elevation(
                                        defaultElevation = 0.dp,
                                        pressedElevation = 4.dp
                                    ),
                                    modifier = Modifier
                                        .size(20.dp),
                                    onClick = {
                                        bagViewModel.changeCount(productInBag, productInBag.first.productWithCount.count + 1)
                                    }
                                ) {
                                    Image(
                                        imageVector = ImageVector
                                            .vectorResource(
                                                id = R.drawable.buttonbagplus
                                            ),
                                        contentDescription = ""
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
                            text = "${productInBag.first.totalPrice}",
                            style = MaterialTheme.typography.h3,
                            color = MaterialTheme.colors.onBackground
                        )
//                        if (productInBag.first.product is Product.Flower) {
//                            Text(
//                                text = "${productInBag.first.count * (productInBag.first.product as Product.Flower).totalPrice}",
//                                style = MaterialTheme.typography.h3,
//                                color = MaterialTheme.colors.onBackground
//                            )
//                        } else if (productInBag.first.product is Product.Bouquet) {
//                            Text(
//                                text = "${productInBag.first.count * (productInBag.first.product as Product.Bouquet).totalPrice}",
//                                style = MaterialTheme.typography.h3,
//                                color = MaterialTheme.colors.onBackground
//                            )
//                        } else {
//                            Text(
//                                text = "${productInBag.first.count * productInBag.first.product.price}",
//                                style = MaterialTheme.typography.h3,
//                                color = MaterialTheme.colors.onBackground
//                            )
//                        }
                    }
                }
            }

        }
    }
}