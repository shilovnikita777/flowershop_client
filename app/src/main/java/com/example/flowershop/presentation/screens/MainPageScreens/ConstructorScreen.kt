@file:OptIn(ExperimentalMaterialApi::class, ExperimentalMaterialApi::class)

package com.example.flowershop.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.flowershop.R
import com.example.flowershop.data.helpers.Response
import com.example.flowershop.domain.model.Flower
import com.example.flowershop.presentation.model.ProductEnum
import com.example.flowershop.presentation.model.ProductWithCountState
import com.example.flowershop.presentation.screens.CatalogPageScreens.Search
import com.example.flowershop.presentation.screens.CatalogPageScreens.SortAndFilter
import com.example.flowershop.presentation.screens.MainPageScreens.ConstructorViewModel
import com.example.flowershop.presentation.screens.MainPageScreens.ProductBaseViewModel
import com.example.flowershop.presentation.screens.UserProductViewModel
import com.example.flowershop.presentation.screens.common.Separator
import com.example.flowershop.presentation.screens.common.h2
import com.example.flowershop.presentation.screens.common.noRippleClickable
import com.example.flowershop.util.Constants.NO_PRODUCT_CONSTANT
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ConstructorScreen(navController: NavHostController, productId: Int) {
    val constructorViewModel = hiltViewModel<ConstructorViewModel>()
    val userProductViewModel = hiltViewModel<UserProductViewModel>()

    val productResponse = constructorViewModel.currentProductResponse.value

    val postcardMessage by constructorViewModel.postcardMessage

    val tables by constructorViewModel.tables
    val decorations by constructorViewModel.decorations

    val selectedTable by constructorViewModel.selectedTable
    val selectedDecoration by constructorViewModel.selectedDecoration

    val isTablesVisible by constructorViewModel.isTablesVisible

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = constructorViewModel.bottomSheetState
    )

    val coroutineScope = rememberCoroutineScope()

    if (constructorViewModel.productId == NO_PRODUCT_CONSTANT || productResponse is Response.Success) {

        if (productResponse is Response.Success) {
            //constructorViewModel.changeProductInBagState(Response.Success(true))
            val isProductInBagResponse = constructorViewModel.isProductInBag.value
            if (isProductInBagResponse is Response.Loading) {
                userProductViewModel.isAuthorBouquetInBag(
                    productId = productResponse.data.productWithCount.product.id,
                    onValueChanged = {
                        constructorViewModel.changeProductInBagState(it)
                    }
                )
            }
        } else {
            constructorViewModel.changeProductInBagState(Response.Success(false))
        }

        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            sheetContent = {
                BottomSheetContent(constructorViewModel)
            },
            sheetPeekHeight = 0.dp,
            sheetShape = RoundedCornerShape(15.dp,15.dp,0.dp,0.dp)
        ) {
            BackHandler(
                enabled = constructorViewModel.bottomSheetState.isExpanded
            ) {
                coroutineScope.launch {
                    constructorViewModel.onDismissSheet()
                }
            }
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                PreviewImage()
                h2(
                    text = "Ваш авторский букет",
                    color = MaterialTheme.colors.onBackground,
                    modifier = Modifier
                        .padding(top = 24.dp, start = 24.dp, end = 24.dp)
                )
                Separator(
                    modifier = Modifier
                        .padding(top = 16.dp)
                )

                Structure(
                    flowers = constructorViewModel.flowersInBouquet,
                    viewModel = constructorViewModel
                )
                Separator()

                Decoration(decorations,selectedDecoration) {
                    constructorViewModel.changeChosenDecoration(it)
                }

                Separator(
                    modifier = Modifier
                        .padding(top = 16.dp)
                )

                Postcard(
                    value = postcardMessage
                ) {
                    constructorViewModel.changePostcardMessage(it)
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
                        constructorViewModel.changeTablesVisibility()
                    },
                    onItemClick = {
                        constructorViewModel.changeChosenTable(it)
                        //viewModel.recalculatePrice()
                    }
                )
                Separator(
                    modifier = Modifier
                        .padding(top = 16.dp)
                )

                Count(
                    product = ProductEnum.BOUQUET,
                    value = constructorViewModel.count.value,
                    onValueChange = {
                        constructorViewModel.changeCount(it)
                    }
                )
                Separator(
                    modifier = Modifier
                        .padding(top = 16.dp)
                )

                ToBag(
                    productViewModel = constructorViewModel,
                    userProductViewModel = userProductViewModel,
                    isButtonEnabled = constructorViewModel.flowersInBouquet.isNotEmpty(),
                    isAuthor = true
                )
            }
        }
    } else if (productResponse is Response.Loading) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (productResponse is Response.Error) {
        Text(
            text = productResponse.message,
            color = MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.h4,
            modifier = Modifier
                .padding(top = 12.dp)
        )
    }
}

