package com.example.flowershop.domain.model

import android.util.Log
import com.example.flowershop.util.Constants.IMAGES_URL
import com.google.gson.annotations.JsonAdapter
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import javax.annotation.Nullable


open class Product(
    open var id: Int = -1,
    open val price: Int = 0,
    open val rating: Rating = Rating(),
    open val image: Image = Image(""),
    open val name: String = "",
    open val description: String = "",
    open val categoriesIds: List<Int> = emptyList(),
    open val type : String = "product"
)

open class ProductWithCount(
    open val product: Product,
    open var count: Int = 1,
)

class FlowersWithDecoration(
    override val product : Flower,
    override var count: Int,
    var decoration : Decoration
) : ProductWithCount(product)

class ProductInBag(
    val productWithCount : ProductWithCount,
) {
    val totalPrice : Int
        get() = if (productWithCount is FlowersWithDecoration) {
            (productWithCount.product.price * productWithCount.count + productWithCount.decoration.price)
        }
        else if (productWithCount.product is Bouquet){
            (productWithCount.product as Bouquet).totalPrice * productWithCount.count
        } else {
            productWithCount.product.price * productWithCount.count
        }
}

data class Category(
    val id: Int,
    val name: String,
    val image: Image
)