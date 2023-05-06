package com.example.flowershop.data.network

import com.example.flowershop.data.model.Request.*
import com.example.flowershop.data.model.Response.UserMainInfoResponse
import com.example.flowershop.domain.model.Product
import com.example.flowershop.domain.model.ProductWithCount
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserApiService {
    @GET("user/username")
    suspend fun getUsername() : Response<String>

    @GET("user/userinfo")
    suspend fun getUserMainInfo() : Response<UserMainInfoResponse>

    @GET("user/isinbag/{id}")
    suspend fun isProductInBag(@Path("id") productId : Int) : Response<Boolean>

    @GET("user/isauthorinbag/{id}")
    suspend fun isAuthorInBag(@Path("id") productId : Int) : Response<Boolean>

    @GET("user/isinfavourite/{id}")
    suspend fun isProductInFavourite(@Path("id") productId : Int) : Response<Boolean>

    @GET("user/bag")
    suspend fun getBag() : Response<List<ProductWithCount>>

    @POST("user/addtobag")
    suspend fun addToBag(
        @Body productToBagData : AddToBagRequest
    ) : Response<Boolean>

    @POST("user/addauthortobag")
    suspend fun addAuthorToBag(
        @Body authorToBag : AddAuthorToBagRequest
    ) : Response<Int>

    @POST("user/addtofavourite")
    suspend fun addToFavourite(
        @Body productId : ProductIdRequest
    ) : Response<Boolean>

    @POST("user/removefromfavourite")
    suspend fun removeFromFavourite(
        @Body productId: ProductIdRequest
    ) : Response<Boolean>

    @POST("user/removefrombag")
    suspend fun removeFromBag(
        @Body productId: RemoveFromBagRequest
    ) : Response<Boolean>

    @GET("user/favourite")
    suspend fun getFavourite() : Response<MutableList<Product>>

    @POST("user/updateproduct")
    suspend fun updateInBag(
        @Body updateProductInBagData : AddToBagRequest
    ) : Response<Boolean>

    @POST("user/updateauthor")
    suspend fun updateAuthorInBag(
        @Body updateProductInBagData : AddAuthorToBagRequest
    ) : Response<Boolean>

    @POST("user/changecount")
    suspend fun changeProductInBagCount(
        @Body changeCountData : ChangeCountRequest
    ) : Response<Boolean>

    @GET("user/author/{id}")
    suspend fun getAuthorBouquetById(@Path("id") authorBouquetId : Int) : Response<ProductWithCount>
}