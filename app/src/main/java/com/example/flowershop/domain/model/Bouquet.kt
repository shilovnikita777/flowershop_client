package com.example.flowershop.domain.model

import com.example.flowershop.presentation.model.BouquetSize

data class Bouquet(
    override var id: Int = -1,
    override val price: Int = 0,
    override val image: Image = Image(""),
    override val name: String = "",
    override val description: String = "",
    override val categoriesIds: List<Int> = emptyList(),
    override val type : String = "bouquet",
    val flowers: List<ProductWithCount> = emptyList(),
    val size : BouquetSize = BouquetSize.Small,
    var decoration: Decoration = Decoration(),
    var table: Table = Table(),
    var postcard: String = ""
) : Product(id,price,image,name, description,categoriesIds,type) {
    val totalPrice : Int
        get() = price + decoration.price + table.price
}