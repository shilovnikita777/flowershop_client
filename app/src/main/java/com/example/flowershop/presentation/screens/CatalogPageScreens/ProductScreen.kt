package com.example.flowershop.screens

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.flowershop.R
import com.example.flowershop.data.helpers.Response
import com.example.flowershop.domain.model.*
import com.example.flowershop.presentation.model.BouquetSize
import com.example.flowershop.presentation.model.ProductEnum
import com.example.flowershop.presentation.screens.CatalogPageScreens.ProductViewModel
import com.example.flowershop.presentation.screens.MainPageScreens.ProductBaseViewModel
import com.example.flowershop.presentation.screens.UserProductViewModel
import com.example.flowershop.presentation.screens.common.CustomTextField
import com.example.flowershop.presentation.screens.common.Separator
import com.example.flowershop.presentation.screens.common.h2
import com.example.flowershop.util.Constants.MAX_PRODUCT_COUNT
import com.example.flowershop.util.Constants.MIN_PRODUCT_COUNT

@Composable
fun ProductScreen(
    navController: NavHostController,
    productId: Int,
    productType : String
) {

    val productViewModel = hiltViewModel<ProductViewModel>()
    val userProductViewModel = hiltViewModel<UserProductViewModel>()

    val selectedDecoration by productViewModel.selectedDecoration
    val selectedTable by productViewModel.selectedTable

    val productResponse = productViewModel.currentProductResponse.value

    val isProductInBagResponse = productViewModel.isProductInBag.value
    val isProductInFavouriteResponse = productViewModel.isProductInFavourite.value

    val decorations = productViewModel.decorations.value
    val tables = productViewModel.tables.value

    val postcardMessage = productViewModel.postcardMessage.value

    val isTablesVisible by productViewModel.isTablesVisible

    when (productResponse) {
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
                text = productResponse.message,
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.h4,
                modifier = Modifier
                    .padding(top = 12.dp)
            )
        }
        is Response.Success -> {
            LaunchedEffect(key1 = Unit) {
//                Log.d("esp11",isProductInBagResponse.toString())
                if (isProductInBagResponse is Response.Loading) {
                    userProductViewModel.isProductInBag(
                        product = productResponse.data.productWithCount.product,
                        onValueChanged = {
                            productViewModel.changeProductInBagState(it)
                        }
                    )
                }
                if (isProductInFavouriteResponse is Response.Loading) {
                    userProductViewModel.isProductInFavourite(
                        product = productResponse.data.productWithCount.product,
                        onValueChanged = {
                            productViewModel.changeProductInFavouriteState(it)
                        }
                    )
                }
            }
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                val productInBag = productViewModel.currentProduct.value
                BoxWithConstraints {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(productInBag.productWithCount.product.image.getPath())
                            .build(),
                        contentDescription = productInBag.productWithCount.product.name,
                        placeholder = painterResource(id = R.drawable.bouquet_placeholder),
                        error = painterResource(id = R.drawable.escanor),
                        onError = {
                            Log.d("xd", it.result.throwable.message!!)
                        },
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(maxWidth)
                    )
                }

                MainInfo(
                    product = productInBag.productWithCount.product,
                    isProductInFavouriteResponse = isProductInFavouriteResponse,
                    productViewModel = productViewModel,
                    userProductViewModel = userProductViewModel
                )
                Separator(
                    modifier = Modifier
                        .padding(top = 16.dp)
                )

                if (productInBag.productWithCount.product is Bouquet) {
                    Composition((productInBag.productWithCount.product as Bouquet).flowers)
                    Separator(
                        modifier = Modifier
                            .padding(top = 16.dp)
                    )
                }

                Description(productInBag.productWithCount.product.description)
                Separator(
                    modifier = Modifier
                        .padding(top = 16.dp)
                )
                if (productInBag.productWithCount.product is Bouquet) {
                    Size(bouquetSize = (productInBag.productWithCount.product as Bouquet).size)
                    Separator(
                        modifier = Modifier
                            .padding(top = 16.dp)
                    )
                }

                if (productInBag.productWithCount.product !is Bouquet) {
                    Count(
                        product = if (productInBag.productWithCount.product is Flower)
                            ProductEnum.FLOWER else ProductEnum.PRODUCT,
                        value = productViewModel.count.value,
                        onValueChange = {
                            productViewModel.changeCount(it)
                        }
                    )
                    Separator(
                        modifier = Modifier
                            .padding(top = 16.dp)
                    )
                }

                if (productInBag.productWithCount.product is Flower || productInBag.productWithCount.product is Bouquet)
                {
                    Decoration(decorations,selectedDecoration) {
                        productViewModel.changeChosenDecoration(it)
                    }
                    Separator(
                        modifier = Modifier
                            .padding(top = 16.dp)
                    )
                }

                if (productInBag.productWithCount.product is Bouquet) {
                    Postcard(
                        value = postcardMessage
                    ) {
                        productViewModel.changePostcardMessage(it)
                    }
                    Separator(
                        modifier = Modifier
                            .padding(top = 16.dp)
                    )

                    Table(
                        tables = tables,
                        selectedTable = selectedTable,
                        isSectionVisible = isTablesVisible,
                        changeVisibility = {
                            productViewModel.changeTablesVisibility()
                        },
                        onItemClick = {
                            productViewModel.changeChosenTable(it)
                        }
                    )
                    Separator(
                        modifier = Modifier
                            .padding(top = 16.dp)
                    )

                    Count(
                        product = ProductEnum.BOUQUET,
                        value = productViewModel.count.value,
                        onValueChange = {
                            productViewModel.changeCount(it)
                        }
                    )
                    Separator(
                        modifier = Modifier
                            .padding(top = 16.dp)
                    )
                }

                ToBag(
                    productViewModel = productViewModel,
                    userProductViewModel = userProductViewModel
                )
            }
        }