@Composable
fun Step(number: Int,title: String){
    Text(
        text = "$number",
        style = MaterialTheme.typography.h2,
        color = MaterialTheme.colors.onBackground
    )
    Text(
        text = title,
        style = MaterialTheme.typography.subtitle1.copy(fontSize = 12.sp),
        color = MaterialTheme.colors.onBackground,
        modifier = Modifier
            .padding(top = 4.dp)
    )
}

@Composable
fun ArrowDown(){
    Image(
        imageVector = ImageVector
            .vectorResource(
                id = R.drawable.arrow_down
            ),
        contentDescription = "Arrow Down",
        modifier = Modifier
            .padding(top = 8.dp, bottom = 8.dp)
    )
}

@Composable
fun PreviewImage(){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(top = 48.dp, start = 24.dp, end = 24.dp)
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Step(1,"Состав")
            ArrowDown()

            Step(2,"Оформление")
            ArrowDown()

            Step(3,"Открытка")
            ArrowDown()

            Step(4,"Табличка")
        }
        Spacer(
            modifier = Modifier
                .weight(1f)
        )
        Image(
            imageVector = ImageVector
                .vectorResource(
                    id = R.drawable.summ
                ),
            contentDescription = "Summ",
        )
        Spacer(
            modifier = Modifier
                .weight(1f)
        )
        Image(
            imageVector = ImageVector
                .vectorResource(
                    id = R.drawable.constructor_bouquet
                ),
            contentDescription = "Bouquet",
        )
    }
}

@Composable
fun Structure(
    flowers: List<ProductWithCountState>,
    viewModel: ConstructorViewModel
) {
    Text(
        text = "Состав",
        style = MaterialTheme.typography.h3.copy(fontSize = 20.sp),
        color = MaterialTheme.colors.onBackground,
        modifier = Modifier
            .padding(top = 16.dp, start = 24.dp)
    )
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .padding(top = 24.dp)
            .heightIn(max = 500.dp),
        horizontalArrangement = Arrangement.spacedBy(65.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 16.dp,start = 24.dp, end = 24.dp)
    ){
        items(flowers) {item ->
            FlowerItem(
                flower = item,
                viewModel = viewModel
            )
        }
        item {
            val scope = rememberCoroutineScope()
            AddButton {
                scope.launch {
                    viewModel.onAddClicked()
                }
            }
        }
    }
}

