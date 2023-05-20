package com.example.flowershop.domain.repository

import android.net.Uri
import com.example.flowershop.domain.model.Product
import com.example.flowershop.domain.model.ProductInBag
import com.example.flowershop.domain.model.ProductWithCount
import com.example.flowershop.domain.model.User
import com.example.flowershop.data.helpers.Response
import com.example.flowershop.data.model.Response.UserMainInfoResponse
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getBagByUserId(id: Int) : Flow<Response<List<ProductWithCount>>>

    fun isProductInBag(product: Product) : Flow<Response<Boolean>>

    fun isAuthorInBag(productId: Int) : Flow<Response<Boolean>>

    fun addProductToBag(product:ProductInBag, userId: Int): Flow<Response<Boolean>>

    fun addAuthorToBag(product: ProductWithCount) : Flow<Response<Int>>

    fun removeProductFromBag(productId: Int, isAuthor: Boolean?): Flow<Response<Boolean>>

    fun isProductInFavourite(product: Product, userId: Int) : Flow<Response<Boolean>>

    fun addProductToFavourite(product: Product, userId: Int): Flow<Response<Boolean>>

    fun removeProductFromFavourite(productId: Int, userId: Int): Flow<Response<Boolean>>

    fun getUsername(userId: Int) : Flow<Response<String>>

    fun changeProductCount(product: ProductWithCount, count: Int, isAuthor: Boolean?) : Flow<Response<Boolean>>

    fun getFavouriteByUserId(userId: Int) : Flow<Response<MutableList<Product>>>

    fun getUserMainInfo(userId: Int) : Flow<Response<UserMainInfoResponse>>

    fun changeUserMainInfo(username: String, image: String) : Flow<Response<Boolean>>

    fun updateProductInBag(productWithCount: ProductWithCount, userId: Int) : Flow<Response<Boolean>>

    fun updateAuthorBouquetInBag(productWithCount: ProductWithCount, userId: Int) : Flow<Response<Boolean>>

    fun getAuthorBouquetById(id : Int) : Flow<Response<ProductWithCount>>

    fun deleteAccount() : Flow<Response<Boolean>>

    fun makeOrder(orderData: User.Order) : Flow<Response<Boolean>>

    fun getOrderHistory() : Flow<Response<List<User.Order>>>

    fun getOrderById(id : Int) : Flow<Response<User.Order>>
}