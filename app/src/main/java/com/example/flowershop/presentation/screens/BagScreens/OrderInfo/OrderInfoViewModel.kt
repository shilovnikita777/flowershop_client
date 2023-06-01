package com.example.flowershop.presentation.screens.BagScreens.OrderInfo

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flowershop.data.TokenManager
import com.example.flowershop.data.helpers.Response
import com.example.flowershop.domain.model.User
import com.example.flowershop.domain.use_cases.UserUseCases.UserUseCases
import com.example.flowershop.util.Constants.PHONE_LENGTH
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class OrderInfoViewModel @Inject constructor(
    private val userUseCases: UserUseCases,
    private val tokenManager : TokenManager
): ViewModel() {

    private val _userBagResponse = mutableStateOf<Response<User.Bag>>(Response.Loading)
    val userBagResponse : State<Response<User.Bag>> = _userBagResponse

    private val _makeOrderResponse = mutableStateOf<Response<Boolean>?>(null)
    val makeOrderResponse : State<Response<Boolean>?> = _makeOrderResponse

    private val _state = mutableStateOf(OrderInfoStates())
    val state : State<OrderInfoStates> = _state

    init {
        getBagByUserId()
    }

    private fun getBagByUserId() {
        viewModelScope.launch {
            userUseCases.getBagByUserIdUseCase().collect { bagResponse ->
                _userBagResponse.value = bagResponse
                if (bagResponse is Response.Success) {
                    _state.value = _state.value.copy(
                        orderSumm = bagResponse.data.total
                    )
                }
            }
        }
    }

    fun makeOrder(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val promoResponse = _state.value.usePromoResponse
            val promocodeId = if (promoResponse is Response.Success)
                promoResponse.data.id else null
            Log.d("xd11",_state.value.usePromoResponse?.toString() ?: "")
            val order = User.Order(
                products = (userBagResponse.value as Response.Success).data.products,
                date = LocalDate.now(),
                phone = _state.value.phone.text,
                address = _state.value.address.text,
                fullname = _state.value.fullname.text,
                promocodeId = promocodeId,
                summ = _state.value.orderSumm
            )
            userUseCases.makeOrderUseCase(order).collect {
                _makeOrderResponse.value = it
                if (it is Response.Success) {
                    onSuccess()
                }
            }
        }
    }

    fun usePromocode(promo : String) : Job {
        return viewModelScope.launch {
            delay(1000)
            userUseCases.usePromocodeUseCase(promo).collect {
                _state.value = _state.value.copy(
                    usePromoResponse = it
                )
                if (it is Response.Success) {
                    _state.value.usedPromocode = promo

                    val bagResponse = _userBagResponse.value
                    if (bagResponse is Response.Success) {
                        val total = bagResponse.data.total
                        _state.value = _state.value.copy(
                            orderSumm = total - total * it.data.amount/100
                        )
                    }

                }
            }
        }
    }

    fun onEvent(event: OrderInfoEvents) {
        when(event) {
            is OrderInfoEvents.EnterPhone -> {
                //val phone = filterPhone(event.value)
                _state.value = _state.value.copy(
                    phone = _state.value.phone.copy(
                        text = event.value,
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
            is OrderInfoEvents.EnterPromocode -> {
                _state.value = _state.value.copy(
                    promocode = _state.value.promocode.copy(
                        text = event.value,
                        isValid = true,
                        msg = null
                    ),
                )

                _state.value = _state.value.copy(
                    usedPromocode = null,
                    usePromoResponse = null,
                    orderSumm = (_userBagResponse.value as Response.Success).data.total
                )
                _state.value.searchJob?.cancel()
                if (event.value.isNotEmpty()) {
                    _state.value = _state.value.copy(
                        searchJob = usePromocode(event.value)
                    )
                }
            }
        }
    }

    fun pay(onSuccess:() -> Unit) {
        if (validateData()) {
            viewModelScope.launch {
                _makeOrderResponse.value = Response.Loading
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

    private fun isPhoneValid(phone: String) : Boolean {
        if (phone.length == PHONE_LENGTH && phone.startsWith("+7") && phone.subSequence(1,phone.length - 1).all {
            it.isDigit()
        }) {
            return true
        }
        if (phone.length == PHONE_LENGTH - 1 && phone.startsWith("8") && phone.all {
            it.isDigit()
        }) {
            return true
        }
        return false
    }

    private fun isAddressValid(address: String) = address.isNotEmpty()

    private fun isFullnameValid(fullname: String) = fullname.isNotEmpty()
}