package com.example.flowershop.presentation.screens.AuthScreens.SignIn

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flowershop.data.TokenManager
import com.example.flowershop.data.UserDatastore
import com.example.flowershop.domain.use_cases.AuthenticationUseCases.AuthenticationUseCases
import com.example.flowershop.data.helpers.Response
import com.example.flowershop.data.model.Response.LoginResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authenticationUseCases : AuthenticationUseCases,
    private val tokenManager : TokenManager,
    private val userDatastore : UserDatastore
) : ViewModel() {

    private val _signInState = mutableStateOf<Response<LoginResponse?>>(Response.Success(null))
    val signInState : State<Response<LoginResponse?>> = _signInState

    private val _state = mutableStateOf(SignInStates())
    val state : State<SignInStates> = _state

    fun onEvent(event: SignInEvents) {
        when(event) {
            is SignInEvents.EnterMail -> {
                _state.value = _state.value.copy(
                    mail = _state.value.mail.copy(
                        text = event.value,
                        isValid = true,
                        msg = null
                    )
                )
            }
            is SignInEvents.EnterPassword -> {
                _state.value = _state.value.copy(
                    password = _state.value.password.copy(
                        text = event.value,
                        isValid = true,
                        msg = null
                    )
                )
            }
            is SignInEvents.ChangePasswordVisibility -> {
                _state.value = _state.value.copy(
                    password = _state.value.password.copy(
                        isPasswordHidden = !_state.value.password.isPasswordHidden
                    )
                )
            }
        }
    }

    fun signIn() {
        viewModelScope.launch {
            authenticationUseCases.signInUseCase(
                mail = _state.value.mail.text,
                password = _state.value.password.text
            ).collect {
                _signInState.value = it
            }
        }
    }

//    fun saveUserId(id: Int){
//        viewModelScope.launch {
//            userDatastore.saveUserId(id)
//        }
//    }

    fun saveToken(token: String) {
        Log.d("xd3,","token to save : $token")
        viewModelScope.launch {
            tokenManager.saveToken(token)
        }
    }
}