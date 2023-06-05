package com.example.flowershop.data.repository.test

import com.example.flowershop.data.TestData
import com.example.flowershop.domain.model.*
import com.example.flowershop.domain.repository.ProductsRepository
import com.example.flowershop.data.helpers.Response
import com.example.flowershop.presentation.model.SearchConditions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class TestProductsRepositoryImpl @Inject constructor(
    private val data: TestData
): ProductsRepository {
    //    override fun getProductById(productId: Int, type: String): Flow<Response<Product>> {
//        TODO("Not yet implemented")
//    }
    override fun getSorts(): Flow<Response<List<Sort>>> {
        TODO("Not yet implemented")
    }

    override fun getPopularProducts(): Flow<Response<List<Product>>> = flow {
        emit(Response.Loading)

        try {
            coroutineScope {
                async {
                    delay(3000)
                }.await()
                val list = data.popularProducts.toList().subList(0,6)
                emit(Response.Success(list))
            }
        } catch(e: Exception) {
            emit(Response.Error(e.localizedMessage ?: "Error"))
        }
    }.flowOn(Dispatchers.IO)

    override fun getNewProducts(): Flow<Response<List<Product>>> = flow {
        emit(Response.Loading)

        try {
            coroutineScope {
                async {
                    delay(3000)
                }.await()
                val list = data.newProducts.toList().subList(0,6)
                emit(Response.Success(list))
            }
        } catch(e: Exception) {
            emit(Response.Error(e.localizedMessage ?: "Error"))
        }
    }.flowOn(Dispatchers.IO)

    override fun getPromocodes(): Flow<Response<List<Promocode>>> = flow {
        emit(Response.Loading)

        try {
            coroutineScope {
                async {
                    delay(3000)
                }.await()
                val list = data.promocodes.toList()
                emit(Response.Success(list))
            }
        } catch(e: Exception) {
            emit(Response.Error(e.localizedMessage ?: "Error"))
        }

    }.flowOn(Dispatchers.IO)

    override fun getCategories(): Flow<Response<List<Category>>> = flow {
        emit(Response.Loading)

        try {
            coroutineScope {
                async {
                    delay(1000)
                }.await()
                val list = data.categories.toList()
                emit(Response.Success(list))
            }
        } catch(e: Exception) {
            emit(Response.Error(e.localizedMessage ?: "Error"))
        }
    }.flowOn(Dispatchers.IO)

    override fun getAllProducts(): Flow<Response<List<Product>>> = flow {
        emit(Response.Loading)

        try {
            coroutineScope {
                async {
                    delay(1000)
                }.await()
                val list = data.products.toList()
                emit(Response.Success(list))
            }
        } catch(e: Exception) {
            emit(Response.Error(e.localizedMessage ?: "Error"))
        }
    }.flowOn(Dispatchers.IO)

    override fun getProductsByCategory(categoryId: Int): Flow<Response<List<Product>>>  = flow {
        emit(Response.Loading)

        try {
            coroutineScope {
                async {
                    delay(1000)
                }.await()
                val list = data.products.toList().filter { product ->
                    product.categoriesIds.any { category ->
                        category == categoryId
                    }
                }
                emit(Response.Success(list))
            }
        } catch(e: Exception) {
            emit(Response.Error(e.localizedMessage ?: "Error"))
        }
    }.flowOn(Dispatchers.IO)

    //    override fun getProductById(productId: Int, type: String): Flow<Response<ProductInBag>> = flow {
//
//        emit(Response.Loading)
//
//        try {
//            coroutineScope {
//                async {
//                    delay(1000)
//                }.await()
//                val user = data.users.find {
//                    //Log.d("xd5",it.id.toString() + " " + userId.toString())
//                    it.id == 0
//                }
//                if (user != null) {
//                    val productInBag = user.bag.products.find {
//                        productId == it.productWithCount.product.id
//                    }
//                    if (productInBag != null) {
//                        emit(Response.Success(productInBag))
//                    } else {
//                        val product = data.products.find {
//                            productId == it.id
//                        }
//                        if (product != null) {
//                            if (product is Product.Flower) {
//                                emit(
//                                    Response.Success(ProductInBag(
//                                    productWithCount = FlowersWithDecoration(
//                                        product = product,
//                                        count = 1,
//                                        decoration = data.decorations.first()
//                                    )
//                                )))
//                            } else {
//                                emit(
//                                    Response.Success(ProductInBag(
//                                    productWithCount = ProductWithCount(
//                                        product = product,
//                                        count = 1
//                                    )
//                                )))
//                            }
//                        } else {
//                            emit(Response.Error("No such product"))
//                        }
//                    }
//                } else {
//                    emit(Response.Error("No such user"))
//                }
//            }
//        } catch(e: Exception) {
//            emit(Response.Error(e.localizedMessage ?: "Error"))
//        }
//    }.flowOn(Dispatchers.IO)
    override fun getProductById(productId: Int, type: String): Flow<Response<ProductWithCount>> {
        TODO("Not yet implemented")
    }

    override fun getDecorations(): Flow<Response<List<Decoration>>> = flow {
        emit(Response.Loading)

        try {
            coroutineScope {
                async {
                    delay(4000)
                }.await()
                val list = data.decorations.toList()
                emit(Response.Success(list))
            }
        } catch(e: Exception) {
            emit(Response.Error(e.localizedMessage ?: "Error"))
        }
    }.flowOn(Dispatchers.IO)

    override fun getTables(): Flow<Response<List<Table>>> = flow {
        emit(Response.Loading)

        try {
            coroutineScope {
                async {
                    delay(4000)
                }.await()
                val list = data.tables.toList()
                emit(Response.Success(list))
            }
        } catch(e: Exception) {
            emit(Response.Error(e.localizedMessage ?: "Error"))
        }
    }.flowOn(Dispatchers.IO)

    override fun getFlowers(searchConditions: SearchConditions): Flow<Response<List<Flower>>> {
        TODO("Not yet implemented")
    }

    override fun getProducts(searchConditions: SearchConditions): Flow<Response<List<Product>>> {
        TODO("Not yet implemented")
    }
}