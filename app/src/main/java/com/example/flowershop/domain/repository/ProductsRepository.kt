package com.example.flowershop.domain.repository

import com.example.flowershop.domain.model.*
import com.example.flowershop.data.helpers.Response
import com.example.flowershop.presentation.model.SearchConditions
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {

    fun getPopularProducts() : Flow<Response<List<Product>>>

    fun getNewProducts() : Flow<Response<List<Product>>>

    fun getPromocodes() : Flow<Response<List<Promocode>>>

    fun getCategories() : Flow<Response<List<Category>>>

    fun getAllProducts() : Flow<Response<List<Product>>>

    fun getProductsByCategory(categoryId: Int) : Flow<Response<List<Product>>>

    fun getProductById(productId: Int, type : String) : Flow<Response<ProductWithCount>>

    fun getDecorations() : Flow<Response<List<Decoration>>>

    fun getTables() : Flow<Response<List<Table>>>

    fun getFlowers(searchConditions: SearchConditions) : Flow<Response<List<Flower>>>

    fun getProducts(searchConditions: SearchConditions) : Flow<Response<List<Product>>>
}