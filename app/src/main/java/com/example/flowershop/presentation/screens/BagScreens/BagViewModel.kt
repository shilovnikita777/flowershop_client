package com.example.flowershop.presentation.screens.BagScreens

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flowershop.data.UserDatastore
import com.example.flowershop.domain.use_cases.UserUseCases.UserUseCases
import com.example.flowershop.presentation.model.UserBagState
import com.example.flowershop.util.Constants.NO_USER_CONSTANT
import com.example.flowershop.data.helpers.Response
import com.example.flowershop.domain.model.*
import com.example.flowershop.util.Constants.AUTHOR_BOUQUET_NAME
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BagViewModel @Inject constructor(
    private val userUseCases: UserUseCases,
    private val userDatastore: UserDatastore
) : ViewModel() {

    private val _userBagResponse = mutableStateOf<Response<User.Bag>>(Response.Loading)
    val userBagResponse : State<Response<User.Bag>> = _userBagResponse

//    var productsInBag = mutableStateListOf<Triple<ProductWithCount<Product>,MutableState<Response<Boolean>>,MutableState<Response<Boolean>>>>()
//
//    private val _total = mutableStateOf(0)
//    val total : State<Int> = _total

    private val _userBagState = UserBagState()
    val userBagState = _userBagState
    
//    private val _userBag = mutableStateOf(User.Bag())
//    val userBag : State<User.Bag> = _userBag

    private var userId = NO_USER_CONSTANT

    init {
        viewModelScope.launch {
            userDatastore.getUserId.collect {
                if (it != NO_USER_CONSTANT) {
                    userId = it
                    getBagByUserId()
                }
            }
        }
    }

    fun getBagByUserId() {
        if (userId != NO_USER_CONSTANT) {
            viewModelScope.launch {
                userUseCases.getBagByUserIdUseCase(userId).collect { bagResponse ->
                    if (bagResponse is Response.Success) {
                        //
                        _userBagState.products = bagResponse.data.products.map {
                            if (it.productWithCount.product is Bouquet) {
                                Log.d("xd6","bouquet")
                            } else if (it.productWithCount.product is Flower) {
                                Log.d("xd6","flower")
                            } else {
                                Log.d("xd6","product")
                            }
                            Log.d("xd3", "price = ${it.productWithCount.product.price } count = ${it.productWithCount.count} totalPrice = ${it.totalPrice}")
                            Triple(
                                first = it,
                                second = mutableStateOf<Response<Boolean>>(Response.Success(false)),
                                third = mutableStateOf<Response<Boolean>>(Response.Success(false))
                            )
                        }.toMutableStateList()
                        _userBagState.total.value = _userBagState.calculateTotal()
                        //_userBagState.total.value = bagResponse.data.total
                        //
//                        productsInBag = bagResponse.data.products.map {
//                            Triple(
//                                first = it,
//                                second = mutableStateOf<Response<Boolean>>(Response.Success(false)),
//                                third = mutableStateOf<Response<Boolean>>(Response.Success(false))
//                            )
//                        }.toMutableStateList()

                        //_total.value = bagResponse.data.total
                    }
                    _userBagResponse.value = bagResponse
                }
            }
        } else {
            _userBagResponse.value = Response.Error("Ошибка при идентификации пользователя")
        }
    }

    fun deleteProductFromLocalBag(productInBag: ProductInBag) {
//        val e = productsInBag.find {
//            it.first.product.id == product.product.id
//        }
//        productsInBag.remove(e)

        val e = _userBagState.products.find {
            it.first.productWithCount.product.id == productInBag.productWithCount.product.id
                    && it.first.productWithCount.product.name == productInBag.productWithCount.product.name
        }
        _userBagState.products.remove(e)
        _userBagState.total.value = _userBagState.calculateTotal()
        //recalculateTotal()
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

//    fun recalculateTotal() {
//        _total.value = productsInBag.sumOf {
//            it.first.product.price * it.first.count
//        }
//    }
}