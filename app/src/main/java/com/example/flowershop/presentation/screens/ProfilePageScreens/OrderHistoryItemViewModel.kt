package com.example.flowershop.presentation.screens.ProfilePageScreens

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flowershop.data.helpers.Response
import com.example.flowershop.data.model.Response.OrderResponse
import com.example.flowershop.domain.model.Product
import com.example.flowershop.domain.model.ProductInBag
import com.example.flowershop.domain.model.ProductWithCount
import com.example.flowershop.domain.model.User
import com.example.flowershop.domain.use_cases.UserUseCases.UserUseCases
import com.example.flowershop.presentation.navigation.ARGUMENT_PRODUCT_ID
import com.example.flowershop.util.Constants
import com.example.flowershop.util.Constants.NO_ORDER_CONSTANT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderHistoryItemViewModel @Inject constructor(
    private val userUseCases: UserUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _order = mutableStateOf<Response<OrderResponse>>(Response.Loading)
    val order : State<Response<OrderResponse>> = _order

    init {
        val orderId = savedStateHandle.get<Int>(ARGUMENT_PRODUCT_ID) ?: NO_ORDER_CONSTANT
        if (orderId != NO_ORDER_CONSTANT) {
            getOrder(orderId)
        } else {
            _order.value = Response.Error("Ошибка при идентификации заказа")
        }
    }

    private fun getOrder(id : Int) {
        viewModelScope.launch {
            userUseCases.getOrderByIdUseCase(id).collect {
                _order.value = it
            }
        }
    }
}