package com.example.flowershop.data.repository

import com.example.flowershop.data.helpers.Response
import com.example.flowershop.data.helpers.apiRequestFlow
import com.example.flowershop.data.model.Request.*
import com.example.flowershop.data.model.Response.OrderResponse
import com.example.flowershop.data.network.UserApiService
import com.example.flowershop.domain.model.*
import com.example.flowershop.domain.repository.UserRepository
import com.example.flowershop.presentation.model.UserEditInfo
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApiService : UserApiService
) : UserRepository {
    override fun getUserBag(): Flow<Response<List<ProductWithCount>>> = apiRequestFlow {
        userApiService.getBag()
    }

    override fun isProductInBag(product: Product) = apiRequestFlow {
        userApiService.isProductInBag(product.id)
    }

    override fun isAuthorInBag(productId: Int) = apiRequestFlow {
        userApiService.isAuthorInBag(productId)
    }

    override fun getAuthorBouquetById(id: Int) = apiRequestFlow {
        userApiService.getAuthorBouquetById(id)
    }

    override fun addProductToBag(product: ProductInBag) = apiRequestFlow {
        when (product.productWithCount.product.type) {
            "bouquet" -> {
                if (product.productWithCount.product is Bouquet) {
                    val bouquet = product.productWithCount.product as Bouquet
                    val productToBagData = AddToBagRequest(
                        productId = bouquet.id,
                        count = product.productWithCount.count,
                        decorationId = bouquet.decoration.id,
                        tableId = bouquet.table.id,
                        postcard = bouquet.postcard
                    )
                    userApiService.addToBag(productToBagData)
                } else {
                    val productToBagData = AddToBagRequest(
                        productId = product.productWithCount.product.id,
                        count = product.productWithCount.count
                    )
                    userApiService.addToBag(productToBagData)
                }

            }
            "flower" -> {
                if (product.productWithCount is FlowersWithDecoration) {
                    val productToBagData = AddToBagRequest(
                        productId = product.productWithCount.product.id,
                        count = product.productWithCount.count,
                        decorationId = product.productWithCount.decoration.id
                    )
                    userApiService.addToBag(productToBagData)
                } else {
                    val productToBagData = AddToBagRequest(
                        productId = product.productWithCount.product.id,
                        count = product.productWithCount.count
                    )
                    userApiService.addToBag(productToBagData)
                }
            }
            else -> {
                val productToBagData = AddToBagRequest(
                    productId = product.productWithCount.product.id,
                    count = product.productWithCount.count
                )
                userApiService.addToBag(productToBagData)
            }
        }
    }

    override fun addAuthorToBag(product: ProductWithCount): Flow<Response<Int>> = apiRequestFlow {
        val bouquet = product.product as Bouquet
        val addAuthorData = AddAuthorToBagRequest(
            flowers = bouquet.flowers,
            count = product.count,
            decorationId = bouquet.decoration.id,
            tableId = bouquet.table.id,
            postcard = bouquet.postcard
        )
        userApiService.addAuthorToBag(addAuthorData)
    }

    override fun removeProductFromBag(productId: Int, isAuthor: Boolean?) = apiRequestFlow {
        val removeData = RemoveFromBagRequest(
            productId = productId,
            isAuthor = isAuthor
        )
        userApiService.removeFromBag(removeData)
    }

    override fun isProductInFavourite(product: Product) = apiRequestFlow {
        userApiService.isProductInFavourite(product.id)
    }

    override fun addProductToFavourite(product: Product) = apiRequestFlow {
        val productIdRequest = ProductIdRequest(product.id)
        userApiService.addToFavourite(productIdRequest)
    }

    override fun removeProductFromFavourite(productId: Int) = apiRequestFlow {
        val productIdRequest = ProductIdRequest(productId)
        userApiService.removeFromFavourite(productIdRequest)
    }

    override fun getUsername() = apiRequestFlow {
        userApiService.getUsername()
    }

    override fun changeProductCount(
        product: ProductWithCount,
        count: Int,
        isAuthor: Boolean?
    ) = apiRequestFlow {
        val changeCountData = ChangeCountRequest(
            productId = product.product.id,
            count = count,
            isAuthor = isAuthor
        )
        userApiService.changeProductInBagCount(changeCountData)
    }

    override fun getFavouriteByUserId() = apiRequestFlow {
        userApiService.getFavourite()
    }

    override fun getUserMainInfo() = apiRequestFlow {
        userApiService.getUserMainInfo()
    }

    override fun changeUserMainInfo(userData: UserEditInfo) = apiRequestFlow {
        val data = UsernameRequest(
            username = userData.username
        )
        val dataPart = Gson().toJson(data).toRequestBody("application/json".toMediaTypeOrNull())

        val imageRequestBody = userData.image?.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imagePart = imageRequestBody?.let {
            MultipartBody.Part.createFormData("image", userData.image.name, imageRequestBody)
        }
        userApiService.updateUserInfo(dataPart, imagePart)
    }

    override fun updateProductInBag(
        productWithCount: ProductWithCount
    ) = apiRequestFlow {
        delay(2000)
        when (productWithCount.product.type) {
            "bouquet" -> {
                if (productWithCount.product is Bouquet) {
                    val bouquet = productWithCount.product as Bouquet
                    val updateProductInBagData = AddToBagRequest(
                        productId = bouquet.id,
                        count = productWithCount.count,
                        decorationId = bouquet.decoration.id,
                        tableId = bouquet.table.id,
                        postcard = bouquet.postcard
                    )
                    userApiService.updateInBag(updateProductInBagData)
                } else {
                    val updateProductInBagData = AddToBagRequest(
                        productId = productWithCount.product.id,
                        count = productWithCount.count
                    )
                    userApiService.updateInBag(updateProductInBagData)
                }

            }
            "flower" -> {
                if (productWithCount is FlowersWithDecoration) {
                    val updateProductInBagData = AddToBagRequest(
                        productId = productWithCount.product.id,
                        count = productWithCount.count,
                        decorationId = productWithCount.decoration.id
                    )
                    userApiService.updateInBag(updateProductInBagData)
                } else {
                    val updateProductInBagData = AddToBagRequest(
                        productId = productWithCount.product.id,
                        count = productWithCount.count
                    )
                    userApiService.updateInBag(updateProductInBagData)
                }
            }
            else -> {
                val updateProductInBagData = AddToBagRequest(
                    productId = productWithCount.product.id,
                    count = productWithCount.count
                )
                userApiService.updateInBag(updateProductInBagData)
            }
        }
    }

    override fun updateAuthorBouquetInBag(
        productWithCount: ProductWithCount
    ) = apiRequestFlow {
        val bouquet = productWithCount.product as Bouquet
        val updateProductInBagData = AddAuthorToBagRequest(
            productId = bouquet.id,
            flowers = bouquet.flowers,
            count = productWithCount.count,
            decorationId = bouquet.decoration.id,
            tableId = bouquet.table.id,
            postcard = bouquet.postcard
        )
        userApiService.updateAuthorInBag(updateProductInBagData)
    }

    override fun deleteAccount() = apiRequestFlow {
        userApiService.deleteAccount()
    }

    override fun makeOrder(orderData: User.Order) = apiRequestFlow {
        val order = OrderRequest(
            date = orderData.date,
            phone = orderData.phone,
            address = orderData.address,
            fullname = orderData.fullname,
            promocodeId = orderData.promocodeId,
            summ = orderData.summ
        )
        userApiService.makeOrder(order)
    }

    override fun getOrderHistory(): Flow<Response<List<User.Order>>> = apiRequestFlow {
        userApiService.getOrderHistory()
    }

    override fun getOrderById(id: Int): Flow<Response<OrderResponse>> = apiRequestFlow {
        userApiService.getOrderById(id)
    }

    override fun usePromocode(promo: String): Flow<Response<Promocode>> = apiRequestFlow {
        val data = PromocodeRequest(
            promo = promo
        )
        userApiService.usePromocode(data)
    }
}