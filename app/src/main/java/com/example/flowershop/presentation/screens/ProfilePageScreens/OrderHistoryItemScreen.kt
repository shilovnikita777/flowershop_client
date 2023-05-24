package com.example.flowershop.presentation.screens.ProfilePageScreens

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.flowershop.R
import com.example.flowershop.data.helpers.Response
import com.example.flowershop.domain.model.Bouquet
import com.example.flowershop.domain.model.FlowersWithDecoration
import com.example.flowershop.domain.model.ProductInBag
import com.example.flowershop.domain.model.Promocode
import com.example.flowershop.presentation.navigation.CatalogNavRoute
import com.example.flowershop.presentation.navigation.MainNavRoute
import com.example.flowershop.presentation.screens.BagScreens.BagViewModel
import com.example.flowershop.presentation.screens.BagScreens.OrderInfo.OrderInfoEvents
import com.example.flowershop.presentation.screens.UserProductViewModel
import com.example.flowershop.presentation.screens.common.Separator
import com.example.flowershop.presentation.screens.common.h2
import com.example.flowershop.presentation.screens.common.noRippleClickable
import com.example.flowershop.util.Constants
import java.time.LocalDate

@Composable
fun OrderHistoryItemScreen(orderId : Int) {
    val viewModel = hiltViewModel<OrderHistoryItemViewModel>()

    val orderResponse = viewModel.order.value

    when (orderResponse) {
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
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = orderResponse.message,
                    color = MaterialTheme.colors.onError,
                    style = MaterialTheme.typography.h3
                )
            }
        }
        is Response.Success -> {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(top = 24.dp)
            ) {
                h2(
                    text = "Информация о заказе",
                    color = MaterialTheme.colors.onBackground,
                    modifier = Modifier
                        .padding(bottom = 16.dp, start = 24.dp)
                )
                Date(orderResponse.data.date)
                Separator(
                    modifier = Modifier
                        .padding(top = 16.dp)
                )

                Address(orderResponse.data.address)
                Separator(
                    modifier = Modifier
                        .padding(top = 16.dp)
                )

                Receiver(orderResponse.data.fullname)
                Separator(
                    modifier = Modifier
                        .padding(top = 16.dp)
                )

                if (orderResponse.data.promocode != null) {
                    Promocode(
                        promocode = orderResponse.data.promocode
                    )
                    Separator(
                        modifier = Modifier
                            .padding(top = 16.dp)
                    )
                }

                Composition(orderResponse.data.products ?: emptyList())
                Separator(
                    modifier = Modifier
                        .padding(top = 8.dp)
                )

                ContactUs()
            }
        }
    }

}

@Composable
fun Date(date: LocalDate) {
    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
    ) {
        Text(
            text = "Дата",
            style = MaterialTheme.typography.h3.copy(
                fontSize = 20.sp
            ),
            color = MaterialTheme.colors.onBackground
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(top = 12.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.calendar_color),
                contentDescription = "calendar",
                modifier = Modifier
                    .size(32.dp)
                    .padding(end = 8.dp)
            )
            Text(
                text = date.toString(),
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.subtitle1.copy(
                    fontSize = 16.sp
                )
            )
        }
    }
}

@Composable
fun Address(address: String) {
    Column(
        modifier = Modifier
            .padding(top = 16.dp, start = 24.dp, end = 24.dp)
    ) {
        Text(
            text = "Адрес доставки",
            style = MaterialTheme.typography.h3.copy(
                fontSize = 20.sp
            ),
            color = MaterialTheme.colors.onBackground
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(top = 12.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.location_color),
                contentDescription = "location",
                modifier = Modifier
                    .size(32.dp)
                    .padding(end = 8.dp)
            )
            Text(
                text = address,
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.subtitle1.copy(
                    fontSize = 16.sp
                )
            )
        }
    }
}

@Composable
fun Receiver(fullname: String) {
    Column(
        modifier = Modifier
            .padding(top = 16.dp, start = 24.dp, end = 24.dp)
    ) {
        Text(
            text = "Получатель",
            style = MaterialTheme.typography.h3.copy(
                fontSize = 20.sp
            ),
            color = MaterialTheme.colors.onBackground
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(top = 12.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.person_color),
                contentDescription = "user",
                modifier = Modifier
                    .size(32.dp)
                    .padding(end = 8.dp)
            )
            Text(
                text = fullname,
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.subtitle1.copy(
                    fontSize = 16.sp
                )
            )
        }
    }
}

@Composable
fun Promocode(promocode: Promocode) {
    Column(
        modifier = Modifier
            .padding(top = 16.dp, start = 24.dp, end = 24.dp)
    ) {
        Text(
            text = "Промокод",
            style = MaterialTheme.typography.h3.copy(
                fontSize = 20.sp
            ),
            color = MaterialTheme.colors.onBackground
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(top = 12.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.discount_color),
                contentDescription = "location",
                modifier = Modifier
                    .size(32.dp)
                    .padding(end = 8.dp)
            )
            Text(
                text = "Промокод ${promocode.value} на ${promocode.amount}%",
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.subtitle1.copy(
                    fontSize = 16.sp
                )
            )
        }
    }
}

