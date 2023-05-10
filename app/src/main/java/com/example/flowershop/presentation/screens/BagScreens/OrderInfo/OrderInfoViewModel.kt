package com.example.flowershop.presentation.screens.BagScreens.OrderInfo

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flowershop.data.TokenManager
import com.example.flowershop.data.UserDatastore
import com.example.flowershop.data.helpers.Response
import com.example.flowershop.data.model.Response.RegisterResponse
import com.example.flowershop.domain.model.Bouquet
import com.example.flowershop.domain.model.Flower
import com.example.flowershop.domain.model.User
import com.example.flowershop.domain.use_cases.AuthenticationUseCases.AuthenticationUseCases
import com.example.flowershop.domain.use_cases.UserUseCases.UserUseCases
import com.example.flowershop.presentation.screens.AuthScreens.SignUp.SignUpEvents
import com.example.flowershop.presentation.screens.AuthScreens.SignUp.SignUpStates
import com.example.flowershop.util.Constants
import com.example.flowershop.util.Constants.PHONE_LENGTH
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class OrderInfoViewModel @Inject constructor(
    private val userUseCases: UserUseCases,
    private val tokenManager : TokenManager,
    private val userDatastore : UserDatastore
): ViewModel() {

    private val _userBagResponse = mutableStateOf<Response<User.Bag>>(Response.Loading)
    val userBagResponse : State<Response<User.Bag>> = _userBagResponse

    private val _makeOrderResponse = mutableStateOf<Response<Boolean>?>(null)
    val makeOrderResponse : State<Response<Boolean>?> = _makeOrderResponse

    private val _state = mutableStateOf(OrderInfoStates())
    val state : State<OrderInfoStates> = _state

    private var userId = Constants.NO_USER_CONSTANT

    init {
        viewModelScope.launch {
            userDatastore.getUserId.collect {
                userId = it
                getBagByUserId()
            }
        }
    }

    private fun getBagByUserId() {
        if (userId != Constants.NO_USER_CONSTANT) {
            viewModelScope.launch {
                userUseCases.getBagByUserIdUseCase(userId).collect { bagResponse ->
                    _userBagResponse.value = bagResponse
                }
            }
        } else {
            _userBagResponse.value = Response.Error("Ошибка при идентификации пользователя")
        }
    }

    fun makeOrder() {
        viewModelScope.launch {
            val order = User.Order(
                products = (userBagResponse.value as Response.Success).data.products,
                date = LocalDate.now(),
                phone = _state.value.phone.text,
                address = _state.value.address.text,
                fullname = _state.value.fullname.text
            )
            userUseCases.makeOrderUseCase(order).collect {
                _makeOrderResponse.value = it
            }
        }
    }

    fun onEvent(event: OrderInfoEvents) {
        when(event) {
            is OrderInfoEvents.EnterPhone -> {
                val phone = filterPhone(event.value)
                _state.value = _state.value.copy(
                    phone = _state.value.phone.copy(
                        text = phone,
                        isValid = true,
                        msg = null
                    )
                )
            }
            is OrderInfoEvents.EnterAddress -> {
                _state.value = _state.value.copy(
                    address = _state.value.address.copy(
                        text = event.value,
                        isValid = true,
                        msg = null
                    )
                )
            }
            is OrderInfoEvents.EnterFN -> {
                _state.value = _state.value.copy(
                    fullname = _state.value.fullname.copy(
                        text = event.value,
                        isValid = true,
                        msg = null
                    )
                )
            }
        }
    }

    fun pay(onSuccess:() -> Unit) {
        if (validateData()) {
            viewModelScope.launch {
                delay(2000)
                onSuccess()
            }
        }
    }

    private fun filterPhone(phone : String): String{
        val filteredPhone = phone.filterIndexed { index, symbol ->
            symbol.isDigit() || (symbol == '+' && index == 0)
        }
        return filteredPhone
    }

    private fun validateData() : Boolean {
        var isDataCorrect = true

        if (!isPhoneValid(_state.value.phone.text)) {

            isDataCorrect = false
            _state.value = _state.value.copy(
                phone = _state.value.phone.copy(
                    isValid = false,
                    msg = "Неверный формат телефона"
                )
            )
        }
        if (!isAddressValid(_state.value.address.text)) {

            isDataCorrect = false
            _state.value = _state.value.copy(
                address = _state.value.address.copy(
                    isValid = false,
                    msg = "Пожалуйста, введите адрес, куда доставить заказ"
                )
            )
        }
        if (!isFullnameValid(_state.value.fullname.text)) {

            isDataCorrect = false
            _state.value = _state.value.copy(
                fullname = _state.value.fullname.copy(
                    isValid = false,
                    msg = "Пожалуйста, введите ФИО получателя"
                )
            )
        }

        return isDataCorrect
    }

    fun saveUserId(id: Int){
        viewModelScope.launch {
            userDatastore.saveUserId(id)
        }
    }

    fun saveToken(token: String) {
        viewModelScope.launch {
            tokenManager.saveToken(token)
        }
    }

    private fun isPhoneValid(phone: String) : Boolean {
        if (phone.length != PHONE_LENGTH) {
            return false
        }
        if (phone.startsWith("+7")) {
            return true
        }
        return false
    }

    private fun isAddressValid(address: String) = address.isNotEmpty()

    private fun isFullnameValid(fullname: String) = fullname.isNotEmpty()
}