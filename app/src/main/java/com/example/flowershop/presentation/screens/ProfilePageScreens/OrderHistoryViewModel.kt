package com.example.flowershop.presentation.screens.ProfilePageScreens

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flowershop.data.helpers.Response
import com.example.flowershop.domain.mapper.Mapper
import com.example.flowershop.domain.model.Promocode
import com.example.flowershop.domain.model.User
import com.example.flowershop.domain.use_cases.ProductsUseCases.ProductsUseCases
import com.example.flowershop.domain.use_cases.UserUseCases.UserUseCases
import com.example.flowershop.presentation.model.PromocodeUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderHistoryViewModel @Inject constructor(
    private val userUseCases: UserUseCases
) : ViewModel() {

    private val _orders = mutableStateOf<Response<List<User.Order>>>(Response.Loading)
    val orders : State<Response<List<User.Order>>> = _orders

    init {
        getOrderHistory()
    }

    fun getOrderHistory() {
        viewModelScope.launch {
            userUseCases.getOrderHistoryUseCase().collect {
                _orders.value = it
            }
        }
    }
}