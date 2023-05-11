package com.example.flowershop.data.repository

import com.example.flowershop.data.helpers.Response
import com.example.flowershop.data.helpers.apiRequestFlow
import com.example.flowershop.data.model.Request.*
import com.example.flowershop.data.network.UserApiService
import com.example.flowershop.domain.model.*
import com.example.flowershop.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApiService : UserApiService
) : UserRepository {
    override fun getBagByUserId(id: Int): Flow<Response<List<ProductWithCount>>> = apiRequestFlow {
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

    override fun addProductToBag(product: ProductInBag, userId: Int) = apiRequestFlow {
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
                //Log.d("xd7",product.productWithCount)
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

    override fun isProductInFavourite(product: Product, userId: Int) = apiRequestFlow {
        userApiService.isProductInFavourite(product.id)
    }

    override fun addProductToFavourite(product: Product, userId: Int) = apiRequestFlow {
        val productIdRequest = ProductIdRequest(product.id)
        userApiService.addToFavourite(productIdRequest)
    }

    override fun removeProductFromFavourite(productId: Int, userId: Int) = apiRequestFlow {
        val productIdRequest = ProductIdRequest(productId)
        userApiService.removeFromFavourite(productIdRequest)
    }

    override fun getUsername(userId: Int) = apiRequestFlow {
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

    override fun getFavouriteByUserId(userId: Int) = apiRequestFlow {
        userApiService.getFavourite()
    }

    override fun getUserMainInfo(userId: Int) = apiRequestFlow {
        userApiService.getUserMainInfo()
    }

    override fun changeUserMainInfo(userId: Int, userData: User.Data): Flow<Response<Boolean>> {
        TODO("Not yet implemented")
    }

    override fun updateProductInBag(
        productWithCount: ProductWithCount,
        userId: Int
    ) = apiRequestFlow {
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
        productWithCount: ProductWithCount,
        userId: Int
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
            fullname = orderData.fullname
        )
        userApiService.makeOrder(order)
    }

    override fun getOrderHistory(): Flow<Response<List<User.Order>>> = apiRequestFlow {
        userApiService.getOrderHistory()
    }

    override fun getOrderById(id: Int) = apiRequestFlow {
        userApiService.getOrderById(id)
    }
}