@Composable
fun Composition(products: List<ProductInBag>) {
    Text(
        text = "Состав",
        style = MaterialTheme.typography.h3.copy(
            fontSize = 20.sp
        ),
        color = MaterialTheme.colors.onBackground,
        modifier = Modifier
            .padding(top = 16.dp, start = 24.dp, end = 24.dp)
    )
    Column(
        modifier = Modifier
            .padding(top = 16.dp, start = 24.dp, end = 24.dp)
    ) {
        products.forEach {
            OrderProductCard(productInBag = it)
            Spacer(
                modifier = Modifier
                    .padding(bottom = 8.dp)
            )
        }
    }
//    LazyColumn(
//        contentPadding = PaddingValues(top = 16.dp,start = 24.dp, end = 24.dp, bottom = 8.dp),
//        verticalArrangement = Arrangement.spacedBy(12.dp)
//    ) {
//        items(products) {
//            OrderProductCard(productInBag = it)
//        }
//    }
}

@Composable
fun OrderProductCard(
    productInBag: ProductInBag
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        elevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(start = 10.dp, end = 14.dp, top = 10.dp, bottom = 10.dp)
        ) {
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
                    .size(90.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
            Column(
                modifier = Modifier
                    .padding(start = 10.dp)
            ) {
                Row {
                    Text(
                        text = productInBag.productWithCount.product.name,
                        style = MaterialTheme.typography.h3.copy(fontSize = 14.sp),
                        color = MaterialTheme.colors.onBackground,
                        modifier = Modifier
                            .padding(top = 4.dp, end = 12.dp)
                            .weight(1f)
                    )
                }
                Row(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    if (productInBag.productWithCount is FlowersWithDecoration) {
                        Text(
                            text = (productInBag.productWithCount as FlowersWithDecoration).decoration.title,
                            color = MaterialTheme.colors.secondary,
                            style = MaterialTheme.typography.subtitle1.copy(fontSize = 10.sp),
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    } else if (productInBag.productWithCount.product is Bouquet) {
                        val decorText =
                            (productInBag.productWithCount.product as Bouquet).decoration.title
                        val postcardText =
                            if ((productInBag.productWithCount.product as Bouquet).postcard.isNotEmpty())
                                "Открытка"
                            else
                                "Без открытки"
                        val tableText =
                            (productInBag.productWithCount.product as Bouquet).table.text
                        Text(
                            text = "$decorText • $postcardText • $tableText",
                            color = MaterialTheme.colors.secondary,
                            style = MaterialTheme.typography.subtitle1.copy(fontSize = 10.sp),
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
                Row {
                    Text(
                        text = "Кол-во: ${productInBag.productWithCount.count}",
                        style = MaterialTheme.typography.h4,
                        color = MaterialTheme.colors.onBackground,
                        modifier = Modifier
                    )
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
                            text = "${productInBag.totalPrice}",
                            style = MaterialTheme.typography.h3,
                            color = MaterialTheme.colors.onBackground
                        )
                    }
                }
            }
        }

    }
}

@Composable
fun ContactUs() {
    val context = LocalContext.current

    val vkIntent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.VK_URL))
    val tgIntent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.TELEGRAM_URL))
    val email = Constants.GMAIL_ADDRESS
    val subject = "Мобильное приложение магазина цветов"
    val gmailIntent = Intent(Intent.ACTION_SEND).apply {
        data = "mailto:$email?subject=${subject}".toUri()
    }

    Column(
        modifier = Modifier
            .padding(top = 16.dp, start = 24.dp, end = 24.dp)
    ) {
        Text(
            text = "Возникли проблемы с заказом?\n" +
                    "Напишите нам!",
            color = MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.body1
        )

        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 12.dp)
        ) {
            Image(
                painterResource(id = R.drawable.vk),
                contentDescription = "vk",
                modifier = Modifier
                    .size(36.dp)
                    .noRippleClickable {
                        context.startActivity(vkIntent)
                    }
            )

            Image(
                painterResource(id = R.drawable.telegram),
                contentDescription = "tg",
                modifier = Modifier
                    .size(36.dp)
                    .noRippleClickable {
                        context.startActivity(tgIntent)
                    }
            )

            Image(
                painterResource(id = R.drawable.gmail),
                contentDescription = "gmail",
                modifier = Modifier
                    .size(36.dp)
                    .noRippleClickable {
                        gmailIntent
                            .resolveActivityInfo(context.packageManager, 0)
                            ?.let {
                                context.startActivity(gmailIntent)
                            }
                    }
            )
        }
    }
}