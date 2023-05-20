package com.example.flowershop.presentation.screens.MainPageScreens

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.flowershop.R
import com.example.flowershop.presentation.model.PromocodeUI
import com.example.flowershop.presentation.navigation.CatalogNavRoute
import com.example.flowershop.presentation.navigation.MainNavRoute
import com.example.flowershop.presentation.screens.UserProductViewModel
import com.example.flowershop.presentation.screens.common.h2
import com.example.flowershop.ui.theme.CustomLightRed
import com.example.flowershop.data.helpers.Response
import com.example.flowershop.domain.model.*
import com.example.flowershop.presentation.screens.common.noRippleClickable

@Composable
fun HomeScreen(navController: NavHostController) {

    val viewModel = hiltViewModel<HomeViewModel>()

    val userProductViewModel = hiltViewModel<UserProductViewModel>()

    val popularProducts = viewModel.popularProducts.value

    val newProducts = viewModel.newProducts.value

    val promocodes = viewModel.promocodes.value

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(top = 64.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
        ) {
            when(val response = viewModel.username.value) {
                is Response.Error -> {
                    Text(
                        text = response.message,
                        color = MaterialTheme.colors.onError,
                        style = MaterialTheme.typography.subtitle1
                    )
                }
                is Response.Success -> {
                    h2(
                        text = "Здравствуйте, ${response.data} !"
                    )
                }
                is Response.Loading -> {
                    CircularProgressIndicator(
                        strokeWidth = 3.dp,
                        modifier = Modifier
                            .size(30.dp)
                    )
                }
            }
            Text(
                text = "Цветы для вас на любой вкус !",
                style = MaterialTheme.typography.h4,
                color = MaterialTheme.colors.secondary,
                modifier = Modifier
                    .padding(top = 12.dp)
            )
        }
        when (promocodes) {
//            is Response.Loading -> {
//                LazyRow(
//                    modifier = Modifier
//                        .padding(top = 12.dp),
//                    contentPadding = PaddingValues(
//                        start = 12.dp,
//                        end = 12.dp
//                    )
//                ) {
//                    items(3) {
//                        DiscountCard()
//                    }
//                }
//            }
//            is Response.Success -> {
//                LazyRow(
//                    modifier = Modifier
//                        .padding(top = 12.dp),
//                    contentPadding = PaddingValues(
//                        start = 12.dp,
//                        end = 12.dp
//                    )
//                ) {
//                    items(newsBanners.data) {
//                        DiscountCard(imageId = it.toInt())
//                    }
//                }
//            }
            is Response.Error -> {
                Text(
                    text = promocodes.message,
                    color = MaterialTheme.colors.onBackground,
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .height(75.dp)
                )
            }
            else -> {
                LazyRow(
                    modifier = Modifier
                        .padding(top = 12.dp),
                    contentPadding = PaddingValues(
                        start = 24.dp,
                        end = 12.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (promocodes is Response.Loading) {
                        items(3) {
                            DiscountCard()
                        }
                    } else if (promocodes is Response.Success) {
                        if (promocodes.data.isNotEmpty()) {
                            items(promocodes.data) {
                                DiscountCard(it)
                            }
                        }
                    }
                }
            }
        }

        h2(
            text = "Популярное",
            modifier = Modifier
                .padding(top = 24.dp, start = 24.dp)
        )

        when (popularProducts) {
//            is Response.Loading -> {
//                LazyRow(
//                    modifier = Modifier
//                        .padding(top = 12.dp),
//                    contentPadding = PaddingValues(start = 24.dp, end = 16.dp),
//                    horizontalArrangement = Arrangement.spacedBy(16.dp)
//                ) {
//                    items(6) {
//                        ProductBigCardPlaceholder()
//                    }
//                }
//            }
//            is Response.Success -> {
//                LazyRow(
//                    modifier = Modifier
//                        .padding(top = 12.dp),
//                    contentPadding = PaddingValues(start = 24.dp, end = 16.dp),
//                    horizontalArrangement = Arrangement.spacedBy(16.dp)
//                ) {
//                    items(popularProducts.data) {
//                        ProductBigCard(
//                            product = it,
//                            navController = navController
//                        )
//                    }
//                }
//            }
            is Response.Error -> {
                Text(
                    text = popularProducts.message,
                    color = MaterialTheme.colors.onBackground,
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .height(75.dp)
                )
            } else -> {
                if (popularProducts is Response.Loading){
                    LazyRow(
                        modifier = Modifier
                            .padding(top = 12.dp),
                        contentPadding = PaddingValues(start = 24.dp, end = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(6) {
                            ProductBigCardPlaceholder()
                        }
                    }
                } else if (popularProducts is Response.Success) {
                    LaunchedEffect(key1 = Unit) {
                        popularProducts.data.forEach { product->
                            userProductViewModel.isProductInBag(product.first) {
                                product.second.value = it
                            }
                            userProductViewModel.isProductInFavourite(product.first) {
                                product.third.value = it
                            }
                        }
                    }
                    LazyRow(
                        modifier = Modifier
                            .padding(top = 12.dp),
                        contentPadding = PaddingValues(start = 24.dp, end = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(popularProducts.data) {
                            ProductBigCard(
                                product = it,
                                navController = navController,
                                viewModel = userProductViewModel
                            )
                        }
                    }
                }
            }
        }

        h2(
            text = "Конструктор букета",
            modifier = Modifier
                .padding(top = 12.dp, start = 24.dp, end = 24.dp)
        )
        Button(
            shape = RoundedCornerShape(6.dp),
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier
                .padding(top = 12.dp, start = 24.dp, end = 24.dp)
                .height(190.dp),
            onClick = {
                navController.navigate(MainNavRoute.Constructor.passId())
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
                    .background(Color(0xFFFFA3A3))
            ) {
                Image(
                    painter = painterResource(R.drawable.konstructorflower),
                    contentDescription = "",
                    modifier = Modifier
                        .height(184.dp)
                        .align(Alignment.BottomStart)
                        .rotate(15f)
                        .offset(x = (-35).dp, y = 75.dp)
                )
                Column(
                    modifier = Modifier
                        .padding(top = 12.dp, start = 16.dp, end = 16.dp)
                        .zIndex(1f)
                ) {
                    Text(
                        text = "Соберите свой букет",
                        style = MaterialTheme.typography.h1.copy(fontSize = 22.sp),
                        color = Color(0xFF8E0101)
                    )
                    Text(
                        text = "только то, что выберете сами",
                        style = MaterialTheme.typography.subtitle1,
                        color = CustomLightRed
                    )
                }
            }
        }
        h2(
            text = "Новинки",
            modifier = Modifier
                .padding(top = 24.dp, start = 24.dp)
        )
        when (newProducts) {
            is Response.Loading -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .heightIn(max = 1000.dp),
                    contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    userScrollEnabled = false
                ) {
                    items(6) {
                        ProductSmallCardPlaceholder()
                    }
                }
            }
            is Response.Success -> {
                LaunchedEffect(key1 = Unit) {
                    newProducts.data.forEach { product ->
                        userProductViewModel.isProductInBag(product.first) {
                            product.second.value = it
                        }
                        userProductViewModel.isProductInFavourite(product.first) {
                            product.third.value = it
                        }
                    }
                }
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .heightIn(max = 1000.dp),
                    contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    userScrollEnabled = false
                ) {
                    items(newProducts.data) {
                        ProductSmallCard(
                            product = it,
                            navController = navController,
                            viewModel = userProductViewModel
                        )
                    }
                }
            }
            is Response.Error -> {
                Text(
                    text = newProducts.message,
                    color = MaterialTheme.colors.onBackground,
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .height(75.dp)
                )
            }
        }
    }
}

