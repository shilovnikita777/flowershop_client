package com.example.flowershop.presentation.screens.MainPageScreens

import android.util.Log
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.flowershop.R
import com.example.flowershop.data.UserDatastore
import com.example.flowershop.data.helpers.Response
import com.example.flowershop.domain.model.*
import com.example.flowershop.domain.use_cases.ProductsUseCases.ProductsUseCases
import com.example.flowershop.domain.use_cases.UserUseCases.UserUseCases
import com.example.flowershop.presentation.model.ProductWithCountState
import com.example.flowershop.presentation.navigation.ARGUMENT_PRODUCT_ID
import com.example.flowershop.presentation.navigation.ARGUMENT_PRODUCT_TYPE
import com.example.flowershop.util.Constants
import com.example.flowershop.util.Constants.AUTHOR_BOUQUET_DESCRIPTION
import com.example.flowershop.util.Constants.AUTHOR_BOUQUET_ID
import com.example.flowershop.util.Constants.AUTHOR_BOUQUET_NAME
import com.example.flowershop.util.Constants.FLOWERS_CATEGORY_ID
import com.example.flowershop.util.Constants.NO_PRODUCT_CONSTANT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalMaterialApi
@HiltViewModel
class ConstructorViewModel @Inject constructor(
    private val productsUseCases: ProductsUseCases,
    private val userDatastore: UserDatastore,
    private val userUseCases: UserUseCases,
    savedStateHandle: SavedStateHandle
) : ProductBaseViewModel(
    productsUseCases = productsUseCases,
    userDatastore = userDatastore,
    savedStateHandle = savedStateHandle
) {
    private val _flowers = mutableStateOf<Response<List<Flower>>>(Response.Loading)
    val flowers : State<Response<List<Flower>>> = _flowers

    var flowersInBouquet = mutableStateListOf<ProductWithCountState>()
        private set

    var search by mutableStateOf("")
        private set

    var bottomSheetState = (BottomSheetState(
        initialValue = BottomSheetValue.Collapsed
    ))

    //var sheetState by mutableStateOf(false)
    //    private set

    var productId = NO_PRODUCT_CONSTANT
        private set

    init {

        productId = savedStateHandle.get<Int>(ARGUMENT_PRODUCT_ID) ?: NO_PRODUCT_CONSTANT
        val productType = savedStateHandle.get<String>(ARGUMENT_PRODUCT_TYPE) ?: "product"
        if (productId != NO_PRODUCT_CONSTANT && userId != Constants.NO_USER_CONSTANT) {
            loadProduct(productId, productType)
        }
        getDecorations()
        getTables()
        getProducts()
    }

    override fun loadProduct(id: Int, type: String) {
        viewModelScope.launch {
            userUseCases.getAuthorBouquetByIdUseCase(id).collect {
                _currentProductResponse.value = it
                if (it is Response.Success) {
                    _currentProduct.value = it.data
                    _count.value = _currentProduct.value.productWithCount.count.toString()

                    getDecorations()
                    _selectedDecoration.value =
                        (it.data.productWithCount.product as Bouquet).decoration

                    getTables()
                    _selectedTable.value =
                        (it.data.productWithCount.product as Bouquet).table

                    _postcardMessage.value =
                        (it.data.productWithCount.product as Bouquet).postcard

                    flowersInBouquet = (it.data.productWithCount.product as Bouquet).flowers.map {
                        ProductWithCountState(
                            product = it.product,
                            count = mutableStateOf(it.count)
                        )
                    }.toMutableStateList()

                    _price.value = it.data.totalPrice
                    Log.d("xd", "PVM: ${it.data.totalPrice}")
                }
            }
        }
    }

    fun getProducts(categoryId: Int = FLOWERS_CATEGORY_ID) {
        viewModelScope.launch {
            productsUseCases.getFlowersUseCase().map { response ->
                when(response) {
                    is Response.Loading -> {
                        response
                    }
                    is Response.Error -> {
                        response
                    }
                    is Response.Success -> {
                        val list = mutableListOf<Flower>()
                        response.data.forEach { product ->
                            list.add(product)
//                            if (product is Flower) {
//                                list.add(product)
//                            }
                        }
                        Response.Success(list)
                    }
                }
            }.collect {
                _flowers.value = it
            }
        }
    }

    fun addFlower(flower: Flower) {
        val flowerInBouquet = flowersInBouquet.find {
            it.product.id == flower.id
        }
        if (flowerInBouquet != null) {
            flowerInBouquet.count.value++
        } else {
            flowersInBouquet.add(
                ProductWithCountState(
                    product = flower
                )
            )
        }
        recalculatePrice()
    }

    fun increaseFlowerCount(flower: ProductWithCountState) {
        flower.count.value++
        recalculatePrice()
    }

    fun decreaseFlowerCount(flower: ProductWithCountState){
        if (flower.count.value <= 1) {
            flowersInBouquet.remove(flower)
        } else {
            flower.count.value--
        }
        recalculatePrice()
    }

    fun onSearchInput(text: String) {
        search = text
    }

    suspend fun onAddClicked() {
        bottomSheetState.expand()
    }

    suspend fun onDismissSheet() {
        bottomSheetState.collapse()
    }

    override fun getDecorations() {
        viewModelScope.launch {
            productsUseCases.getDecorationsUseCase().collect {
                _decorations.value = it
                if (it is Response.Success) {
                    if (it.data.isNotEmpty() && productId == NO_PRODUCT_CONSTANT) {
                        _selectedDecoration.value = it.data.first()
                        recalculatePrice()
                    }
                }
            }
        }
    }

    override fun getTables() {
        viewModelScope.launch {
            productsUseCases.getTablesUseCase().collect {
                _tables.value = it
                if (it is Response.Success) {
                    if (it.data.isNotEmpty() && productId == NO_PRODUCT_CONSTANT) {
                        _selectedTable.value = it.data.first()
                        recalculatePrice()
                    }
                }
            }
        }
    }

    override fun getProduct(): ProductInBag {
        val flowers = flowersInBouquet.toList().map {
            ProductWithCount(
                product = it.product,
                count = it.count.value
            )
        }
        if (_currentProductResponse.value is Response.Success) {
            return ProductInBag(
                productWithCount = ProductWithCount(
                    product = Bouquet(
                        id = _currentProduct.value.productWithCount.product.id,
                        image = _currentProduct.value.productWithCount.product.image,
                        name = _currentProduct.value.productWithCount.product.name,
                        description = _currentProduct.value.productWithCount.product.description,
                        flowers = flowers,
                        type = "bouquet",
                        decoration = _selectedDecoration.value,
                        table = _selectedTable.value,
                        postcard = _postcardMessage.value
                    ),
                    count = _count.value.toInt()
                )
            )
        } else {

            var categories = listOf<Int>()
            flowers.forEach {
                categories.union(it.product.categoriesIds)
            }
            categories = categories.distinct()

            val price = flowers.sumOf {
                it.product.price * it.count
            }

            return ProductInBag(
                productWithCount = ProductWithCount(
                    product = Bouquet(
                        id = AUTHOR_BOUQUET_ID,
                        price = price,
                        image = Image(R.drawable.author_bouquet_image.toString()),
                        name = AUTHOR_BOUQUET_NAME,
                        description = AUTHOR_BOUQUET_DESCRIPTION,
                        flowers = flowers,
                        type = "bouquet",
                        decoration = _selectedDecoration.value,
                        table = _selectedTable.value,
                        postcard = _postcardMessage.value,
                        categoriesIds = categories
                    ),
                    count = _count.value.toInt()
                )
            )
        }
    }

//    fun changeChosenDecoration(decoration: Decoration) {
//        _selectedDecoration.value = decoration
//        Log.d("xd",decoration.title)
//        recalculatePrice()
//    }
//
//    fun changeChosenTable(table: Table) {
//        _selectedTable.value = table
//        Log.d("xd",table.text)
//        recalculatePrice()
//    }
//
//    fun changeTablesVisibility() {
//        _isTablesVisible.value = !_isTablesVisible.value
//    }
//
//    fun changePostcardMessage(text: String) {
//        _postcardMessage.value = text
//    }

    override fun recalculatePrice() {
        val flowersPrice = flowersInBouquet.sumOf {
            it.product.price * it.count.value
        }
        _price.value = (flowersPrice +
                _selectedDecoration.value.price +
                _selectedTable.value.price) * _count.value.toInt()
    }
}