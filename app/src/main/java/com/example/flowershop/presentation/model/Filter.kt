package com.example.flowershop.presentation.model

data class SearchConditions(
    val minPrice : Int? = null,
    val maxPrice : Int? = null,
    val include : List<String>? = null,
    val bouquetSize : BouquetSize? = null,
    val search : String? = null,
    val categoryId : Int? = null,
    val sortCriteria: SortCriteria? = null
)

data class ListItem(
    val id : Int,
    val title : String,
    val isSelected : Boolean = false
)

sealed class BouquetSize(
    val size : String
) {
    object Small : BouquetSize("small")

    object Medium : BouquetSize("medium")

    object Big : BouquetSize("big")

    object Large : BouquetSize("large")
}

sealed class SortCriteria(
    val value : String,
    val title : String
) {
    object PriceAscending : SortCriteria("priceasc", "По возрастанию цены")

    object PriceDescending : SortCriteria("pricedesc", "По убыванию цены")

    object New : SortCriteria("new", "Сначала новые")
}