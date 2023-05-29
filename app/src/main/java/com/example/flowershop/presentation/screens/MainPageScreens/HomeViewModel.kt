package com.example.flowershop.presentation.screens.MainPageScreens

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flowershop.data.UserDatastore
import com.example.flowershop.domain.mapper.Mapper
import com.example.flowershop.domain.model.Product
import com.example.flowershop.domain.model.Promocode
import com.example.flowershop.domain.use_cases.ProductsUseCases.ProductsUseCases
import com.example.flowershop.domain.use_cases.UserUseCases.UserUseCases
import com.example.flowershop.presentation.model.PromocodeUI
import com.example.flowershop.util.Constants.NO_USER_CONSTANT
import com.example.flowershop.data.helpers.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productsUseCases: ProductsUseCases,
    private val userUseCases: UserUseCases,
    private val userDatastore: UserDatastore,
    private val mapper: Mapper<Promocode,PromocodeUI>
) : ViewModel() {

    private val _popularProducts = mutableStateOf<Response<MutableList<Triple<Product, MutableState<Response<Boolean>>, MutableState<Response<Boolean>>>>>>(
        Response.Loading)
    val popularProducts: State<Response<MutableList<Triple<Product, MutableState<Response<Boolean>>, MutableState<Response<Boolean>>>>>> = _popularProducts

    private val _newProducts = mutableStateOf<Response<MutableList<Triple<Product, MutableState<Response<Boolean>>, MutableState<Response<Boolean>>>>>>(
        Response.Loading)
    val newProducts: State<Response<MutableList<Triple<Product, MutableState<Response<Boolean>>, MutableState<Response<Boolean>>>>>> = _newProducts

//    private val _newProducts = mutableStateOf<Response<List<Product>>>(Response.Loading)
//    val newProducts: State<Response<List<Product>>> = _newProducts

    private val _username = mutableStateOf<Response<String>>(Response.Loading)
    val username : State<Response<String>> = _username

    private val _promocodes = mutableStateOf<Response<List<PromocodeUI>>>(Response.Loading)
    val promocodes : State<Response<List<PromocodeUI>>> = _promocodes

    init {
        viewModelScope.launch {
            userUseCases.getUsernameUseCase().collect {
                _username.value = it
            }
        }
        getPopularProducts()
        getNewProducts()
        getPromocodes()
    }

    private fun getPopularProducts() {
        viewModelScope.launch {
            productsUseCases.getPopularProductsUseCase().collect { response ->
                _popularProducts.value = response
//                 if (response is Response.Success) {
//                     response.data.forEach { product ->
//                        isProductInBag(product.first) {
//                            product.second.value = it
//                        }
//                        isProductInFavourite(product.first) {
//                            product.third.value = it
//                        }
//                    }
//                }
            }
        }
    }

    private fun getNewProducts() {
        viewModelScope.launch {
            productsUseCases.getNewProductsUseCase().collect { response ->
                _newProducts.value = response
//                if (response is Response.Success) {
//                    response.data.forEach { product ->
//                        isProductInBag(product.first) {
//                            product.second.value = it
//                        }
//                        isProductInFavourite(product.first) {
//                            product.third.value = it
//                        }
//                    }
//                }
            }
        }
    }

    private fun getPromocodes() {
        viewModelScope.launch {
            productsUseCases.getPromocodesUseCase().map { response ->
                when(response) {
                    is Response.Error -> {
                        response
                    }
                    is Response.Loading -> {
                        response
                    }
                    is Response.Success -> {
                        val list = response.data.map {
                            mapper.map(it)
                        }
                        Response.Success(list)
                    }
                }
            }.collect {
                _promocodes.value = it
            }
        }
    }
}