//            else {
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    CircularProgressIndicator()
//                }
//            }
//        }
    }
}

@Composable
fun Size(bouquetSize: BouquetSize) {
    Text(
        text = "Размер букета",
        style = MaterialTheme.typography.h3.copy(fontSize = 20.sp),
        color = MaterialTheme.colors.onBackground,
        modifier = Modifier
            .padding(top = 16.dp, start = 24.dp)
    )
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(top = 12.dp, start = 24.dp)
            .height(75.dp)
            .border(width = 1.dp, color = MaterialTheme.colors.onBackground)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            Image(
                painterResource(id = R.drawable.size_bouquet),
                contentDescription = "bouquet",
                modifier = Modifier
                    .size(48.dp)
            )
            Image(
                painterResource(id = R.drawable.size_ruler),
                contentDescription = "ruler",
                modifier = Modifier
                    .padding(start = 4.dp)
                    .size(48.dp)
            )
            val size = when(bouquetSize) {
                is BouquetSize.Small -> {
                    "Маленький"
                }
                is BouquetSize.Medium -> {
                    "Средний"
                }
                is BouquetSize.Big -> {
                    "Большой"
                }
                is BouquetSize.Large -> {
                    "Огромный"
                }
            }
            Text(
                text = size,
                style = MaterialTheme.typography.subtitle1.copy(
                    fontSize = 16.sp
                ),
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier
                    .padding(start = 8.dp)
            )
        }
    }

}

@Composable
fun Description(description: String) {
    Text(
        text = "Описание",
        style = MaterialTheme.typography.h3.copy(fontSize = 20.sp),
        color = MaterialTheme.colors.onBackground,
        modifier = Modifier
            .padding(top = 16.dp, start = 24.dp)
    )
    Text(
        text = description,
        style = MaterialTheme.typography.subtitle1,
        color = MaterialTheme.colors.onBackground,
        modifier = Modifier
            .padding(top = 12.dp,start = 24.dp, end = 24.dp)
    )
}

