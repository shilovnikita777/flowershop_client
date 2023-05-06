package com.example.flowershop.data.repository.test

import android.util.Log
import com.example.flowershop.data.TestData
import com.example.flowershop.domain.model.*
import com.example.flowershop.domain.repository.UserRepository
import com.example.flowershop.util.Constants.AUTHOR_BOUQUET_ID
import com.example.flowershop.data.helpers.Response
import com.example.flowershop.data.model.Response.UserMainInfoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class TestUserRepositoryImpl @Inject constructor(
    private val data: TestData
): UserRepository {


    override fun getBagByUserId(id: Int): Flow<Response<List<ProductWithCount>>> {
        TODO("Not yet implemented")
    }

    override fun updateAuthorBouquetInBag(
        productWithCount: ProductWithCount,
        userId: Int
    ): Flow<Response<Boolean>> {
        TODO("Not yet implemented")
    }

    //    override fun getBagByUserId(id: Int): Flow<Response<List<ProductWithCount>>> = flow {
//        emit(Response.Loading)
//
//        try {
//            coroutineScope {
//                async {
//                    delay(1000)
//                }.await()
//                val userBag = data.getBagByUserId(id)
//                if (userBag != null) {
////                    userBag.total = 0
////                    userBag.products.forEach {
////                        userBag.total += it.product.price
////                    }
//                    emit(Response.Success(userBag))
//                } else {
//                    emit(Response.Error("No such user"))
//                }
//            }
//        } catch(e: Exception) {
//            emit(Response.Error(e.localizedMessage ?: "Error"))
//        }
//    }.flowOn(Dispatchers.IO)

    override fun addAuthorToBag(product: ProductWithCount): Flow<Response<Int>> {
        TODO("Not yet implemented")
    }

    override fun addProductToBag(product: ProductInBag, userId: Int): Flow<Response<Boolean>> = flow {
        emit(Response.Loading)

        try {
            coroutineScope {
                async {
                    delay(1000)
                }.await()
                //val userBag = data.getBagByUserId(id)
                Log.d("xd",userId.toString())
                val user = data.users.find {
                    it.id == userId
                }
                if (user != null) {
                    if (product.productWithCount is FlowersWithDecoration) {
                        if (product.productWithCount.decoration.title.isEmpty()) {
                            product.productWithCount.decoration = data.decorations.minBy {
                                it.price
                            }
                        }
                    } else if (product.productWithCount.product is Bouquet) {
                        if (product.productWithCount.product.id == AUTHOR_BOUQUET_ID) {
                            if (user.bag.products.isNotEmpty()) {
                                val maxId = user.bag.products.maxOf {
                                    it.productWithCount.product.id
                                } - 1
                                product.productWithCount.product.id = maxId
                            }
                        }
                        if ((product.productWithCount.product as Bouquet).decoration.title.isEmpty()) {
                            (product.productWithCount.product as Bouquet).decoration = data.decorations.minBy {
                                it.price
                            }
                        }
                        if ((product.productWithCount.product as Bouquet).table.text.isEmpty()) {
                            (product.productWithCount.product as Bouquet).table = data.tables.minBy {
                                it.price
                            }
                        }
                    }
                    user.bag.products.add(product)
                    Log.d("xd1",product.productWithCount.toString())
                    if (product.productWithCount is FlowersWithDecoration) {
                        Log.d("xd1",product.productWithCount.decoration.title)
                    }
                    //Log.d("xd1",user.bag.products.last())
                    emit(Response.Success(true))
                } else {
                    emit(Response.Error("No such user"))
                }
            }
        } catch(e: Exception) {
            emit(Response.Error(e.localizedMessage ?: "Error"))
        }
    }.flowOn(Dispatchers.IO)

    //    override fun removeProductFromBag(
//        productId: Int,
//        isAuthor: Boolean?
//    ): Flow<Response<Boolean>> = flow {
//        emit(Response.Loading)
//
//        try {
//            coroutineScope {
//                async {
//                    delay(1000)
//                }.await()
//                //val userBag = data.getBagByUserId(id)
//                Log.d("xd",userId.toString())
//                val user = data.users.find {
//                    it.id == userId
//                }
//                if (user != null) {
//                    val element = user.bag.products.find {
//                        it.productWithCount.product.id == productId
//                    }
//                    if (element != null) {
//                        user.bag.products.remove(element)
//                        emit(Response.Success(true))
//                    } else {
//                        emit(Response.Error("No such product"))
//                    }
//                } else {
//                    emit(Response.Error("No such user"))
//                }
//            }
//        } catch(e: Exception) {
//            emit(Response.Error(e.localizedMessage ?: "Error"))
//        }
//    }.flowOn(Dispatchers.IO)
    override fun removeProductFromBag(productId: Int, isAuthor: Boolean?): Flow<Response<Boolean>> {
        TODO("Not yet implemented")
    }

    //    override fun isProductInBag(product: Product): Flow<Response<Boolean>> = flow {
//        emit(Response.Loading)
//
//        try {
//            coroutineScope {
//                async {
//                    delay(3000)
//                }.await()
//                //val userBag = data.getBagByUserId(id)
//                Log.d("xd",userId.toString())
//                val user = data.users.find {
//                    Log.d("xd5",it.id.toString() + " " + userId.toString())
//                    it.id == userId
//                }
//                if (user != null) {
//                    val element = user.bag.products.find {
//                        it.productWithCount.product.id == product.id
//                    }
//                    Log.d("xd","Bag size:" + user.bag.products.size.toString())
//                    if (element != null) {
//                        emit(Response.Success(true))
//                    } else {
//                        emit(Response.Success(false))
//                    }
//                } else {
//                    emit(Response.Error("No such user"))
//                }
//            }
//        } catch(e: Exception) {
//            emit(Response.Error(e.localizedMessage ?: "Error"))
//        }
//    }.flowOn(Dispatchers.IO)
    override fun isProductInBag(product: Product): Flow<Response<Boolean>> {
        TODO("Not yet implemented")
    }

    override fun getAuthorBouquetById(id: Int): Flow<Response<ProductWithCount>> {
        TODO("Not yet implemented")
    }

    override fun isAuthorInBag(productId: Int): Flow<Response<Boolean>> {
        TODO("Not yet implemented")
    }

    override fun changeProductCount(
        product: ProductWithCount,
        count: Int,
        isAuthor: Boolean?
    ): Flow<Response<Boolean>> {
        TODO("Not yet implemented")
    }

    override fun isProductInFavourite(product: Product, userId: Int): Flow<Response<Boolean>> = flow {
        emit(Response.Loading)

        try {
            coroutineScope {
                async {
                    delay(1000)
                }.await()

                Log.d("xd",userId.toString())

                val user = data.users.find {
                    it.id == userId
                }
                if (user != null) {
                    val element = user.favourite.products.find {
                        it.id == product.id
                    }
                    Log.d("xd","Favourite size: " + user.favourite.products.size.toString())
                    if (element != null) {
                        emit(Response.Success(true))
                    } else {
                        emit(Response.Success(false))
                    }
                } else {
                    emit(Response.Error("No such user"))
                }
            }
        } catch(e: Exception) {
            emit(Response.Error(e.localizedMessage ?: "Error"))
        }
    }.flowOn(Dispatchers.IO)

    override fun addProductToFavourite(
        product: Product,
        userId: Int
    ): Flow<Response<Boolean>>  = flow {
        emit(Response.Loading)

        try {
            coroutineScope {
                async {
                    delay(1000)
                }.await()
                //val userBag = data.getBagByUserId(id)
                Log.d("xd",userId.toString())
                val user = data.users.find {
                    it.id == userId
                }
                if (user != null) {
                    user.favourite.products.add(product)
                    emit(Response.Success(true))
                } else {
                    emit(Response.Error("No such user"))
                }
            }
        } catch(e: Exception) {
            emit(Response.Error(e.localizedMessage ?: "Error"))
        }
    }.flowOn(Dispatchers.IO)

    override fun removeProductFromFavourite(
        productId: Int,
        userId: Int
    ): Flow<Response<Boolean>>  = flow {
        emit(Response.Loading)

        try {
            coroutineScope {
                async {
                    delay(1000)
                }.await()
                //val userBag = data.getBagByUserId(id)
                Log.d("xd",userId.toString() + " here")
                val user = data.users.find {
                    it.id == userId
                }
                if (user != null) {
                    val element = user.favourite.products.find {
                        it.id == productId
                    }
                    if (element != null) {
                        user.favourite.products.remove(element)
                        emit(Response.Success(true))
                    } else {
                        emit(Response.Error("No such product"))
                    }
                } else {
                    emit(Response.Error("No such user"))
                }
            }
        } catch(e: Exception) {
            emit(Response.Error(e.localizedMessage ?: "Error"))
        }
    }.flowOn(Dispatchers.IO)

    override fun getUsername(userId: Int): Flow<Response<String>> = flow {
        emit(Response.Loading)

        try {
            coroutineScope {
                async {
                    delay(1000)
                }.await()
                Log.d("xd",userId.toString() + " here")
                val user = data.users.find {
                    it.id == userId
                }
                if (user != null) {
                    emit(Response.Success(user.data.username))
                } else {
                    emit(Response.Error("No such user"))
                }
            }
        } catch(e: Exception) {
            emit(Response.Error(e.localizedMessage ?: "Error"))
        }
    }.flowOn(Dispatchers.IO)

    fun changeProductCount(
        product: ProductWithCount,
        userId: Int,
        count: Int
    ): Flow<Response<Boolean>> = flow {
        emit(Response.Loading)

        try {
            coroutineScope {
                async {
                    delay(1000)
                }.await()
                Log.d("xd",userId.toString() + " here")
                val user = data.users.find {
                    it.id == userId
                }
                if (user != null) {
                    val index = user.bag.products.indexOfFirst {
                        it.productWithCount.product.id == product.product.id
                    }
                    if (index != -1) {
                        user.bag.products[index].productWithCount.count += count
                        emit(Response.Success(true))
                    } else {
                        emit(Response.Error("No such product"))
                    }
                } else {
                    emit(Response.Error("No such user"))
                }
            }
        } catch(e: Exception) {
            emit(Response.Error(e.localizedMessage ?: "Error"))
        }
    }.flowOn(Dispatchers.IO)

    override fun getUserMainInfo(userId: Int): Flow<Response<UserMainInfoResponse>> {
        TODO("Not yet implemented")
    }

    //    override fun getUserMainInfo(userId: Int): Flow<Response<User.Data>> = flow {
//        emit(Response.Loading)
//
//        try {
//            coroutineScope {
//                async {
//                    delay(1000)
//                }.await()
//                Log.d("xd",userId.toString() + " here")
//                val userInDb = data.users.find {
//                    it.id == userId
//                }
//                if (userInDb != null) {
//                    val user = User.Data(
//                        username = userInDb.data.username,
//                        email = userInDb.data.email,
//                        image = userInDb.data.image
//                    )
//                    emit(Response.Success(user))
//                }
//                else {
//                    emit(Response.Error("No such user"))
//                }
//            }
//        } catch(e: Exception) {
//            emit(Response.Error(e.localizedMessage ?: "Error"))
//        }
//    }.flowOn(Dispatchers.IO)
    override fun getFavouriteByUserId(userId: Int): Flow<Response<MutableList<Product>>> {
        TODO("Not yet implemented")
    }

    //    override fun getFavouriteByUserId(userId: Int): Flow<Response<User.Favourite>> = flow {
//        emit(Response.Loading)
//
//        try {
//            coroutineScope {
//                async {
//                    delay(1000)
//                }.await()
//                val userFavourite = data.getFavouriteByUserId(userId)
//                if (userFavourite != null) {
//                    emit(Response.Success(userFavourite))
//                } else {
//                    emit(Response.Error("No such user"))
//                }
//            }
//        } catch(e: Exception) {
//            emit(Response.Error(e.localizedMessage ?: "Error"))
//        }
//    }.flowOn(Dispatchers.IO)

    override fun changeUserMainInfo(userId: Int, userData: User.Data): Flow<Response<Boolean>> = flow {
        emit(Response.Loading)

        try {
            coroutineScope {
                async {
                    delay(1000)
                }.await()
                val user = data.users.find {
                    it.id == userId
                }
                if (user != null) {
                    user.data = userData
                    emit(Response.Success(true))
                } else {
                    emit(Response.Error("Ошибка идентификации пользователя"))
                }
            }
        } catch(e: Exception) {
            emit(Response.Error(e.localizedMessage ?: "Error"))
        }
    }.flowOn(Dispatchers.IO)

    override fun updateProductInBag(
        productWithCount: ProductWithCount,
        userId: Int
    ): Flow<Response<Boolean>> = flow {
        emit(Response.Loading)

        try {
            coroutineScope {
                async {
                    delay(1000)
                }.await()
                //val userBag = data.getBagByUserId(id)
                Log.d("xd",userId.toString())
                val user = data.users.find {
                    it.id == userId
                }
                if (user != null) {
                    val index = user.bag.products.indexOfFirst {
                        it.productWithCount.product.id == productWithCount.product.id
                    }
                    if (index != -1) {
                        user.bag.products[index] = ProductInBag(
                            productWithCount = productWithCount
                        )
                        emit(Response.Success(true))
                    } else {
                        emit(Response.Error("No such product"))
                    }
                } else {
                    emit(Response.Error("No such user"))
                }
            }
        } catch(e: Exception) {
            emit(Response.Error(e.localizedMessage ?: "Error"))
        }
    }.flowOn(Dispatchers.IO)
}