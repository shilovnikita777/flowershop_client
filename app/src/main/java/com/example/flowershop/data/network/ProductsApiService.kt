package com.example.flowershop.data.network

import com.example.flowershop.domain.model.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductsApiService {
    @GET("products/popular")
    suspend fun getPopularProducts() : Response<List<Product>>

    @GET("products/new")
    suspend fun getNewProducts() : Response<List<Product>>

    @GET("products/promocodes")
    suspend fun getPromocodes() : Response<List<Promocode>>

    @GET("products/categories")
    suspend fun getCategories() : Response<List<Category>>

    @GET("products/all")
    suspend fun getAllProducts() : Response<List<Product>>

    @GET("products")
    suspend fun getProductsByCategories(
        @Query("category") categoryId : Int
    ) : Response<List<Product>>

    @GET("products/flowers")
    suspend fun getFlowers(
        @Query("minprice") minPrice : Int?,
        @Query("maxprice") maxPrice : Int?,
        @Query("sorts") sorts : List<String>?,
        @Query("search") search : String?,
        @Query("sort") sort : String?,
    ) : Response<List<Flower>>

    @GET("products/decorations")
    suspend fun getDecorations() : Response<List<Decoration>>

    @GET("products/tables")
    suspend fun getTables() : Response<List<Table>>

//    @GET("products/{id}")
//    suspend fun getProductById(@Path("id") productId: Int) : Response<ProductInBag>

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") productId : Int) : Response<ProductWithCount>

    @GET("products")
    suspend fun getProducts(
        @Query("category") categoryId : Int?,
        @Query("minprice") minPrice : Int?,
        @Query("maxprice") maxPrice : Int?,
        @Query("sorts") sorts : List<String>?,
        @Query("size") size : String?,
        @Query("search") search : String?,
        @Query("sort") sort : String?,
    ) : Response<List<Product>>

    @GET("products/flowers/sorts")
    suspend fun getSorts() : Response<List<Sort>>
}