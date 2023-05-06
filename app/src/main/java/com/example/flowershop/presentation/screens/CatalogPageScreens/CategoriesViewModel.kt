package com.example.flowershop.presentation.screens.CatalogPageScreens

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flowershop.domain.model.Category
import com.example.flowershop.domain.use_cases.ProductsUseCases.ProductsUseCases
import com.example.flowershop.data.helpers.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val productsUseCases: ProductsUseCases
) : ViewModel() {
    private val _categories = mutableStateOf<Response<List<Category>>>(Response.Loading)
    val categories: State<Response<List<Category>>> = _categories

    init {
        getCategories()
    }

    fun getCategories() {
        viewModelScope.launch {
            productsUseCases.getCategoriesUseCase().collect {
                _categories.value = it
            }
        }
    }
}