@Composable
fun MainInfo(
    product: Product,
    isProductInFavouriteResponse : Response<Boolean>,
    productViewModel: ProductViewModel,
    userProductViewModel: UserProductViewModel
) {
    Column(
        modifier = Modifier
            .padding(top = 16.dp,start = 24.dp,end = 24.dp)
    ) {

        Row(
            modifier = Modifier
                .padding(top = 8.dp)
        ) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.h2,
                color = MaterialTheme.colors.onBackground,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .height(42.dp)
                    .padding(end = 8.dp)
            )
            when (isProductInFavouriteResponse) {
                is Response.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(20.dp),
                        strokeWidth = 2.dp
                    )
                }
                is Response.Error -> {
                    Text(
                        text = isProductInFavouriteResponse.message,
                        color = MaterialTheme.colors.onError,
                        style = MaterialTheme.typography.subtitle1
                    )
                }
                is Response.Success -> {
                    if (isProductInFavouriteResponse.data) {
                        Image(
                            imageVector = ImageVector
                                .vectorResource(
                                    id = R.drawable.heart_active
                                ),
                            contentDescription = "",
                            modifier = Modifier
                                .padding(top = 6.dp)
                                .clickable {
                                    userProductViewModel.removeProductFromFavourite(
                                        product = product
                                    ) {
                                        productViewModel.changeProductInFavouriteState(it)
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
                                .padding(top = 6.dp)
                                .clickable {
                                    userProductViewModel.addProductToFavourite(
                                        product = product
                                    ) {
                                        productViewModel.changeProductInFavouriteState(it)
                                    }
                                }
                        )
                    }

                }
            }
//            Image(
//                imageVector = ImageVector
//                    .vectorResource(
//                        id = R.drawable.heart
//                    ),
//                contentDescription = ""
//            )
        }
    }
}

@Composable
fun Composition(flowers: List<ProductWithCount>) {
    Column(
        modifier = Modifier
            .padding(top = 16.dp, start = 24.dp, end = 24.dp)
    ) {
        Text(
            text = "Состав",
            style = MaterialTheme.typography.h3.copy(fontSize = 20.sp),
            color = MaterialTheme.colors.onBackground,
        )
        flowers.forEach {
            Text(
                text = "${it.product.name} : ${it.count} шт",
                style = MaterialTheme.typography.h4,
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier
                    .padding(top = 12.dp)
            )
        }
//        Text(
//            text = "Роза : 5 шт",
//            style = MaterialTheme.typography.h4,
//            color = MaterialTheme.colors.onBackground,
//            modifier = Modifier
//                .padding(top = 12.dp)
//        )
//        Text(
//            text = "Гвоздика : 3 шт",
//            style = MaterialTheme.typography.h4,
//            color = MaterialTheme.colors.onBackground,
//            modifier = Modifier
//                .padding(top = 12.dp)
//        )
//        Text(
//            text = "Тюльпан : 3 шт",
//            style = MaterialTheme.typography.h4,
//            color = MaterialTheme.colors.onBackground,
//            modifier = Modifier
//                .padding(top = 12.dp)
//        )
    }
}

