package com.example.flowershop.presentation.screens.CatalogPageScreens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.flowershop.R
import com.example.flowershop.domain.model.Product
import com.example.flowershop.presentation.screens.MainPageScreens.ProductSmallCard
import com.example.flowershop.presentation.screens.UserProductViewModel
import com.example.flowershop.data.helpers.Response
import com.example.flowershop.presentation.screens.MainPageScreens.SortAndFilterViewModel
import com.example.flowershop.presentation.screens.common.noRippleClickable
import com.example.flowershop.util.Constants

@Composable
fun ProductsScreen(
    navController: NavHostController,
    categoryName: String,
    categoryId: Int
) {
    val viewModel = hiltViewModel<ProductsViewModel>()
    val userProductViewModel = hiltViewModel<UserProductViewModel>()

    val products = viewModel.currentProducts.value

    val job = viewModel.searchJob

    Column(
        modifier = Modifier
        .padding(top = 24.dp)
    ) {

        ProductsCategoryHeader(categoryName)

        Search(
            search = viewModel.searchConditions.value.search ?: "",
            onValueChange = {
                viewModel.onSearchInput(it)
                job.value?.cancel()
                job.value = viewModel.performSearchWithDelay()
            },
            onSearch = {
                job.value?.cancel()
                job.value = viewModel.getProducts()
            }
        )

        when(products) {
            is Response.Loading -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(top = 24.dp)
                    )
                }
            }
            is Response.Error -> {
                Text(
                    text = products.message,
                    color = MaterialTheme.colors.onBackground,
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier
                        .padding(top = 12.dp)
                )
            }
            is Response.Success -> {
                SortAndFilter(
                    onSortClicked = {
                        viewModel.onSortClicked()
                    },
                    onFilterClicked = {
                        viewModel.onFilterClicked()
                    }
                )
                if (products.data.isEmpty()) {
                    Text(
                        text = "Продуктов, подходящих под критерии не найдено",
                        style = MaterialTheme.typography.subtitle1.copy(
                            fontSize = 20.sp
                        ),
                        color = MaterialTheme.colors.onBackground,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(horizontal = 24.dp, vertical = 16.dp)
                    )
                } else {
                    Products(
                        products = products.data,
                        navController = navController,
                        viewModel = userProductViewModel,
                        isProductsLoaded = viewModel.isProductsLoaded,
                        changeProductsLoaded = {
                            viewModel.changeProductsLoaded(it)
                        }
                    )
                }
            }
        }
        if (viewModel.isFilterDialogShown) {
            FilterDialog(
                viewModel = viewModel,
                getProducts = {
                    job.value?.cancel()
                    job.value = viewModel.getProducts()
                },
                areBouquetsAvailable = true
            )
        }

        if (viewModel.isSortDialogShown) {
            SortDialog(
                viewModel = viewModel,
                getProducts = {
                    job.value?.cancel()
                    job.value = viewModel.getProducts()
                }
            )
        }
    }
}

@Composable
fun Search(search: String,onValueChange: (String) -> Unit,onSearch:() -> Unit){
//    CustomTextField(
//        value = search,
//        placeholder = "Поиск",
//        iconId = R.drawable.search,
//        onValueChange = onValueChange,
//        modifier = Modifier
//            .padding(start = 24.dp, end = 24.dp)
//    )
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = search,
        onValueChange = {
            onValueChange(it)
        },
        shape = RoundedCornerShape(15.dp),
        leadingIcon = {
            Image(
                imageVector = ImageVector
                    .vectorResource(
                        id = R.drawable.search
                    ),
                contentDescription = "",
                modifier = Modifier.padding(start = 19.dp, end = 15.dp)
            )
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch()
                focusManager.clearFocus()
            }
        ),
        singleLine = true,
        textStyle = MaterialTheme.typography.subtitle1
            .copy(
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
                text = "Поиск",
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.secondary
            )
        },
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth()
            .height(65.dp)
    )
}

@Composable
fun SortAndFilter(
    onSortClicked: () -> Unit,
    onFilterClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(top = 12.dp,start = 24.dp,end = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .noRippleClickable {
                    onSortClicked()
                }
        ) {
            Text(
                text = "Сортировка",
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.primaryVariant
            )
            Image(
                painter = painterResource(
                    id = R.drawable.sort
                ),
                contentDescription = "sort",
                modifier = Modifier
                    .padding(start = 8.dp)
            )
        }
        Spacer(
            modifier = Modifier
                .weight(1f)
        )
        Row(
            modifier = Modifier
                .noRippleClickable {
                    onFilterClicked()
                }
        ) {
            Text(
                text = "Фильтрация",
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.primaryVariant
            )
            Image(
                painter = painterResource(
                    id = R.drawable.filter
                ),
                contentDescription = "filter",
                modifier = Modifier
                    .padding(start = 8.dp)
            )
        }
    }
}