@Composable
fun DiscountCard(promocodeUI: PromocodeUI? = null) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(75.dp),
        elevation = 2.dp,
        shape = RoundedCornerShape(6.dp)
    ) {
        if (promocodeUI == null) {
            Image(
                painter = painterResource(id = R.drawable.promocode_placeholder),
                contentDescription = "Promocode placeholder",
                modifier = Modifier
                    .width(200.dp)
                    .height(75.dp)
                    .clip(RoundedCornerShape(6.dp))
            )
        } else {
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
                    modifier = Modifier
                        .width(200.dp)
                        .height(75.dp)
                        .clip(RoundedCornerShape(6.dp))
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
                modifier = Modifier
                    .width(200.dp)
                    .height(75.dp)
                    .clip(RoundedCornerShape(6.dp))
            )
            Column(
                modifier = Modifier
                    .padding(top = 8.dp,start = 10.dp)
            ) {
                Text(
                    text = "${promocodeUI.amount}%",
                    style = MaterialTheme.typography.h1.copy(fontSize = 32.sp),
                    color = promocodeUI.amountColor
                )
                Text(
                    text = promocodeUI.title,
                    style = MaterialTheme.typography.h4.copy(fontSize = 10.sp),
                    color = promocodeUI.titleColor,
                    modifier = Modifier
                        .offset(y = (-6).dp)
                )
            }

        }
    }
}