@Composable
fun FlowerItem(
    flower: ProductWithCountState,
    viewModel: ConstructorViewModel
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
//        Image(
//            painter = painterResource(flower.product.image.toInt()),
//            contentDescription = "",
//            modifier = Modifier
//                .size(60.dp)
//        )
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(if (flower.product is Flower) flower.product.smallImage.getPath()
                else R.drawable.profile_photo_error)
                .build(),
            contentDescription = "Flower photo",
            placeholder = painterResource(id = R.drawable.bouquet_placeholder),
            error = painterResource(id = R.drawable.profile_photo_error),
            onError = {
                Log.d("xd", it.result.throwable.message!!)
            },
            modifier = Modifier
                .size(60.dp)
        )
        Text(
            text = flower.product.name,
            style = MaterialTheme.typography.h3.copy(fontSize = 12.sp),
            color = MaterialTheme.colors.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 4.dp)
                .heightIn(min = 30.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = "${flower.product.price} ₽ / шт",
            style = MaterialTheme.typography.subtitle1.copy(fontSize = 12.sp),
            color = MaterialTheme.colors.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 2.dp)
                .fillMaxWidth()
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(top = 4.dp)
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
                modifier = Modifier
                    .size(20.dp),
                onClick = {
                    viewModel.decreaseFlowerCount(flower)
                }
            ) {
                Image(
                    imageVector = ImageVector
                        .vectorResource(
                            id = R.drawable.buttonbagminus
                        ),
                    contentDescription = ""
                )
            }
            Text(
                text = "${flower.count.value}",
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
                    viewModel.increaseFlowerCount(flower)
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
fun AddButton(onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .noRippleClickable {
                onClick()
            }
    ) {
        Button(
            contentPadding = PaddingValues(0.dp),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.primaryVariant
            ),
            elevation = ButtonDefaults.elevation(
                defaultElevation = 0.dp,
                pressedElevation = 4.dp
            ),
            modifier = Modifier
                .size(40.dp),
            onClick = {
                onClick()
            }
        ) {
            Image(
                imageVector = ImageVector
                    .vectorResource(
                        id = R.drawable.button_add
                    ),
                contentDescription = ""
            )
        }
        Text(
            text = "Добавить",
            style = MaterialTheme.typography.subtitle1.copy(fontSize = 12.sp),
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier
                .padding(top = 4.dp)
        )
    }

}

@Composable
fun BottomSheetContent(
    viewModel: ConstructorViewModel
) {
    val flowersResponse = viewModel.flowers.value
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp)
    ) {
        when (flowersResponse) {
            is Response.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )
            }
            is Response.Error -> {
                Text(
                    text = flowersResponse.message,
                    color = MaterialTheme.colors.onBackground,
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier
                        .padding(top = 12.dp)
                )
            }
            is Response.Success -> {
                Search(
                    search = viewModel.search,
                    onValueChange = {
                        viewModel.onSearchInput(it)
                    },
                    onSearch = {

                    }
                )
                SortAndFilter({

                }, {

                })

                Flowers(
                    products = flowersResponse.data,
                    viewModel = viewModel
                )
            }
        }

    }
}

@Composable
fun Flowers(
    products: List<Flower>,
    viewModel: ConstructorViewModel
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .padding(top = 12.dp),
        contentPadding = PaddingValues(start = 24.dp, end = 24.dp,bottom = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ){
        items(products) {
            FlowerCard(
                product = it,
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun FlowerCard(
    product: Flower,
    viewModel: ConstructorViewModel
) {
    val scope = rememberCoroutineScope()
    Card(
        shape = RoundedCornerShape(10.dp),
        elevation = 2.dp,
        modifier = Modifier
            .noRippleClickable {
                viewModel.addFlower(product)
                scope.launch {
                    viewModel.onDismissSheet()
                }
            }
    ) {
        Column(
            modifier = Modifier
                .padding(all = 6.dp)
        ) {
            BoxWithConstraints {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(product.image.getPath())
                        .build(),
                    contentDescription = product.name,
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
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.h3.copy(fontSize = 14.sp),
                    color = MaterialTheme.colors.onBackground,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .height(36.dp)
                        .padding(end = 8.dp)
                )
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
                            text = "${product.price}",
                            style = MaterialTheme.typography.h3.copy(fontSize = 14.sp),
                            color = MaterialTheme.colors.onBackground
                        )
                    }
                }
            }
        }
    }
}