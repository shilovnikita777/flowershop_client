package com.example.flowershop.presentation.screens.CatalogPageScreens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flowershop.domain.model.Product
import com.example.flowershop.domain.use_cases.ProductsUseCases.ProductsUseCases
import com.example.flowershop.domain.use_cases.UserUseCases.UserUseCases
import com.example.flowershop.presentation.navigation.ARGUMENT_CATEGORY_ID
import com.example.flowershop.data.helpers.Response
import com.example.flowershop.presentation.screens.MainPageScreens.SortAndFilterViewModel
import com.example.flowershop.util.Constants.NO_CATEGORY_CONSTANT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val productsUseCases: ProductsUseCases,
    private val userUseCases: UserUseCases,
    savedStateHandle: SavedStateHandle
) : SortAndFilterViewModel() {

    private val _currentProducts = mutableStateOf<Response<MutableList<Triple<Product, MutableState<Response<Boolean>>, MutableState<Response<Boolean>>>>>>(
        Response.Loading)
    val currentProducts : State<Response<MutableList<Triple<Product, MutableState<Response<Boolean>>, MutableState<Response<Boolean>>>>>> = _currentProducts
    var isProductsLoaded = false
        private set

    private var categoryId = NO_CATEGORY_CONSTANT

    init {
        categoryId = savedStateHandle.get<Int>(ARGUMENT_CATEGORY_ID) ?: NO_CATEGORY_CONSTANT
        setCategory(categoryId)

        getProducts()
    }

    fun changeProductsLoaded(state : Boolean) {
        isProductsLoaded = state
    }

    fun getProducts() : Job {
        isProductsLoaded = false
        return viewModelScope.launch {
            productsUseCases.getProductsUseCase(_searchConditions.value).map { response ->
                when (response) {
                    is Response.Error -> {
                        response
                    }
                    is Response.Loading -> {
                        response
                    }
                    is Response.Success -> {
                        val list =
                            mutableListOf<Triple<Product, MutableState<Response<Boolean>>, MutableState<Response<Boolean>>>>()
                        response.data.forEach {
                            list.add(
                                Triple(
                                    first = it,
                                    second = mutableStateOf(Response.Success(false)),
                                    third = mutableStateOf(Response.Success(false))
                                )
                            )
                        }
                        Response.Success(list)
                    }
                }
            }.collect {
                _currentProducts.value = it
            }
        }
    }

    fun performSearchWithDelay() : Job {
        return viewModelScope.launch {
            delay(1000)
            getProducts()
        }
    }
}