@Composable
fun FlowerCategoryCard(title: String, color: Color, imageId: Int) {
    Column(
        modifier = Modifier
            .padding(start = 24.dp)
            .width(75.dp)
            .height(96.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(75.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(color),
            contentAlignment = if (imageId != R.drawable.toulpan)
                Alignment.Center
            else
                Alignment.BottomStart
        ) {
            Image(
                painter = painterResource(imageId),
                contentDescription = "",
                modifier = Modifier
                    .size(if (imageId != R.drawable.toulpan) 55.dp else 75.dp)
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.subtitle1.copy(
                fontSize = 12.sp
            ),
            color = MaterialTheme.colors.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight()
        )
    }
}


@Composable
fun ProductBigCard(
    product: Triple<Product, MutableState<Response<Boolean>>, MutableState<Response<Boolean>>>,
    navController: NavHostController,
    viewModel: UserProductViewModel
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .width(221.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                navController.navigate(CatalogNavRoute.Product.passIdAndType(
                    product.first.id,
                    product.first.type
                ))
            },
        elevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .padding(all = 6.dp)
        ) {

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(product.first.image.getPath())
                    .build(),
                contentDescription = product.first.name,
                placeholder = painterResource(id = R.drawable.bouquet_placeholder),
                error = painterResource(id = R.drawable.bouquet_placeholder),
                onError = {
                    Log.d("xd", it.result.throwable.message!!)
                },
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(209.dp)
                    .clip(RoundedCornerShape(10.dp))
            )

            Column(
                modifier = Modifier
                    .padding(top = 8.dp, start = 6.dp, end = 6.dp)
            ) {
//                Text(
//                    text = product.first.name,
//                    style = MaterialTheme.typography.h3,
//                    color = MaterialTheme.colors.onBackground,
//                    maxLines = 2,
//                    overflow = TextOverflow.Ellipsis,
//                    modifier = Modifier
//                        .height(42.dp)
//                )
                Row(
                    modifier = Modifier
                        .padding(top = 6.dp)
                ) {
                    Text(
                        text = product.first.name,
                        style = MaterialTheme.typography.h3,
                        color = MaterialTheme.colors.onBackground,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .weight(1f)
                            .height(42.dp)
                            .padding(end = 8.dp)
                    )
                    when (val response = product.third.value) {
                        is Response.Loading -> {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(20.dp),
                                strokeWidth = 2.dp
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
                            if (response.data) {
                                Image(
                                    imageVector = ImageVector
                                        .vectorResource(
                                            id = R.drawable.heart_active
                                        ),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .clickable {
                                            viewModel.removeProductFromFavourite(
                                                product = product.first
                                            ) {
                                                product.third.value = it
                                            }
                                        }
                                )
                            } else {
                                Image(
                                    imageVector = ImageVector
                                        .vectorResource(
                                            id = R.drawable.heart
                                        ),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .clickable {
                                            viewModel.addProductToFavourite(
                                                product = product.first
                                            ) {
                                                product.third.value = it
                                            }
                                        }
                                )
                            }

                        }
                    }
//                    Image(
//                        imageVector = ImageVector
//                            .vectorResource(
//                                id = R.drawable.heart
//                            ),
//                        contentDescription = ""
//                    )
                }
                Row(
                    modifier = Modifier
                        .padding(top = 12.dp)
                ) {
                    when(val response = product.second.value) {
                        is Response.Loading -> {
                            Log.d("xd",response.toString() + "loading")
                            Box(
                                modifier = Modifier
                                    .width(110.dp)
                                    .height(35.dp),
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
                            Log.d("xd",response.toString() + "error")
                            Text(
                                text = response.message,
                                color = MaterialTheme.colors.onError,
                                style = MaterialTheme.typography.subtitle1
                            )
                        }
                        is Response.Success -> {
                            Log.d("xd", response.toString() + "success")
                            if (response.data) {
                                Button(
                                    modifier = Modifier
                                        .width(110.dp)
                                        .height(35.dp),
                                    contentPadding = PaddingValues(0.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = MaterialTheme.colors.primaryVariant
                                    ),
                                    shape = RoundedCornerShape(6.dp),
                                    onClick = {
                                        viewModel.removeProductFromBag(product.first) {
                                            product.second.value = it
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
                                        .width(110.dp)
                                        .height(35.dp),
                                    contentPadding = PaddingValues(0.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = MaterialTheme.colors.primaryVariant
                                    ),
                                    shape = RoundedCornerShape(6.dp),
                                    onClick = {
                                        val productWithCount = if (product.first is Flower) FlowersWithDecoration(
                                            product = product.first as Flower,
                                            count = 1,
                                            decoration = Decoration()
                                        ) else {
                                            ProductWithCount(
                                                product = product.first,
                                                count = 1
                                            )
                                        }
                                        viewModel.addProductToBag(
                                            productWithCount = productWithCount
                                        ) {
                                            product.second.value = it
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
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(alignment = Alignment.CenterVertically)
                    ) {
                        Text(
                            text = "₽ ",
                            style = MaterialTheme.typography.h3.copy(fontSize = 18.sp),
                            color = MaterialTheme.colors.primaryVariant

                        )
                        Text(
                            text = "${product.first.price}",
                            style = MaterialTheme.typography.h3.copy(fontSize = 18.sp),
                            color = MaterialTheme.colors.onBackground
                        )
                    }

                }
            }
        }
    }
}

//@Composable
//fun ProductBigCard(product: Product, navController: NavHostController) {
//    Card(
//        shape = RoundedCornerShape(10.dp),
//        modifier = Modifier
//            .width(221.dp)
//            .clickable(
//                interactionSource = remember { MutableInteractionSource() },
//                indication = null
//            ) {
//                navController.navigate(CatalogNavRoute.Product.passId(product.id))
//            },
//        elevation = 2.dp
//    ) {
//        Column(
//            modifier = Modifier
//                .padding(all = 6.dp)
//        ) {
//
//            AsyncImage(
//                model = ImageRequest.Builder(LocalContext.current)
//                    .data(product.image)
//                    .build(),
//                contentDescription = product.name,
//                placeholder = painterResource(id = R.drawable.bouquet_placeholder),
//                error = painterResource(id = R.drawable.bouquet_placeholder),
//                onError = {
//                    Log.d("xd", it.result.throwable.message!!)
//                },
//                contentScale = ContentScale.Crop,
//                modifier = Modifier
//                    .size(209.dp)
//                    .clip(RoundedCornerShape(10.dp))
//            )
//
//            Column(
//                modifier = Modifier
//                    .padding(top = 8.dp, start = 6.dp, end = 6.dp)
//            ) {
//                Text(
//                    text = product.name,
//                    style = MaterialTheme.typography.h3,
//                    color = MaterialTheme.colors.onBackground,
//                    maxLines = 2,
//                    overflow = TextOverflow.Ellipsis,
//                    modifier = Modifier
//                        .height(42.dp)
//                )
//                Row(
//                    modifier = Modifier
//                        .padding(top = 6.dp)
//                ) {
//                    Image(
//                        imageVector = ImageVector
//                            .vectorResource(
//                                id = R.drawable.star
//                            ),
//                        contentDescription = "",
//                        modifier = Modifier
//                            .size(18.dp)
//                    )
//                    Text(
//                        text = "${product.rating.value}",
//                        style = MaterialTheme.typography.h3.copy(fontSize = 16.sp),
//                        color = MaterialTheme.colors.onBackground,
//                        modifier = Modifier
//                            .padding(start = 4.dp)
//                    )
//                    Spacer(
//                        modifier = Modifier
//                            .weight(1f)
//                    )
//                    Image(
//                        imageVector = ImageVector
//                            .vectorResource(
//                                id = R.drawable.heart
//                            ),
//                        contentDescription = ""
//                    )
//                }
//                Row(
//                    modifier = Modifier
//                        .padding(top = 12.dp)
//                ) {
//                    Button(
//                        modifier = Modifier
//                            .width(110.dp)
//                            .height(35.dp),
//                        contentPadding = PaddingValues(0.dp),
//                        colors = ButtonDefaults.buttonColors(
//                            backgroundColor = MaterialTheme.colors.primaryVariant
//                        ),
//                        shape = RoundedCornerShape(6.dp),
//                        onClick = {
//
//                        }
//                    ) {
//                        Text(
//                            text = "В корзину",
//                            style = MaterialTheme.typography.h3.copy(fontSize = 12.sp),
//                            color = MaterialTheme.colors.background
//                        )
//                    }
//                    Row(
//                        horizontalArrangement = Arrangement.End,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .align(alignment = Alignment.CenterVertically)
//                    ) {
//                        Text(
//                            text = "₽ ",
//                            style = MaterialTheme.typography.h3.copy(fontSize = 18.sp),
//                            color = MaterialTheme.colors.primaryVariant
//
//                        )
//                        Text(
//                            text = "${product.price}",
//                            style = MaterialTheme.typography.h3.copy(fontSize = 18.sp),
//                            color = MaterialTheme.colors.onBackground
//                        )
//                    }
//
//                }
//            }
//        }
//    }
//}

@Composable
fun ProductBigCardPlaceholder() {
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .width(221.dp),
        elevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .padding(all = 6.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.bouquet_placeholder),
                contentDescription = "bouquet placeholder",
                modifier = Modifier
                    .size(209.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
            Column(
                modifier = Modifier
                    .padding(top = 8.dp, start = 6.dp, end = 6.dp)
            ) {

                Row(
                    modifier = Modifier
                        .padding(top = 6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(42.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .width(100.dp)
                                .height(20.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(MaterialTheme.colors.onSurface)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .width(20.dp)
                            .height(19.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(MaterialTheme.colors.onSurface)
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(top = 12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .width(110.dp)
                            .height(35.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(MaterialTheme.colors.onSurface)
                    )
                    Row(
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(alignment = Alignment.CenterVertically)
                    ) {
                        Text(
                            text = "₽ ",
                            style = MaterialTheme.typography.h3.copy(fontSize = 18.sp),
                            color = MaterialTheme.colors.primaryVariant

                        )
                        Box(
                            modifier = Modifier
                                .padding(start = 4.dp)
                                .width(45.dp)
                                .height(18.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(MaterialTheme.colors.onSurface)
                        )
                    }

                }
            }
        }
    }
}

@Composable
fun ProductSmallCard(
    product: Triple<Product, MutableState<Response<Boolean>>, MutableState<Response<Boolean>>>,
    navController: NavHostController,
    viewModel: UserProductViewModel
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        elevation = 2.dp,
        modifier = Modifier
            .noRippleClickable {
                navController.navigate(CatalogNavRoute.Product.passIdAndType(
                    product.first.id,
                    product.first.type
                ))
            }
    ) {
        Column(
            modifier = Modifier
                .padding(all = 6.dp)
        ) {
            BoxWithConstraints {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(product.first.image.getPath())
                        .build(),
                    contentDescription = product.first.name,
                    placeholder = painterResource(id = R.drawable.bouquet_placeholder),
                    error = painterResource(id = R.drawable.escanor),
                    onError = {
                        Log.d("xd", it.result.throwable.message!!)
                    },
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(maxWidth)
                        .clip(RoundedCornerShape(10.dp))
                )
            }
            Column(
                modifier = Modifier
                    .padding(top = 8.dp, start = 6.dp, end = 6.dp)
            ) {
                Row {
                    Text(
                        text = product.first.name,
                        style = MaterialTheme.typography.h3.copy(fontSize = 14.sp),
                        color = MaterialTheme.colors.onBackground,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp)
                            .padding(end = 8.dp)
                    )
                    when (val response = product.third.value) {
                        is Response.Loading -> {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(16.dp),
                                strokeWidth = 2.dp
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
                            if (response.data) {
                                Image(
                                    imageVector = ImageVector
                                        .vectorResource(
                                            id = R.drawable.heart_active
                                        ),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .width(16.dp)
                                        .clickable {
                                            viewModel.removeProductFromFavourite(product.first) {
                                                product.third.value = it
                                            }
                                        }
                                )
                            } else {
                                Image(
                                    imageVector = ImageVector
                                        .vectorResource(
                                            id = R.drawable.heart
                                        ),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .width(16.dp)
                                        .clickable {
                                            viewModel.addProductToFavourite(product.first) {
                                                product.third.value = it
                                            }
                                        }
                                )
                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .padding(top = 4.dp)
                ) {
                    Row {
                        Text(
                            text = "₽ ",
                            style = MaterialTheme.typography.h3.copy(fontSize = 14.sp),
                            color = MaterialTheme.colors.primaryVariant

                        )
                        Text(
                            text = "${product.first.price}",
                            style = MaterialTheme.typography.h3.copy(fontSize = 14.sp),
                            color = MaterialTheme.colors.onBackground
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                    )
                    when (val response = product.second.value) {
                        is Response.Error -> {
                            Text(
                                text = response.message,
                                color = MaterialTheme.colors.onError,
                                style = MaterialTheme.typography.subtitle1
                            )
                        }
                        is Response.Loading -> {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(18.dp),
                                strokeWidth = 2.dp
                            )
                        }
                        is Response.Success -> {
                            if (response.data) {
                                Image(
                                    imageVector = ImageVector
                                        .vectorResource(
                                            id = R.drawable.confirmed
                                        ),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clickable {
                                            viewModel.removeProductFromBag(product.first) {
                                                product.second.value = it
                                            }
                                        }
                                )
                            } else {
                                Image(
                                    imageVector = ImageVector
                                        .vectorResource(
                                            id = R.drawable.buy_selected
                                        ),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clickable {
                                            val productWithCount =
                                                if (product.first is Flower) FlowersWithDecoration(
                                                    product = product.first as Flower,
                                                    count = 1,
                                                    decoration = Decoration()
                                                ) else {
                                                    ProductWithCount(
                                                        product = product.first,
                                                        count = 1
                                                    )
                                                }
                                            viewModel.addProductToBag(
                                                productWithCount = productWithCount
                                            ) {
                                                product.second.value = it
                                            }
                                        }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

//@Composable
//fun ProductSmallCard(product: Product, navController: NavHostController) {
//    Card(
//        shape = RoundedCornerShape(10.dp),
//        elevation = 2.dp,
//        modifier = Modifier
//            .clickable(
//                interactionSource = remember { MutableInteractionSource() },
//                indication = null
//            ) {
//                navController.navigate(CatalogNavRoute.Product.passId(product.id))
//            }
//    ) {
//        Column(
//            modifier = Modifier
//                .padding(all = 6.dp)
//        ) {
//            BoxWithConstraints {
//                AsyncImage(
//                    model = ImageRequest.Builder(LocalContext.current)
//                        .data(product.image)
//                        .build(),
//                    contentDescription = product.name,
//                    placeholder = painterResource(id = R.drawable.bouquet_placeholder),
//                    error = painterResource(id = R.drawable.escanor),
//                    onError = {
//                        Log.d("xd", it.result.throwable.message!!)
//                    },
//                    contentScale = ContentScale.Crop,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(maxWidth)
//                        .clip(RoundedCornerShape(10.dp))
//                )
//            }
//            Column(
//                modifier = Modifier
//                    .padding(top = 8.dp, start = 6.dp, end = 6.dp)
//            ) {
//                Row {
//                    Text(
//                        text = product.name,
//                        style = MaterialTheme.typography.h3.copy(fontSize = 14.sp),
//                        color = MaterialTheme.colors.onBackground,
//                        maxLines = 2,
//                        overflow = TextOverflow.Ellipsis,
//                        modifier = Modifier
//                            .weight(1f)
//                            .height(36.dp)
//                            .padding(end = 8.dp)
//                    )
//                    Image(
//                        imageVector = ImageVector
//                            .vectorResource(
//                                id = R.drawable.heart
//                            ),
//                        contentDescription = "",
//                        modifier = Modifier
//                            .width(16.dp)
//                    )
//                }
//                Row(
//                    modifier = Modifier
//                        .padding(top = 4.dp)
//                ) {
//                    Row {
//                        Text(
//                            text = "₽ ",
//                            style = MaterialTheme.typography.h3.copy(fontSize = 14.sp),
//                            color = MaterialTheme.colors.primaryVariant
//
//                        )
//                        Text(
//                            text = "${product.price}",
//                            style = MaterialTheme.typography.h3.copy(fontSize = 14.sp),
//                            color = MaterialTheme.colors.onBackground
//                        )
//                    }
//                    Spacer(
//                        modifier = Modifier
//                            .weight(1f)
//                    )
//                    Image(
//                        imageVector = ImageVector
//                            .vectorResource(
//                                id = R.drawable.buy_selected
//                            ),
//                        contentDescription = "",
//                        modifier = Modifier
//                            .size(18.dp)
//                    )
//                }
//            }
//        }
//    }
//}

@Composable
fun ProductSmallCardPlaceholder() {
    Card(
        shape = RoundedCornerShape(10.dp),
        elevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .padding(all = 6.dp)
        ) {
            BoxWithConstraints {
                Image(
                    painter = painterResource(id = R.drawable.bouquet_placeholder),
                    contentDescription = "bouquet placeholder",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(maxWidth)
                        .clip(RoundedCornerShape(10.dp))
                )
            }
            Column(
                modifier = Modifier
                    .padding(top = 8.dp, start = 6.dp, end = 6.dp)
            ) {
                Row {
                    Box(
                        modifier = Modifier
                            .height(36.dp)
                            .padding(end = 8.dp)
                            .weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .width(75.dp)
                                .height(18.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(MaterialTheme.colors.onSurface)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(MaterialTheme.colors.onSurface)
                    )
//                    Image(
//                        imageVector = ImageVector
//                            .vectorResource(
//                                id = R.drawable.heart
//                            ),
//                        contentDescription = "like",
//                        modifier = Modifier
//                            .width(16.dp)
//                    )
                }
                Row(
                    modifier = Modifier
                        .padding(top = 4.dp)
                ) {
                    Row {
                        Text(
                            text = "₽ ",
                            style = MaterialTheme.typography.h3.copy(fontSize = 14.sp),
                            color = MaterialTheme.colors.primaryVariant

                        )
                        Box(
                            modifier = Modifier
                                .width(45.dp)
                                .height(18.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(MaterialTheme.colors.onSurface)
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                    )
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(MaterialTheme.colors.onSurface)
                    )
                }
            }
        }
    }
}
