package com.example.flowershop.presentation.screens.ProfilePageScreens

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flowershop.data.TokenManager
import com.example.flowershop.domain.model.User
import com.example.flowershop.domain.use_cases.UserUseCases.UserUseCases
import com.example.flowershop.data.helpers.Response
import com.example.flowershop.domain.model.Image
import com.example.flowershop.domain.use_cases.AuthenticationUseCases.AuthenticationUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userUseCases: UserUseCases,
    private val authUseCases : AuthenticationUseCases,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _userMainInfoResponse = mutableStateOf<Response<User.Data>>(Response.Loading)
    val userMainInfoResponse: State<Response<User.Data>> = _userMainInfoResponse

    private val _logoutResponse = mutableStateOf<Response<Boolean>?>(null)
    val logoutResponse: State<Response<Boolean>?> = _logoutResponse

    private val _deleteAccResponse = mutableStateOf<Response<Boolean>?>(null)
    val deleteAccResponse: State<Response<Boolean>?> = _deleteAccResponse

    var isExitDialogShown by mutableStateOf(false)
        private set

    var isDeleteDialogShown by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            getUserMainInfo()
        }
    }

    private fun getUserMainInfo() {
        viewModelScope.launch {
            userUseCases.getUserMainInfoUseCase().map {
                when (it) {
                    is Response.Loading -> {
                        it
                    }
                    is Response.Error -> {
                        it
                    }
                    is Response.Success -> {
                        Response.Success(
                            User.Data(
                                username = it.data.username,
                                email = it.data.email,
                                image = if (it.data.image != null) Image(it.data.image) else null
                            )
                        )
                    }
                }
            }.collect {
                _userMainInfoResponse.value = it
            }
        }
    }

    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            tokenManager.getToken().collect {
                authUseCases.logoutUseCase(it!!).collect {
                    _logoutResponse.value = it
                    if (it is Response.Success && it.data) {
                        onSuccess()
                    }
                }
            }
        }
    }

    fun deleteAccount(onSuccess: () -> Unit) {
        viewModelScope.launch {
            userUseCases.deleteAccountUseCase().collect {
                _deleteAccResponse.value = it
                if (it is Response.Success && it.data) {
                    tokenManager.deleteToken()
                    onSuccess()
                }
            }
        }
    }

    fun onExitClicked() {
        isExitDialogShown = true
    }

    fun onDeleteClicked() {
        isDeleteDialogShown = true
    }

    fun onDismissExit() {
        isExitDialogShown = false
    }

    fun onDismissDelete() {
        isDeleteDialogShown = false
    }
}