package com.example.flowershop.presentation.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.flowershop.domain.model.ProductInBag
import com.example.flowershop.data.helpers.Response

class UserBagState {
    var products: SnapshotStateList<Triple<ProductInBag, MutableState<Response<Boolean>>, MutableState<Response<Boolean>>>> = mutableStateListOf()
    val total: MutableState<Int> = mutableStateOf(calculateTotal())

    fun calculateTotal(): Int {
        return products.sumOf {
            it.first.totalPrice
            //it.first.productWithCount.product.totalPrice * it.first.productWithCount.count
        }
    }
}