@Composable
fun Products(
    products: List<Triple<Product, MutableState<Response<Boolean>>, MutableState<Response<Boolean>>>>,
    navController: NavHostController,
    viewModel: UserProductViewModel,
    isProductsLoaded : Boolean,
    changeProductsLoaded : (Boolean) -> Unit
) {
    LaunchedEffect(key1 = Unit) {
        products.forEach { product->
            viewModel.isProductInBag(product.first) {
                product.second.value = it
            }
            viewModel.isProductInFavourite(product.first) {
                product.third.value = it
            }
        }
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .padding(top = 12.dp),
        contentPadding = PaddingValues(start = 24.dp, end = 24.dp,bottom = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ){
//        if (!isProductsLoaded) {
//            products.forEach { product ->
//                viewModel.isProductInBag(product.first) {
//                    product.second.value = it
//                }
//                viewModel.isProductInFavourite(product.first) {
//                    product.third.value = it
//                }
//            }
//            changeProductsLoaded(true)
//        }
        items(products) {
            ProductSmallCard(
                product = it,
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun ProductsCategoryHeader(categoryName: String) {
    Text(
        text = categoryName,
        style = MaterialTheme.typography.h2,
        color = MaterialTheme.colors.onBackground,
        modifier = Modifier
            .padding(start = 24.dp,end = 24.dp, bottom = 12.dp)
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FilterDialog(
    viewModel : SortAndFilterViewModel,
    getProducts:() -> Unit,
    areBouquetsAvailable : Boolean
) {
    Dialog(
        onDismissRequest = {
            viewModel.onFilterDismiss()
            if (viewModel.haveConditionsChanged()) {
                getProducts()
            }
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = true
        )
    ) {
        Card(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth(0.95f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 24.dp)
            ) {
                Text(
                    text = "Цена",
                    style = MaterialTheme.typography.h4.copy(
                        fontSize = 16.sp
                    ),
                    color = MaterialTheme.colors.onBackground
                )
                Row(
                    modifier = Modifier
                        .padding(top = 8.dp)
                ) {
                    OutlinedTextField(
                        value = viewModel.searchConditions.value.minPrice?.toString() ?: "",
                        onValueChange = {
                            viewModel.changeMinPrice(viewModel.changePrice(it))
                        },
                        shape = RoundedCornerShape(15.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal
                        ),
                        keyboardActions = KeyboardActions(

                        ),
                        singleLine = true,
                        textStyle = MaterialTheme.typography.subtitle1
                            .copy(
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
                                text = "От",
                                style = MaterialTheme.typography.subtitle1,
                                color = MaterialTheme.colors.secondary,
                                modifier = Modifier
                                    .fillMaxWidth(),
                            )
                        },
                        modifier = Modifier
                            .width(100.dp)
                            .height(50.dp)
                    )
                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                    )
                    OutlinedTextField(
                        value = viewModel.searchConditions.value.maxPrice?.toString() ?: "",
                        onValueChange = {
                            viewModel.changeMaxPrice(viewModel.changePrice(it))
                        },
                        shape = RoundedCornerShape(15.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal
                        ),
                        keyboardActions = KeyboardActions(

                        ),
                        singleLine = true,
                        textStyle = MaterialTheme.typography.subtitle1
                            .copy(
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
                                text = "До",
                                style = MaterialTheme.typography.subtitle1,
                                color = MaterialTheme.colors.secondary,
                                modifier = Modifier
                                    .fillMaxWidth(),
                            )
                        },
                        modifier = Modifier
                            .width(100.dp)
                            .height(50.dp)
                    )
                }
                Text(
                    text = "Товары, включающие",
                    style = MaterialTheme.typography.h4.copy(
                        fontSize = 16.sp
                    ),
                    color = MaterialTheme.colors.onBackground,
                    modifier = Modifier
                        .padding(top = 12.dp)
                )
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            end = 8.dp,
                            top = 8.dp
                        )
                ) {
                    items(viewModel.sorts.value.size) { index ->
                        Row(
                            modifier = Modifier
                                .noRippleClickable {
                                    viewModel.updateSortsList(
                                        viewModel.sorts.value.mapIndexed{ j, item ->
                                            if (index == j) {
                                                item.copy(isSelected = !item.isSelected)
                                            } else item
                                        }
                                    )
                                }
                        ) {
                            Log.d("xd111", viewModel.sorts.value[index].title + " " + viewModel.sorts.value[index].isSelected)
                            var icon = R.drawable.check_false
                            if (viewModel.sorts.value[index].isSelected)
                                icon = R.drawable.check_true
                            Image(
                                imageVector = ImageVector.vectorResource(id = icon),
                                contentDescription = "icon",
                                modifier = Modifier
                                    .size(20.dp)
                            )
                            Text(
                                text = viewModel.sorts.value[index].title,
                                style = MaterialTheme.typography.h4,
                                color = MaterialTheme.colors.onBackground,
                                modifier = Modifier
                                    .padding(start = 12.dp)
                                    .align(Alignment.CenterVertically)
                            )
                        }
                    }
                }
                if (areBouquetsAvailable) {
                    Text(
                        text = "Размер букета",
                        style = MaterialTheme.typography.h4.copy(
                            fontSize = 16.sp
                        ),
                        color = MaterialTheme.colors.onBackground,
                        modifier = Modifier
                            .padding(top = 12.dp)
                    )
                    Column(
                        modifier = Modifier
                            .padding(
                                start = 16.dp,
                                end = 8.dp
                            )
                            .selectableGroup()
                    ) {
                        viewModel.sizes.forEach {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .padding(top = 8.dp)
                                    .selectable(
                                        selected = viewModel.isSizeChosen(it),
                                        onClick = {
                                            viewModel.changeSize(it)
                                        },
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null,
                                        role = Role.RadioButton
                                    )
                            ) {
                                RadioButton(
                                    selected = viewModel.isSizeChosen(it),
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = MaterialTheme.colors.onBackground,
                                        unselectedColor = Color.DarkGray,
                                        disabledColor = Color.LightGray
                                    ),
                                    onClick = null
                                )
                                Text(
                                    text = it,
                                    style = MaterialTheme.typography.h4,
                                    color = MaterialTheme.colors.onBackground,
                                    modifier = Modifier
                                        .padding(start = 12.dp)
                                )
                            }
                        }
                    }
                }
                Button(
                    contentPadding = PaddingValues(8.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.onSecondary
                    ),
                    elevation = ButtonDefaults.elevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 4.dp
                    ),
                    //border = BorderStroke(1.dp,MaterialTheme.colors.onBackground),
                    modifier = Modifier
                        .padding(top = 16.dp),
                    onClick = {
                        viewModel.clearFilters()
                    }
                ) {
                    Text(
                        text = "Сбросить фильтры",
                        style = MaterialTheme.typography.subtitle1,
                        color = MaterialTheme.colors.background
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SortDialog(
    viewModel : SortAndFilterViewModel,
    getProducts:() -> Unit
) {
    Dialog(
        onDismissRequest = {
            viewModel.onSortDismiss()
            if (viewModel.haveConditionsChanged()) {
                getProducts()
            }
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = true
        )
    ) {
        Card(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth(0.95f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 24.dp)
            ) {
                Text(
                    text = "Сортировать по",
                    style = MaterialTheme.typography.h4.copy(
                        fontSize = 16.sp
                    ),
                    color = MaterialTheme.colors.onBackground
                )
                Column(
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            end = 8.dp,
                            top = 8.dp
                        )
                        .selectableGroup()
                ) {
                    viewModel.sortCriteria.forEach {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .selectable(
                                    selected = viewModel.isSortCriteriaChosen(it),
                                    onClick = {
                                        viewModel.changeSortCriteria(it)
                                    },
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    role = Role.RadioButton
                                )
                        ) {
                            RadioButton(
                                selected = viewModel.isSortCriteriaChosen(it),
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = MaterialTheme.colors.onBackground,
                                    unselectedColor = Color.DarkGray,
                                    disabledColor = Color.LightGray
                                ),
                                onClick = null
                            )
                            Text(
                                text = it,
                                style = MaterialTheme.typography.h4,
                                color = MaterialTheme.colors.onBackground,
                                modifier = Modifier
                                    .padding(start = 12.dp)
                            )
                        }
                    }
                }
                Button(
                    contentPadding = PaddingValues(8.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.onSecondary
                    ),
                    elevation = ButtonDefaults.elevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 4.dp
                    ),
                    //border = BorderStroke(1.dp,MaterialTheme.colors.onBackground),
                    modifier = Modifier
                        .padding(top = 16.dp),
                    onClick = {
                        viewModel.clearSortCriteria()
                    }
                ) {
                    Text(
                        text = "Сбросить сортировку",
                        style = MaterialTheme.typography.subtitle1,
                        color = MaterialTheme.colors.background
                    )
                }
            }
        }
    }
}


