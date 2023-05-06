package com.example.flowershop.presentation.screens.ProfilePageScreens

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flowershop.domain.mapper.Mapper
import com.example.flowershop.domain.model.Promocode
import com.example.flowershop.domain.use_cases.ProductsUseCases.ProductsUseCases
import com.example.flowershop.presentation.model.PromocodeUI
import com.example.flowershop.data.helpers.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PromocodesViewModel @Inject constructor(
    private val productsUseCases: ProductsUseCases,
    private val mapper: Mapper<Promocode, PromocodeUI>
) : ViewModel() {

    private val _promocodes = mutableStateOf<Response<List<PromocodeUI>>>(Response.Loading)
    val promocodes : State<Response<List<PromocodeUI>>> = _promocodes

    var isDialogShown by mutableStateOf(false)
        private set

    var description = ""
        private set

    init {
        getNewsBanners()
    }

    fun getNewsBanners() {
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

    fun onPromocodeClicked(text: String) {
        description = text
        isDialogShown = true
    }

    fun onDismissDialog() {
        isDialogShown = false
    }
}