package com.example.flowershop.presentation.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flowershop.domain.model.*
import com.example.flowershop.domain.use_cases.UserUseCases.UserUseCases
import com.example.flowershop.data.helpers.Response
import com.example.flowershop.util.Constants.AUTHOR_BOUQUET_NAME
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProductViewModel @Inject constructor(
    private val userUseCases: UserUseCases,
) : ViewModel() {

    fun isProductInBag(product: Product, onValueChanged: (Response<Boolean>) -> Unit) {
        viewModelScope.launch {
            userUseCases.isProductInBagUseCase(
                product = product
            ).collect {
                onValueChanged(it)
            }
        }
    }

    fun isAuthorBouquetInBag(productId: Int, onValueChanged: (Response<Boolean>) -> Unit) {
        viewModelScope.launch {
            userUseCases.isAuthorBouquetInBagUseCase(
                productId = productId
            ).collect {
                onValueChanged(it)
            }
        }
    }

    fun addProductToBag(productWithCount: ProductWithCount, onValueChanged: (Response<Boolean>) -> Unit) {
        viewModelScope.launch {
            userUseCases.addProductToBagUseCase(
                product = ProductInBag(
                    productWithCount = productWithCount
                )
            ).collect {
                if (it is Response.Success) {
                    isProductInBag(productWithCount.product) {
                        onValueChanged(it)
                    }
                } else {
                    onValueChanged(it)
                }
            }
        }
    }

    fun addAuthorToBag(
        productWithCount: ProductWithCount,
        onValueChanged: (Response<Boolean>) -> Unit,
        changeProductId : (Int) -> Unit
    ) {
        viewModelScope.launch {
            userUseCases.addAuthorToBagUseCase(
                product = productWithCount
            ).collect {
                if (it is Response.Success) {
                    changeProductId(it.data)
                    isAuthorBouquetInBag(it.data) {
                        onValueChanged(it)
                    }
                } else if (it is Response.Loading){
                    onValueChanged(it)
                } else if (it is Response.Error){
                    onValueChanged(it)
                }
            }
        }
    }

    fun updateProductInBag(productWithCount: ProductWithCount, onValueChanged: (Response<Boolean>) -> Unit) {
        viewModelScope.launch {
            userUseCases.updateProductInBagUseCase(
                product = productWithCount
            ).collect {
//                Log.d("esp1",it.toString())
                if (it is Response.Success) {
                    isProductInBag(productWithCount.product) {
                        onValueChanged(it)
                    }
                } else {
                    onValueChanged(it)
                }
            }
        }
    }

    fun updateAuthorBouquetInBag(productWithCount: ProductWithCount, onValueChanged: (Response<Boolean>) -> Unit) {
        viewModelScope.launch {
            userUseCases.updateAuthorBouquetInBagUseCase(
                product = productWithCount
            ).collect {
                if (it is Response.Success) {
                    isAuthorBouquetInBag(productWithCount.product.id) {
                        onValueChanged(it)
                    }
                } else if (it is Response.Loading){
                    onValueChanged(it)
                } else if (it is Response.Error){
                    onValueChanged(it)
                }
            }
        }
    }


    fun removeProductFromBag(product: Product, onValueChanged: (Response<Boolean>) -> Unit) {
        var isAuthor : Boolean? = null
        if (product.type == "bouquet") {
            isAuthor = product.name == AUTHOR_BOUQUET_NAME
        }
        viewModelScope.launch {
            userUseCases.removeProductFromBagUseCase(
                productId = product.id,
                isAuthor = isAuthor
            ).collect {
                if (it is Response.Success) {
                    if (isAuthor != true) {
                        isProductInBag(product) {
                            onValueChanged(it)
                        }
                    } else {
                        isAuthorBouquetInBag(product.id) {
                            onValueChanged(it)
                        }
                    }
                }
                else {
                    onValueChanged(it)
                }
            }
        }
    }

    fun addProductToFavourite(product: Product, onValueChanged: (Response<Boolean>) -> Unit) {
        viewModelScope.launch {
            userUseCases.addProductToFavouriteUseCase(
                product = product
            ).collect {
                if (it is Response.Success) {
                    isProductInFavourite(product) {
                        onValueChanged(it)
                    }
                } else {
                    onValueChanged(it)
                }
            }
        }
    }

    fun removeProductFromFavourite(product: Product, onValueChanged: (Response<Boolean>) -> Unit) {
        viewModelScope.launch {
            userUseCases.removeProductFromFavouriteUseCase(
                productId = product.id
            ).collect {
                if (it is Response.Success) {
                    isProductInFavourite(product) {
                        onValueChanged(it)
                    }
                }
                else {
                    onValueChanged(it)
                }
            }
        }
    }

    fun isProductInFavourite(product: Product, onValueChanged: (Response<Boolean>) -> Unit) {
        viewModelScope.launch {
            userUseCases.isProductInFavouriteUseCase(
                product = product
            ).collect {
                onValueChanged(it)
            }
        }
    }
}