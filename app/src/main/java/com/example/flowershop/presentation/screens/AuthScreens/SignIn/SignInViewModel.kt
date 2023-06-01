package com.example.flowershop.presentation.screens.AuthScreens.SignIn

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flowershop.data.TokenManager
import com.example.flowershop.domain.use_cases.AuthenticationUseCases.AuthenticationUseCases
import com.example.flowershop.data.helpers.Response
import com.example.flowershop.data.model.Response.LoginResponse
import com.example.flowershop.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authenticationUseCases : AuthenticationUseCases,
    private val tokenManager : TokenManager
) : ViewModel() {

    private val _signInState = mutableStateOf<Response<LoginResponse>?>(null)
    val signInState : State<Response<LoginResponse>?> = _signInState

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
        if (validateData()) {
            viewModelScope.launch {
                authenticationUseCases.signInUseCase(
                    mail = _state.value.mail.text,
                    password = _state.value.password.text
                ).collect {
                    _signInState.value = it
                }
            }
        }
    }

    private fun validateData() : Boolean {
        var isDataCorrect = true
        if (!isMailValid(_state.value.mail.text)) {

            isDataCorrect = false
            _state.value = _state.value.copy(
                mail = _state.value.mail.copy(
                    isValid = false,
                    msg = "Пожалуйста, введите почту согласно шаблону в поле"
                )
            )
        }
        if (!isPasswordValid(_state.value.password.text)) {

            isDataCorrect = false
            _state.value = _state.value.copy(
                password = _state.value.password.copy(
                    isValid = false,
                    msg = "Пароль не может быть менее ${Constants.PASSWORD_MIN_LENGTH} символов"
                )
            )
        }

        return isDataCorrect
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

    private fun isMailValid(mail: String) = android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches()

    private fun isPasswordValid(password: String) = password.length >= Constants.PASSWORD_MIN_LENGTH
}