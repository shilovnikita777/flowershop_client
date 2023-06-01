package com.example.flowershop.presentation.screens.CatalogPageScreens

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.flowershop.data.helpers.Response
import com.example.flowershop.domain.model.*
import com.example.flowershop.domain.use_cases.ProductsUseCases.ProductsUseCases
import com.example.flowershop.presentation.navigation.ARGUMENT_PRODUCT_ID
import com.example.flowershop.presentation.navigation.ARGUMENT_PRODUCT_TYPE
import com.example.flowershop.presentation.screens.MainPageScreens.ProductBaseViewModel
import com.example.flowershop.util.Constants.NO_PRODUCT_CONSTANT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productsUseCases: ProductsUseCases,
    savedStateHandle: SavedStateHandle
) : ProductBaseViewModel(
    productsUseCases = productsUseCases,
    savedStateHandle = savedStateHandle
){

    private val _isProductInFavourite = mutableStateOf<Response<Boolean>>(Response.Loading)
    val isProductInFavourite : State<Response<Boolean>> = _isProductInFavourite

    init {
        val productId = savedStateHandle.get<Int>(ARGUMENT_PRODUCT_ID) ?: NO_PRODUCT_CONSTANT
        val productType = savedStateHandle.get<String>(ARGUMENT_PRODUCT_TYPE) ?: "product"
        if (productId != NO_PRODUCT_CONSTANT) {
            loadProduct(productId,productType)
        }
    }
    override fun loadProduct(id: Int, type: String) {
        viewModelScope.launch {
            productsUseCases.getProductByIdUseCase(id, type).collect {
                _currentProductResponse.value = it
                if (it is Response.Success) {
                    _currentProduct.value = it.data
                    _count.value = _currentProduct.value.productWithCount.count.toString()

                    if (it.data.productWithCount.product is Flower) {
                        getDecorations()

                        if (it.data.productWithCount is FlowersWithDecoration) {
                            Log.d("xd", it.data.productWithCount.decoration.title)
                            _selectedDecoration.value = it.data.productWithCount.decoration
                        }

                        //_price.value = it.data.totalPrice

                    } else if (it.data.productWithCount.product is Bouquet) {

                        getDecorations()
                        _selectedDecoration.value =
                            (it.data.productWithCount.product as Bouquet).decoration
                        Log.d("xd8",_selectedDecoration.value.toString())

                        getTables()
                        _selectedTable.value =
                            (it.data.productWithCount.product as Bouquet).table

                        _postcardMessage.value =
                            (it.data.productWithCount.product as Bouquet).postcard
                    }

                    _price.value = it.data.totalPrice
                    Log.d("xd", "PVM: ${it.data.totalPrice}")
                }
            }
        }
    }

    fun Log() {
        val i = _currentProductResponse.value
        if (i is Response.Success) {
            Log.d("xd", "${i.data}")
        }

    }

//    private fun getDecorations() {
//        viewModelScope.launch {
//            productsUseCases.getDecorationsUseCase().collect {
//                _decorations.value = it
////                    if (it is Response.Success) {
////                        if (it.data.isNotEmpty())
////                            _selectedDecoration.value = it.data.first()
////                    }
//            }
//        }
//    }
//
//    private fun getTables() {
//        viewModelScope.launch {
//            productsUseCases.getTablesUseCase().collect {
//                _tables.value = it
////                    if (it is Response.Success) {
////                        if (it.data.isNotEmpty())
////                            _selectedTable.value = it.data.first()
////                    }
//            }
//        }
//    }

    override fun getProduct() : ProductInBag {
        val productInBag = _currentProduct.value

        productInBag.productWithCount.count = _count.value.toInt()

        if (productInBag.productWithCount is FlowersWithDecoration) {

            productInBag.productWithCount.decoration = _selectedDecoration.value

        } else if (productInBag.productWithCount.product is Bouquet) {

            (productInBag.productWithCount.product as Bouquet).decoration = _selectedDecoration.value
            (productInBag.productWithCount.product as Bouquet).table = _selectedTable.value
            (productInBag.productWithCount.product as Bouquet).postcard = _postcardMessage.value

        }
        return productInBag
    }

    override fun getDecorations() {
        viewModelScope.launch {
            productsUseCases.getDecorationsUseCase().collect {
                _decorations.value = it
//                if (it is Response.Success) {
//                    if (it.data.isNotEmpty())
//                        if (_currentProduct.value.productWithCount.product is Flower)
//                        _selectedDecoration.value = it.data.first()
//                }
            }
        }
    }

    override fun getTables() {
        viewModelScope.launch {
            productsUseCases.getTablesUseCase().collect {
                _tables.value = it
//                    if (it is Response.Success) {
//                        if (it.data.isNotEmpty())
//                            _selectedTable.value = it.data.first()
//                    }
            }
        }
    }

    //    fun changeChosenDecoration(decoration: Decoration) {
//        _selectedDecoration.value = decoration
//        Log.d("xd",decoration.title)
//    }
//
//    fun changeChosenTable(table: Table) {
//        _selectedTable.value = table
//        Log.d("xd",table.text)
//    }
//
//    fun changeTablesVisibility() {
//        _isTablesVisible.value = !_isTablesVisible.value
//    }
//
//    fun changeProductInBagState(inBagResponse :Response<Boolean>) {
//        _isProductInBag.value = inBagResponse
//    }
//
    fun changeProductInFavouriteState(inFavouriteResponse :Response<Boolean>) {
        _isProductInFavourite.value = inFavouriteResponse
    }
//
//    fun changePostcardMessage(text: String) {
//        _postcardMessage.value = text
//    }

//    override fun changeCount(numberText : String) {
//        _count.value = numberText.filter {
//            it.isDigit()
//        }
//
//        if (_count.value.isEmpty() || !_count.value.isDigitsOnly()) {
//            _count.value = MIN_PRODUCT_COUNT.toString()
//        }
//
//        if (_count.value.first() == '0') {
//            _count.value = numberText.trimStart('0')
//        }
//
//        if (_count.value.toInt() > MAX_PRODUCT_COUNT) {
//            _count.value = numberText.dropLast(1)
//        }
//
//        recalculatePrice()
//    }

    override fun recalculatePrice() {
        Log.d("xd2",_count.value)
        if (_currentProduct.value.productWithCount.product is Flower)
            _price.value = _currentProduct.value.productWithCount.product.price *
                    _count.value.toInt() +
                    _selectedDecoration.value.price
        else
            _price.value = (_currentProduct.value.productWithCount.product.price +
                    _selectedDecoration.value.price +
                    _selectedTable.value.price) *
                    _count.value.toInt()
    }
}