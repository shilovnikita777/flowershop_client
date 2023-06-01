package com.example.flowershop.presentation.screens

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flowershop.data.TokenManager
import com.example.flowershop.data.helpers.Response
import com.example.flowershop.data.model.Response.isAuthResponse
import com.example.flowershop.domain.use_cases.AuthenticationUseCases.AuthenticationUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authenticationUseCases: AuthenticationUseCases,
    private val tokenManager: TokenManager,
) : ViewModel() {

    private var job : Job? = null
    val isChecking = job != null

    private val _isAuthResponse = mutableStateOf<Response<isAuthResponse>>(Response.Loading)
    val isAuthResponse : State<Response<isAuthResponse>> = _isAuthResponse

    fun isUserAuthenticated() {
        job = viewModelScope.launch {
            tokenManager.getToken().collect { token ->
                if (token != null) {
                    authenticationUseCases.isUserAuthenticatedUseCase(token).collect { authResponse ->
                        _isAuthResponse.value = authResponse
                    }
                } else {
                    _isAuthResponse.value = Response.Success(isAuthResponse(
                        isAuth = false
                    ))
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job?.let {
            if (it.isActive) {
                it.cancel()
            }
        }
    }
}