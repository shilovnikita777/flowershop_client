package com.example.flowershop.screens

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.flowershop.presentation.screens.common.CustomTextField
import com.example.flowershop.R
import com.example.flowershop.domain.model.Category
import com.example.flowershop.presentation.navigation.CatalogNavRoute
import com.example.flowershop.presentation.screens.CatalogPageScreens.CategoriesViewModel
import com.example.flowershop.data.helpers.Response
import com.example.flowershop.util.Constants.NO_CATEGORY_CONSTANT

@Composable
fun CategoriesScreen(navController: NavHostController) {
    var search by remember { mutableStateOf("") }

    val viewModel = hiltViewModel<CategoriesViewModel>()

    val categories = viewModel.categories.value

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .padding(top = 24.dp, start = 24.dp, end = 24.dp)
        ) {
//            CustomTextField(
//                value = search,
//                placeholder = "Поиск",
//                iconId = R.drawable.search,
//                onValueChange = {
//                    search = it
//                }
//            )

            when (categories) {
                is Response.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is Response.Success -> {
                    Categories(categories.data, navController)
                }
                is Response.Error -> {
                    Text(
                        text = categories.message,
                        color = MaterialTheme.colors.onBackground,
                        style = MaterialTheme.typography.h4,
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .height(75.dp)
                    )
                }
            }

            //Categories(names = names, images = images)
            val textAllProducts = "Все товары"
            Button(
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 16.dp)
                    .fillMaxWidth()
                    .height(65.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.primaryVariant
                ),
                shape = RoundedCornerShape(10.dp),
                onClick = {
                    navController.navigate(
                        CatalogNavRoute.Products.passNameAndId(name = textAllProducts, id = NO_CATEGORY_CONSTANT)
                    )
                }
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = textAllProducts,
                        style = MaterialTheme.typography.button,
                        color = MaterialTheme.colors.background,
                    )
                    Image(
                        imageVector = ImageVector
                            .vectorResource(
                                id = R.drawable.arrowright
                            ),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(start = 15.dp)
                    )
                }
            }
        }
    }

}

@Composable
fun CategoryCard(category: Category, navController: NavHostController) {
    Column(
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                navController.navigate(CatalogNavRoute.Products.passNameAndId(
                    name = category.name,
                    id = category.id
                ))
            }
    ) {
//        Image(
//            painter = painterResource(category.image.toInt()),
//            contentDescription = "",
//            modifier = Modifier
//                .clip(RoundedCornerShape(10.dp))
//        )
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(category.image.getPath())
                .build(),
            contentDescription = "Category image",
            placeholder = painterResource(id = R.drawable.bouquet_placeholder),
            error = painterResource(id = R.drawable.bouquet_placeholder),
            onError = {
                Log.d("xd", it.result.throwable.message!!)
            },
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .aspectRatio(1f)
                .clip(RoundedCornerShape(10.dp))
        )
        Text(
            text = category.name,
            style = MaterialTheme.typography.h4.copy(fontSize = 10.sp),
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier
                .padding(top = 6.dp)
                .heightIn(min = 24.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun Categories(categories: List<Category>, navController: NavHostController){
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .padding(top = 24.dp)
            .heightIn(max = 1500.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        userScrollEnabled = false
    ){
        items(categories) {item ->
            CategoryCard(
                category = item,
                navController = navController
            )
        }
    }
}