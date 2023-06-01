package com.example.flowershop.presentation.screens.MainPageScreens

import androidx.compose.runtime.*
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.flowershop.data.helpers.Response
import com.example.flowershop.domain.model.*
import com.example.flowershop.domain.use_cases.ProductsUseCases.ProductsUseCases
import com.example.flowershop.util.Constants

abstract class ProductBaseViewModel (
    private val productsUseCases: ProductsUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    protected val _currentProductResponse = mutableStateOf<Response<ProductInBag>>(Response.Loading)
    val currentProductResponse : State<Response<ProductInBag>> = _currentProductResponse

    protected val _currentProduct = mutableStateOf(ProductInBag(ProductWithCount(Product())))
    val currentProduct : State<ProductInBag> = _currentProduct

    protected val _isProductInBag = mutableStateOf<Response<Boolean>>(Response.Loading)
    val isProductInBag : State<Response<Boolean>> = _isProductInBag

    protected val _decorations = mutableStateOf<Response<List<Decoration>>>(Response.Loading)
    val decorations : State<Response<List<Decoration>>> = _decorations

    protected val _selectedDecoration : MutableState<Decoration> = mutableStateOf(Decoration())
    val selectedDecoration : State<Decoration> = _selectedDecoration

    protected val _tables = mutableStateOf<Response<List<Table>>>(Response.Loading)
    val tables : State<Response<List<Table>>> = _tables

    protected val _selectedTable : MutableState<Table> = mutableStateOf(Table())
    val selectedTable : State<Table> = _selectedTable

    protected val _isTablesVisible = mutableStateOf(true)
    val isTablesVisible : State<Boolean> = _isTablesVisible

    protected val _postcardMessage = mutableStateOf("")
    val postcardMessage : State<String> = _postcardMessage

    protected val _count = mutableStateOf("1")
    val count : State<String> = _count

    protected val _price = mutableStateOf(0)
    val price : State<Int> = _price

    protected abstract fun loadProduct(id: Int, type: String)

    protected abstract fun getDecorations()

    protected abstract fun getTables()

    fun changeChosenDecoration(decoration: Decoration) {
        _selectedDecoration.value = decoration
        recalculatePrice()
    }

    fun changeChosenTable(table: Table) {
        _selectedTable.value = table
        recalculatePrice()
    }

    fun changeTablesVisibility() {
        _isTablesVisible.value = !_isTablesVisible.value
    }

    fun changeProductInBagState(inBagResponse : Response<Boolean>) {
        _isProductInBag.value = inBagResponse
    }

    fun changePostcardMessage(text: String) {
        _postcardMessage.value = text
    }

    fun changeCount(numberText : String) {
        _count.value = numberText.filter {
            it.isDigit()
        }

        if (_count.value.isEmpty() || !_count.value.isDigitsOnly()) {
            _count.value = Constants.MIN_PRODUCT_COUNT.toString()
        }

        if (_count.value.first() == '0') {
            _count.value = numberText.trimStart('0')
        }

        if (_count.value.toInt() > Constants.MAX_PRODUCT_COUNT) {
            _count.value = numberText.dropLast(1)
        }

        recalculatePrice()
    }

    abstract fun getProduct() : ProductInBag

    protected abstract fun recalculatePrice()
}