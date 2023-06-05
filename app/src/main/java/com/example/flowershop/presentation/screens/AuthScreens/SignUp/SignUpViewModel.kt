package com.example.flowershop.presentation.screens.AuthScreens.SignUp

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.example.flowershop.data.TokenManager
import com.example.flowershop.domain.use_cases.AuthenticationUseCases.AuthenticationUseCases
import com.example.flowershop.util.Constants
import com.example.flowershop.data.helpers.Response
import com.example.flowershop.data.model.Response.RegisterResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authenticationUseCases: AuthenticationUseCases,
    private val tokenManager : TokenManager
): ViewModel() {

    private val _signUpState = mutableStateOf<Response<RegisterResponse>?>(null)
    val signUpState : State<Response<RegisterResponse>?> = _signUpState

    private val _state = mutableStateOf(SignUpStates())
    val state : State<SignUpStates> = _state

    fun onEvent(event: SignUpEvents) {
        when(event) {
            is SignUpEvents.EnterUsername -> {
                _state.value = _state.value.copy(
                    username = _state.value.username.copy(
                        text = event.value,
                        isValid = true,
                        msg = null
                    )
                )
            }
            is SignUpEvents.EnterMail -> {
                _state.value = _state.value.copy(
                    mail = _state.value.mail.copy(
                        text = event.value,
                        isValid = true,
                        msg = null
                    )
                )
            }
            is SignUpEvents.EnterPassword -> {
                _state.value = _state.value.copy(
                    password = _state.value.password.copy(
                        text = event.value,
                        isValid = true,
                        msg = null
                    )
                )
            }
            is SignUpEvents.EnterRepeatedPassword -> {
                _state.value = _state.value.copy(
                    repeatedPassword = _state.value.repeatedPassword.copy(
                        text = event.value,
                        isValid = true,
                        msg = null
                    )
                )
            }
            is SignUpEvents.ChangePasswordVisibility -> {
                _state.value = _state.value.copy(
                    password = _state.value.password.copy(
                        isPasswordHidden = !_state.value.password.isPasswordHidden
                    )
                )
            }
            is SignUpEvents.ChangeRepeatedPasswordVisibility -> {
                _state.value = _state.value.copy(
                    repeatedPassword = _state.value.repeatedPassword.copy(
                        isPasswordHidden = !_state.value.repeatedPassword.isPasswordHidden
                    )
                )
            }
        }
    }

    fun signUp() {
        if (validateData()) {
            viewModelScope.launch {
                authenticationUseCases.signUpUseCase(
                    mail = _state.value.mail.text,
                    username = _state.value.username.text,
                    password = _state.value.password.text
                ).collect {
                    _signUpState.value = it
                }
            }
        }
    }

    private fun validateData() : Boolean {
        var isDataCorrect = true

        if (!isUsernameValid(_state.value.username.text)) {

            isDataCorrect = false
            _state.value = _state.value.copy(
                username = _state.value.username.copy(
                    isValid = false,
                    msg = "Имя пользователя должно быть не менее 1 и не более 20 символов"
                )
            )
        }
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
                    msg = "Пароль должен быть не менее 6 символов"
                )
            )
        }
        if (!isPasswordsEquals(_state.value.password.text, _state.value.repeatedPassword.text)) {

            isDataCorrect = false
            _state.value = _state.value.copy(
                repeatedPassword = _state.value.repeatedPassword.copy(
                    isValid = false,
                    msg = "Пароли не совпадают"
                )
            )
        }

        return isDataCorrect
    }

    fun saveToken(token: String) {
        viewModelScope.launch {
            tokenManager.saveToken(token)
        }
    }

    private fun isUsernameValid(username: String) = username.length <= Constants.USERNAME_MAX_LENGTH && username.isNotEmpty()

    private fun isMailValid(mail: String) = android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches()

    private fun isPasswordValid(password: String) = password.length >= Constants.PASSWORD_MIN_LENGTH

    private fun isPasswordsEquals(password: String, repeatedPassword: String) = (password == repeatedPassword)
}