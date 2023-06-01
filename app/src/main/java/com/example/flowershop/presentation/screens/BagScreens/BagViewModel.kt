package com.example.flowershop.presentation.screens.BagScreens

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flowershop.domain.use_cases.UserUseCases.UserUseCases
import com.example.flowershop.presentation.model.UserBagState
import com.example.flowershop.data.helpers.Response
import com.example.flowershop.domain.model.*
import com.example.flowershop.util.Constants.AUTHOR_BOUQUET_NAME
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BagViewModel @Inject constructor(
    private val userUseCases: UserUseCases
) : ViewModel() {

    private val _userBagResponse = mutableStateOf<Response<User.Bag>>(Response.Loading)
    val userBagResponse : State<Response<User.Bag>> = _userBagResponse

    private val _userBagState = UserBagState()
    val userBagState = _userBagState

    init {
        getBagByUserId()
    }

    private fun getBagByUserId() {
        viewModelScope.launch {
            userUseCases.getBagByUserIdUseCase().collect { bagResponse ->
                if (bagResponse is Response.Success) {
                    _userBagState.products = bagResponse.data.products.map {
                        Triple(
                            first = it,
                            second = mutableStateOf<Response<Boolean>>(Response.Success(false)),
                            third = mutableStateOf<Response<Boolean>>(Response.Success(false))
                        )
                    }.toMutableStateList()
                    _userBagState.total.value = _userBagState.calculateTotal()
                }
                _userBagResponse.value = bagResponse
            }
        }
    }

    fun deleteProductFromLocalBag(productInBag: ProductInBag) {
        val e = _userBagState.products.find {
            it.first.productWithCount.product.id == productInBag.productWithCount.product.id
                    && it.first.productWithCount.product.name == productInBag.productWithCount.product.name
        }
        _userBagState.products.remove(e)
        _userBagState.total.value = _userBagState.calculateTotal()
    }

    fun changeCount(product: Triple<ProductInBag,MutableState<Response<Boolean>>,MutableState<Response<Boolean>>>, count: Int) {
        viewModelScope.launch {
            var isAuthor : Boolean? = null
            if (product.first.productWithCount.product is Bouquet) {
                isAuthor = product.first.productWithCount.product.name == AUTHOR_BOUQUET_NAME
            }
            userUseCases.changeProductCountUseCase(product.first.productWithCount, count, isAuthor).collect { response ->
                _userBagState.total.value = _userBagState.calculateTotal()
                if (response is Response.Success && response.data) {
                    product.first.productWithCount.count = count
                    _userBagState.total.value = _userBagState.calculateTotal()
                }
                product.second.value = response
            }
        }
    }
}