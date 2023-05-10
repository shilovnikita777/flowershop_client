package com.example.flowershop.screens

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.flowershop.R
import com.example.flowershop.presentation.navigation.Graph
import com.example.flowershop.presentation.navigation.ProfileNavRoute
import com.example.flowershop.presentation.screens.ProfilePageScreens.ProfileViewModel
import com.example.flowershop.presentation.screens.common.Separator
import com.example.flowershop.presentation.screens.common.h2
import com.example.flowershop.presentation.screens.common.noRippleClickable
import com.example.flowershop.data.helpers.Response

@Composable
fun ProfileScreen(
    nestedNavController: NavHostController,
    externalNavController: NavHostController
) {
    val viewModel = hiltViewModel<ProfileViewModel>()

    val userMainInfoResponse = viewModel.userMainInfoResponse.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                    top = 56.dp
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                h2(
                    text = "Профиль",
                    color = MaterialTheme.colors.onBackground
                )
                Image(
                    imageVector = ImageVector
                        .vectorResource(
                            id = R.drawable.logout
                        ),
                    contentDescription = "logout",
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            viewModel.onExitClicked()
                        }
                )
            }
            when(userMainInfoResponse) {
                is Response.Loading -> {
                    Image(
                        painter = painterResource(R.drawable.profile_photo_placeholder),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(top = 48.dp)
                            .size(160.dp)
                            .clip(CircleShape)
                            .align(Alignment.CenterHorizontally)
                    )
                }
                is Response.Error -> {
                    Image(
                        painter = painterResource(R.drawable.profile_photo_error),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(top = 48.dp)
                            .size(160.dp)
                            .clip(CircleShape)
                            .align(Alignment.CenterHorizontally)
                    )
                }
                is Response.Success -> {
                    if (userMainInfoResponse.data.image?.isNotEmpty() == true) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(userMainInfoResponse.data.image)
                                .build(),
                            contentDescription = "User photo",
                            placeholder = painterResource(id = R.drawable.profile_photo_placeholder),
                            error = painterResource(id = R.drawable.profile_photo_error),
                            onError = {
                                Log.d("xd", it.result.throwable.message!!)
                            },
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .padding(top = 48.dp)
                                .size(160.dp)
                                .clip(CircleShape)
                                .align(Alignment.CenterHorizontally)
                        )
                    } else {
                        Image(
                            painter = painterResource(R.drawable.profile_photo_default),
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .padding(top = 48.dp)
                                .size(160.dp)
                                .clip(CircleShape)
                                .align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }
        Column(
            modifier = Modifier
                .padding(top = 40.dp)
        ){
            Separator()

            when(userMainInfoResponse) {
                is Response.Loading -> {
                    UserInfoRow(title = "Имя")
                    UserInfoRow(title = "Почта")
                }
                is Response.Error -> {
                    Text(
                        text = userMainInfoResponse.message,
                        color = MaterialTheme.colors.onError,
                        style = MaterialTheme.typography.subtitle1
                    )
                }
                is Response.Success -> {
                    UserInfoRow(title = "Имя", value = userMainInfoResponse.data.username)
                    UserInfoRow(title = "Почта", value = userMainInfoResponse.data.email)
                }
            }


        }
        Column(
            modifier = Modifier
                .padding(top = 12.dp,start = 24.dp,end = 24.dp)
        ){
            when(userMainInfoResponse) {
                is Response.Loading -> {
                    Box(
                        modifier = Modifier
                            .height(20.dp)
                            .width(250.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(MaterialTheme.colors.onSurface)
                    )
                }
                is Response.Error -> {
                    Text(
                        text = userMainInfoResponse.message,
                        color = MaterialTheme.colors.onError,
                        style = MaterialTheme.typography.subtitle1
                    )
                }
                is Response.Success -> {
                    Row(
                        modifier = Modifier
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                nestedNavController.navigate(
                                    ProfileNavRoute.ProfileEdit.route
                                )
                            }
                    ) {
                        Text(
                            text = "Редактировать профиль",
                            style = MaterialTheme.typography.h4.copy(
                                fontSize = 16.sp
                            ),
                            color = MaterialTheme.colors.primaryVariant
                        )
                        Image(
                            imageVector = ImageVector
                                .vectorResource(
                                    id = R.drawable.edit
                                ),
                            contentDescription = "",
                            modifier = Modifier
                                .padding(top = 3.dp,start = 20.dp)

                        )
                    }
                }
            }

//            Row(
//                modifier = Modifier
//                    .padding(top = 20.dp)
//                    .fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                UserInfoCard(R.drawable.car,"История заказов",Color(0xFFA6AFFF))
//                UserInfoCard(R.drawable.heartbig,"Избранное",Color(0xFFFFA3A3))
//                UserInfoCard(R.drawable.promo,"Промокоды",Color(0xFFFFF48F))
//            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .padding(top = 20.dp)
                    .heightIn(max = 200.dp),
                horizontalArrangement = Arrangement.spacedBy(40.dp),
                userScrollEnabled = false
            ){
                item {
                    UserInfoCard(
                        image = R.drawable.car,
                        name = "История заказов",
                        color = Color(0xFFA6AFFF),
                        modifier = Modifier
                            .noRippleClickable {
                                nestedNavController.navigate(ProfileNavRoute.ProfileHistory.route)
                            }
                    )
                }
                item{
                    UserInfoCard(
                        image = R.drawable.heartbig,
                        name = "Избранное",
                        color = Color(0xFFFFA3A3),
                        modifier = Modifier
                            .noRippleClickable {
                                nestedNavController.navigate(ProfileNavRoute.ProfileFavourite.route)
                            }
                    )
                }
                item{
                    UserInfoCard(
                        image = R.drawable.promo,
                        name = "Промокоды",
                        color = Color(0xFFFFF48F),
                        modifier = Modifier
                            .noRippleClickable {
                                nestedNavController.navigate(ProfileNavRoute.ProfilePromocodes.route)
                            }
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .padding(top = 20.dp)
        ){
            Separator()
            InfoRow(title = "О компании") {
                nestedNavController.navigate(route = ProfileNavRoute.ProfileAboutCompany.route)
            }
            InfoRow(title = "Контакты") {
                nestedNavController.navigate(route = ProfileNavRoute.ProfileContacts.route)
            }
            InfoRow(title = "Политика обработки персональных данных") {
                nestedNavController.navigate(route = ProfileNavRoute.ProfilePolicy.route)
            }
            InfoRow(title = "Удалить аккаунт") {
                viewModel.onDeleteClicked()
            }

        }
    }

    if (viewModel.isExitDialogShown) {
        ExitDialog(
            onDismiss = {
                viewModel.onDismissExit()
            },
            onConfirm = {
                viewModel.logout {
                    viewModel.onDismissExit()
                    externalNavController.navigate(route = Graph.AUTHENTICATION.route) {
                        popUpTo(Graph.HOME.route) {
                            inclusive = true
                        }
                    }
                }
            }
        )
    }

    if (viewModel.isDeleteDialogShown) {
        DeleteAccDialog(
            onDismiss = {
                viewModel.onDismissDelete()
            },
            onConfirm = {
                viewModel.deleteAccount {
                    viewModel.onDismissDelete()
                    externalNavController.navigate(route = Graph.AUTHENTICATION.route) {
                        popUpTo(Graph.HOME.route) {
                            inclusive = true
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun UserInfoRow(title: String, value: String = ""){
    Row(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.h4,
            color = MaterialTheme.colors.onSecondary,
            modifier = Modifier
                .weight(1f)
        )
        if (value.isNotEmpty()) {
            Text(
                text = value,
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.onSecondary,
                modifier = Modifier
                    .weight(1f)
            )
        } else {
            Box(
                modifier = Modifier
                    .height(14.dp)
                    .width(50.dp)
                    .weight(1f)
                    .clip(RoundedCornerShape(5.dp))
                    .background(MaterialTheme.colors.onSurface)
            )
        }
    }
    Separator()
}

@Composable
fun InfoRow(title: String, onClick:() -> Unit){
    Row(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .noRippleClickable {
                onClick()
            }
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.h4,
            color = MaterialTheme.colors.onSecondary,
            modifier = Modifier
                .weight(1f)
                .align(alignment = Alignment.CenterVertically)
        )
    }
    Separator()
}

@Composable
fun UserInfoCard(image: Int, name: String,color: Color, modifier : Modifier = Modifier){
//    Column(
//        modifier = Modifier
//            .width(75.dp)
//            .height(110.dp)
//    ){
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(75.dp)
//                .clip(RoundedCornerShape(20.dp))
//                .background(color),
//            contentAlignment = Alignment.Center
//        ){
//            Image(
//                painter = painterResource(imageId),
//                contentDescription = "",
//                modifier = Modifier
//                    .size(50.dp)
//            )
//        }
//        Text(
//            text = text,
//            style = MaterialTheme.typography.subtitle1.copy(
//                fontSize = 12.sp
//            ),
//            color = MaterialTheme.colors.onBackground,
//            textAlign = TextAlign.Center,
//            modifier = Modifier
//                .fillMaxSize()
//                .wrapContentHeight()
//        )
//    }
    Column(
        modifier = modifier
    ) {
        BoxWithConstraints(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(20.dp))
                .background(color)
        ) {
            Image(
                painter = painterResource(image),
                contentDescription = "",
                modifier = Modifier
                    .size(maxWidth / 1.5f)
            )
        }
        Text(
            text = name,
            style = MaterialTheme.typography.subtitle1.copy(fontSize = 12.sp),
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier
                .padding(top = 6.dp)
                .heightIn(min = 30.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ExitDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 24.dp)
            ) {
                Text(
                    text = "Вы уверены что хотите выйти?",
                    style = MaterialTheme.typography.subtitle1,
                    color = MaterialTheme.colors.onBackground
                )
                Image(
                    painter = painterResource(id = R.drawable.exit),
                    contentDescription = "exit",
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .size(60.dp)
                )
                Row(
                    modifier = Modifier
                        .padding(top = 8.dp)
                ) {
                    Button(
                        modifier = Modifier
                            .width(50.dp)
                            .height(25.dp),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.onError
                        ),
                        shape = RoundedCornerShape(6.dp),
                        onClick = {
                            onDismiss()
                        }
                    ) {
                        Text(
                            text = "Нет",
                            style = MaterialTheme.typography.h3.copy(fontSize = 10.sp),
                            color = MaterialTheme.colors.background
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                    )
                    Button(
                        modifier = Modifier
                            .width(50.dp)
                            .height(25.dp),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.primaryVariant
                        ),
                        shape = RoundedCornerShape(6.dp),
                        onClick = {
                            onConfirm()
                        }
                    ) {
                        Text(
                            text = "Да",
                            style = MaterialTheme.typography.h3.copy(fontSize = 10.sp),
                            color = MaterialTheme.colors.background
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DeleteAccDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 24.dp)
            ) {
                Text(
                    text = "Вы уверены что хотите удалить аккаунт без возможности восстановления?",
                    style = MaterialTheme.typography.subtitle1,
                    color = MaterialTheme.colors.onBackground,
                    textAlign = TextAlign.Center
                )
                Image(
                    painter = painterResource(id = R.drawable.exit),
                    contentDescription = "exit",
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .size(60.dp)
                )
                Row(
                    modifier = Modifier
                        .padding(top = 8.dp)
                ) {
                    Button(
                        modifier = Modifier
                            .width(50.dp)
                            .height(25.dp),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.onError
                        ),
                        shape = RoundedCornerShape(6.dp),
                        onClick = {
                            onDismiss()
                        }
                    ) {
                        Text(
                            text = "Нет",
                            style = MaterialTheme.typography.h3.copy(fontSize = 10.sp),
                            color = MaterialTheme.colors.background
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                    )
                    Button(
                        modifier = Modifier
                            .width(50.dp)
                            .height(25.dp),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.primaryVariant
                        ),
                        shape = RoundedCornerShape(6.dp),
                        onClick = {
                            onConfirm()
                        }
                    ) {
                        Text(
                            text = "Да",
                            style = MaterialTheme.typography.h3.copy(fontSize = 10.sp),
                            color = MaterialTheme.colors.background
                        )
                    }
                }
            }
        }
    }
}