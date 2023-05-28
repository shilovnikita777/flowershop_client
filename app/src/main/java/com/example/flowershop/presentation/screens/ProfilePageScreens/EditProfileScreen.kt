package com.example.flowershop.presentation.screens.ProfilePageScreens

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flowershop.R
import com.example.flowershop.presentation.screens.common.CustomTextField
import coil.compose.rememberAsyncImagePainter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.flowershop.presentation.navigation.Graph
import com.example.flowershop.data.helpers.Response
import com.example.flowershop.domain.model.Image
import com.example.flowershop.domain.model.User
import com.example.flowershop.presentation.model.UserEditInfo
import java.io.File
import java.io.FileOutputStream

@Composable
fun EditProfileScreen(
    navController: NavHostController
) {
    val profileViewModel = hiltViewModel<ProfileViewModel>()

    val editProfileViewModel = hiltViewModel<EditProfileViewModel>()

    val userMainInfoResponse = profileViewModel.userMainInfoResponse.value

    val username = editProfileViewModel.username.value

    when(userMainInfoResponse) {
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
                text = userMainInfoResponse.message,
                color = MaterialTheme.colors.onError,
                style = MaterialTheme.typography.subtitle1
            )
        }
        is Response.Success -> {
            if (!editProfileViewModel.isDataLoaded) {
                editProfileViewModel.changeUsername(userMainInfoResponse.data.username)
                //editProfileViewModel.changeSelectedImage(userMainInfoResponse.data.image, null)
                editProfileViewModel.isDataLoaded = true
            }
            Column(
                modifier = Modifier
                    .padding(top = 56.dp,start = 24.dp,end = 24.dp)
            ) {
                Text(
                    text =  "Редактирование профиля",
                    style = MaterialTheme.typography.subtitle1.copy(fontSize = 20.sp),
                    color = MaterialTheme.colors.onBackground
                )

                ImageHolder(
                    userImage = userMainInfoResponse.data.image,
                    selectedImage = editProfileViewModel.selectedImage.value,
                    context = LocalContext.current,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    changeSelectedImage = { uri, file ->
                        editProfileViewModel.changeSelectedImage(uri, file)
                    }
                )

                Text(
                    text = "Имя пользователя",
                    style = MaterialTheme.typography.subtitle1,
                    color = MaterialTheme.colors.secondary,
                    modifier = Modifier
                        .padding(top = 24.dp)
                )

                CustomTextField(
                    value = username,
                    placeholder = "Ваше имя",
                    iconId = R.drawable.profile,
                    keyboardType = KeyboardType.Text,
                    onValueChange = {
                        editProfileViewModel.changeUsername(it)
                    }
                )

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp)
                        .height(65.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.primaryVariant
                    ),
                    shape = RoundedCornerShape(56.dp),
                    onClick = {
                        editProfileViewModel.changeUserMainInfo(
                            userData = UserEditInfo(
                                username = editProfileViewModel.username.value,
                                image = editProfileViewModel.selectedImageFile
                            ),
                            onSuccess = {
                                navController.popBackStack()
                                navController.navigate(Graph.PROFILE.route) {
                                    launchSingleTop = true
                                }
                            }
                        )
                    }
                ) {
                    Text(
                        text = "Сохранить",
                        style = MaterialTheme.typography.h3,
                        color = Color.White
                    )
                }

            }
        }
    }

}

@Composable
fun ImageHolder(
    userImage: Image?,
    selectedImage : Uri?,
    context : Context,
    modifier: Modifier,
    changeSelectedImage: (Uri, File?) -> Unit
) {

    val selectImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
        if (it != null) {
            val filesDir = context.filesDir
            val file = File(filesDir, "userphoto.jpeg")
            val inputStream = context.contentResolver.openInputStream(it)
            val outputStream = FileOutputStream(file)
            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            changeSelectedImage(it,file)
        }
    }
    Box(
        modifier = modifier
            .padding(top = 48.dp)
            .size(160.dp)
            .clip(RoundedCornerShape(100.dp))
            .clickable {
                selectImageLauncher.launch("image/*")
            }
    ) {
        if (selectedImage != null) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(selectedImage)
                    .build(),
                contentDescription = "User photo",
                placeholder = painterResource(id = R.drawable.profile_photo_placeholder),
                error = painterResource(id = R.drawable.profile_photo_default),
                onError = {
                    Log.d("xd", it.result.throwable.message!!)
                },
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
            )
        } else {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(userImage?.getPath())
                    .build(),
                contentDescription = "User photo",
                placeholder = painterResource(id = R.drawable.profile_photo_placeholder),
                error = painterResource(id = R.drawable.profile_photo_default),
                onError = {
                    Log.d("xd", it.result.throwable.message!!)
                },
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
            )
        }


    }
}