package com.example.flowershop.presentation.screens

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flowershop.data.TokenManager
import com.example.flowershop.data.UserDatastore
import com.example.flowershop.data.helpers.Response
import com.example.flowershop.data.model.Response.isAuthResponse
import com.example.flowershop.domain.use_cases.AuthenticationUseCases.AuthenticationUseCases
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authenticationUseCases: AuthenticationUseCases,
    private val tokenManager: TokenManager,
    private val userDatastore: UserDatastore
) : ViewModel() {

    private var job : Job? = null
    val isChecking = job != null

    private val _isAuthResponse = mutableStateOf<Response<isAuthResponse>>(Response.Loading)
    val isAuthResponse : State<Response<isAuthResponse>> = _isAuthResponse

    fun isUserAuthenticated() {
        Log.d("xd3","start")
        job = viewModelScope.launch {
            tokenManager.getToken().collect { token ->
                Log.d("xd3","try to login with token : $token")
                authenticationUseCases.isUserAuthenticatedUseCase(token).collect { authResponse ->
                    //Log.d("xd3",authResponse.toString() + "gederedx")
                    _isAuthResponse.value = authResponse
                    Log.d("xd3","auth resp value : $_isAuthResponse")
                    if (authResponse is Response.Success) {
                        userDatastore.saveUserId(authResponse.data.userId)
                    }
                }
            }
        }
    }

    fun unAuth() {
        FirebaseAuth.getInstance().signOut()
        Log.d("xd",FirebaseAuth.getInstance().currentUser?.uid ?: "user empty - 1.")
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