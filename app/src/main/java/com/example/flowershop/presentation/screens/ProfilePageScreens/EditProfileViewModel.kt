package com.example.flowershop.presentation.screens.ProfilePageScreens

import android.net.Uri
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flowershop.domain.use_cases.UserUseCases.UserUseCases
import com.example.flowershop.data.helpers.Response
import com.example.flowershop.presentation.model.UserEditInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val userUseCases: UserUseCases
) : ViewModel() {

    private val _selectedImage = mutableStateOf<Uri?>(null)
    val selectedImage : State<Uri?> = _selectedImage

    var selectedImageFile : File? = null
    private set

    var isDataLoaded = false

    private val _changeMainInfoResponse = mutableStateOf<Response<Boolean>?>(null)
    val changeMainInfoResponse : State<Response<Boolean>?> = _changeMainInfoResponse

    private val _username = mutableStateOf("")
    val username : State<String> = _username

    fun changeUserMainInfo(userData: UserEditInfo, onSuccess: () -> Unit) {
        viewModelScope.launch {
            userUseCases.changeUserMainInfoUseCase(
                userData = userData
            ).collect {
                _changeMainInfoResponse.value = it
                if (it is Response.Success) {
                    onSuccess()
                }
            }
        }
    }

    fun changeUsername(username : String) {
        _username.value = username
    }

    fun changeSelectedImage(image : Uri?, imageFile : File?) {
        _selectedImage.value = image
        selectedImageFile = imageFile
    }
}