@Composable
fun Count(
    product: ProductEnum,
    value : String,
    onValueChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(top = 16.dp,start = 24.dp,end = 24.dp)
    ) {
        Text(
            text = "Количество",
            style = MaterialTheme.typography.h3.copy(fontSize = 20.sp),
            color = MaterialTheme.colors.onBackground
        )
        val text = when(product) {
            ProductEnum.PRODUCT -> {
                "Единиц товара, которое хотите приобрести"
            }
            ProductEnum.FLOWER -> {
                "Цветов, которое хотите приобрести"
            }
            ProductEnum.BOUQUET -> {
                "Букетов, которое хотите приобрести"
            }
        }

        Text(
            text = text,
            style = MaterialTheme.typography.subtitle1.copy(fontSize = 12.sp),
            color = MaterialTheme.colors.secondary,
            modifier = Modifier
                .padding(top = 12.dp)
        )
        Row(
            modifier = Modifier
                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
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
                enabled = value.toInt() > MIN_PRODUCT_COUNT,
                modifier = Modifier
                    .size(20.dp),
                onClick = {
                    if (value.toInt() > 1)
                        onValueChange((value.toInt() - 1).toString())
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
            OutlinedTextField(
                value = value,
                onValueChange = { onValueChange(it) },
                shape = RoundedCornerShape(10.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                ),
                singleLine = true,
                textStyle = MaterialTheme.typography.subtitle1
                    .copy(
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colors.onSecondary,
                        fontWeight = FontWeight.Medium
                    ),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = MaterialTheme.colors.secondaryVariant,
                    errorBorderColor = MaterialTheme.colors.onError,
                    cursorColor = MaterialTheme.colors.onBackground,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                ),
                placeholder = {
                    Text(
                        text = "1",
                        style = MaterialTheme.typography.subtitle1,
                        color = MaterialTheme.colors.secondary,
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .width(70.dp)
                    .height(50.dp)
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
                enabled = value.toInt() < MAX_PRODUCT_COUNT,
                modifier = Modifier
                    .size(20.dp),
                onClick = {
                    onValueChange((value.toInt() + 1).toString())
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

@Composable
fun Decoration(
    decorations: Response<List<Decoration>>,
    selectedDecoration: Decoration,
    onClick: (Decoration) -> Unit
) {
    Text(
        text = "Оформление",
        style = MaterialTheme.typography.h3.copy(fontSize = 20.sp),
        color = MaterialTheme.colors.onBackground,
        modifier = Modifier
            .padding(top = 16.dp, start = 24.dp)
    )
    when (decorations) {
        is Response.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .height(128.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is Response.Success -> {
            LazyRow(
                modifier = Modifier
                    .padding(top = 12.dp),
                contentPadding = PaddingValues(start = 24.dp,end = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(36.dp)
            ){
                items(decorations.data) { item ->
                    DecorationCard(decoration = item, imageId = R.drawable.ribbon,selectedDecoration,onClick)
                }
            }
        }
        is Response.Error -> {
            Text(
                text = decorations.message,
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.h4,
                modifier = Modifier
                    .padding(top = 12.dp)
            )
        }
    }

}

@Composable
fun DecorationCard(decoration: Decoration, imageId: Int, selectedDecoration: Decoration, onClick: (Decoration) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .selectable(
                selected = (selectedDecoration == decoration),
                onClick = { onClick(decoration) },
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            )

    ) {
        Box(
            modifier = Modifier
                .border(
                    width = 2.dp,
                    color = if (selectedDecoration == decoration)
                        MaterialTheme.colors.primaryVariant else Color.Transparent,
                    shape = RoundedCornerShape(15.dp)
                )
                .padding(all = 5.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(decoration.image.getPath())
                    .build(),
                contentDescription = "Decoration image",
                placeholder = painterResource(id = R.drawable.bouquet_placeholder),
                error = painterResource(id = R.drawable.profile_photo_error),
                onError = {
                    Log.d("xd", it.result.throwable.message!!)
                },
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(75.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
//            Image(
//                painter = painterResource(imageId),
//                contentDescription = "",
//                modifier = Modifier
//                    .size(75.dp)
//                    .clip(RoundedCornerShape(10.dp))
//            )
        }
        Text(
            text = decoration.title,
            style = MaterialTheme.typography.h4,
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier
                .padding(top = 4.dp)
        )
        Text(
            text = "${decoration.price} ₽",
            style = MaterialTheme.typography.h4,
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier
                .padding(top = 2.dp)
        )
    }
}

@Composable
fun Postcard(value: String, onValueChange: (String) -> Unit){
    Column(
        modifier = Modifier
            .padding(top = 16.dp,start = 24.dp)
    ){
        Text(
            text = "Открытка",
            style = MaterialTheme.typography.h3.copy(fontSize = 20.sp),
            color = MaterialTheme.colors.onBackground,
        )
        TextField(
            value = value,
            onValueChange = { onValueChange(it) },
            shape = RoundedCornerShape(15.dp),
            textStyle = MaterialTheme.typography.subtitle1
                .copy(color = MaterialTheme.colors.onSecondary,
                    fontWeight = FontWeight.Medium),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.secondaryVariant,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            placeholder = {
                Text(
                    text = "Что написать в открытке?",
                    style = MaterialTheme.typography.subtitle1,
                    color = MaterialTheme.colors.secondary
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .height(70.dp)
        )
    }
}

@Composable
fun Table(
    tables: Response<List<Table>>,
    selectedTable: Table,
    isSectionVisible: Boolean,
    changeVisibility: () -> Unit,
    onItemClick: (Table) -> Unit
){
    Column(
        modifier = Modifier
            .padding(top = 16.dp,start = 24.dp,end = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    changeVisibility()
                },
        ) {
            Text(
                text = "Табличка",
                style = MaterialTheme.typography.h3.copy(fontSize = 20.sp),
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier
                    .weight(1f)
            )
            if (tables is Response.Success && tables.data.isNotEmpty()){
                val rotation by animateFloatAsState(
                    targetValue = if (isSectionVisible) 180f else 0f
                )
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .rotate(rotation),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.arrow_down_2),
                        contentDescription = "arrow_down"
                    )
                }
            }
        }
        AnimatedVisibility(
            visible = isSectionVisible,
        ) {
            when (tables) {
                is Response.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is Response.Success -> {
                    Column {
                        Text(
                            text = "Табличка станет прекрасным дополнением к букету",
                            style = MaterialTheme.typography.subtitle1.copy(fontSize = 12.sp),
                            color = MaterialTheme.colors.secondary,
                            modifier = Modifier
                                .padding(top = 12.dp)
                        )
                        Column(
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .selectableGroup()
                        ) {
                            tables.data.forEach {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .padding(top = 8.dp)
                                        .selectable(
                                            selected = (selectedTable == it),
                                            onClick = { onItemClick(it) },
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = null,
                                            role = Role.RadioButton
                                        )
                                ) {
                                    RadioButton(
                                        selected = (selectedTable == it),
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = MaterialTheme.colors.onBackground,
                                            unselectedColor = Color.DarkGray,
                                            disabledColor = Color.LightGray
                                        ),
                                        onClick = null
                                    )
                                    Text(
                                        text = "${it.text} — ${it.price} ₽",
                                        style = MaterialTheme.typography.h4,
                                        color = MaterialTheme.colors.onBackground,
                                        modifier = Modifier
                                            .padding(start = 12.dp)
                                    )
                                }
                            }
                        }
                    }
                }
                is Response.Error -> {
                    Text(
                        text = tables.message,
                        color = MaterialTheme.colors.onBackground,
                        style = MaterialTheme.typography.h4,
                        modifier = Modifier
                            .padding(top = 12.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ToBag(
    productViewModel: ProductBaseViewModel,
    userProductViewModel: UserProductViewModel
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 24.dp, end = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "${productViewModel.price.value} ₽",
            style = MaterialTheme.typography.h2,
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier
                .align(alignment = Alignment.End)
        )
        when(val isProductInBagResponse = productViewModel.isProductInBag.value) {
            is Response.Loading -> {
                Box(
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .height(65.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is Response.Error -> {
                Text(
                    text = isProductInBagResponse.message,
                    color = MaterialTheme.colors.onBackground,
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier
                        .padding(top = 12.dp)
                )
            }
            is Response.Success -> {
                if (isProductInBagResponse.data) {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                            .height(65.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.primaryVariant,
                            disabledBackgroundColor = MaterialTheme.colors.onError
                        ),
                        shape = RoundedCornerShape(56.dp),
                        onClick = {
                            userProductViewModel.updateProductInBag(
                                productWithCount = productViewModel.getProduct().productWithCount,
                                onValueChanged = {
                                    productViewModel.changeProductInBagState(it)
                                }
                            )
                        }
                    ) {
                        Text(
                            text = "Обновить товар в корзине",
                            style = MaterialTheme.typography.h3,
                            color = Color.White
                        )
                    }
                } else {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                            .height(65.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.primaryVariant,
                            disabledBackgroundColor = MaterialTheme.colors.onError
                        ),
                        shape = RoundedCornerShape(56.dp),
                        onClick = {
                            userProductViewModel.addProductToBag(
                                productWithCount = productViewModel.getProduct().productWithCount,
                                onValueChanged = {
                                    productViewModel.changeProductInBagState(it)
                                }
                            )
                        }
                    ) {
                        Text(
                            text = "Добавить в корзину",
                            style = MaterialTheme.typography.h3,
                            color = Color.White
                        )
                    }
                }
            }
        }

    }
}