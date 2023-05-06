package com.example.flowershop.presentation.screens.ProfilePageScreens

import androidx.compose.runtime.*
import androidx.lifecycle.*
import com.example.flowershop.data.UserDatastore
import com.example.flowershop.domain.model.Product
import com.example.flowershop.domain.model.User
import com.example.flowershop.domain.use_cases.UserUseCases.UserUseCases
import com.example.flowershop.util.Constants.NO_USER_CONSTANT
import com.example.flowershop.data.helpers.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(
    private val userUseCases: UserUseCases,
    private val userDatastore: UserDatastore
) : ViewModel() {

    private var _userId = NO_USER_CONSTANT

    private val _userFavouriteResponse = mutableStateOf<Response<User.Favourite>>(Response.Loading)
    val userFavouriteResponse : State<Response<User.Favourite>> = _userFavouriteResponse

    var productsInFavourite = mutableStateListOf<Triple<Product, MutableState<Response<Boolean>>,MutableState<Response<Boolean>>>>()

    init {
        viewModelScope.launch {
            userDatastore.getUserId.collect {
                if (it != NO_USER_CONSTANT) {
                    _userId = it
                    getFavouriteByUserId()
                }
            }
        }
    }

    private suspend fun getFavouriteByUserId() {
        if (_userId != NO_USER_CONSTANT) {
            userUseCases.getFavouriteByUserIdUseCase(_userId).collect { favouriteResponse ->
                if (favouriteResponse is Response.Success) {
                    productsInFavourite = favouriteResponse.data.products.map {
                        Triple(
                            first = it,
                            second = mutableStateOf<Response<Boolean>>(Response.Success(false)),
                            third = mutableStateOf<Response<Boolean>>(Response.Success(false))
                        )
                    }.toMutableStateList()
                }
                _userFavouriteResponse.value = favouriteResponse
            }
        } else {
            _userFavouriteResponse.value = Response.Error("Ошибка при идентификации пользователя")
            //Log.d("xd",id.toString())
        }
    }

    fun deleteProductFromLocalFavourite(product: Product) {
        val e = productsInFavourite.find {
            it.first.id == product.id
        }
        productsInFavourite.remove(e)
    }
}