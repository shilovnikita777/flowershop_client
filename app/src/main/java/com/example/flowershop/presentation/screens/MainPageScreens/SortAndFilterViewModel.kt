package com.example.flowershop.presentation.screens.MainPageScreens

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.flowershop.presentation.model.BouquetSize
import com.example.flowershop.presentation.model.SearchConditions
import com.example.flowershop.presentation.model.ListItem
import com.example.flowershop.presentation.model.SortCriteria
import com.example.flowershop.util.Constants
import com.example.flowershop.util.Constants.NO_CATEGORY_CONSTANT
import kotlinx.coroutines.Job

open class SortAndFilterViewModel (
) : ViewModel() {
    var isFilterDialogShown by mutableStateOf(false)
        private set

    var isSortDialogShown by mutableStateOf(false)
        private set

    var sorts = mutableStateOf(listOf(ListItem("Розы"),
        ListItem("Тюльпаны"),
        ListItem("Ромашки"),
        ListItem("Пионы")
    ))
        private set

    var sizes = listOf("Маленький", "Средний", "Большой", "Огромный")
        private set

    var sortCriteria = listOf("По возрастанию цены", "По убыванию цены", "Сначала новые")
        private set

    var savedSearchConditions : SearchConditions? = null

    protected val _searchConditions = mutableStateOf(SearchConditions())
    val searchConditions : State<SearchConditions> = _searchConditions

    var searchJob = mutableStateOf<Job?>(null)

    fun onFilterClicked() {
        isFilterDialogShown = true
        savedSearchConditions = _searchConditions.value
    }

    fun onFilterDismiss() {
        isFilterDialogShown = false
    }

    fun haveConditionsChanged() : Boolean {
        return savedSearchConditions != _searchConditions.value
    }

    fun onSortClicked() {
        isSortDialogShown = true
        savedSearchConditions = _searchConditions.value
    }

    fun onSortDismiss() {
        isSortDialogShown = false
    }

    fun updateSortsList(newSorts : List<ListItem>) {
        sorts.value = newSorts
        val include = newSorts.filter { it.isSelected }.map { it.title }
        _searchConditions.value = _searchConditions.value.copy(
            include = include.ifEmpty { null }
        )
    }

    fun changeMinPrice(price : Int?) {
        _searchConditions.value = _searchConditions.value.copy(
            minPrice = price
        )
    }

    fun changeMaxPrice(price : Int?) {
        _searchConditions.value = _searchConditions.value.copy(
            maxPrice = price
        )
    }

    fun changePrice(numberText : String) : Int? {
        var price = numberText.filter {
            it.isDigit()
        }

        if (price == "")
            return null

        if (price.first() == '0' && price.length > 1) {
            price = numberText.trimStart('0')
        }

        if (price.toInt() > Constants.MAX_PRODUCT_PRICE) {
            price = numberText.dropLast(1)
        }

        return price.toInt()
    }

    fun changeSize(size : String) {
        val bouquetSize = when(size) {
            "Маленький" -> {
                BouquetSize.Small
            }
            "Средний" -> {
                BouquetSize.Medium
            }
            "Большой" -> {
                BouquetSize.Big
            }
            "Огромный"-> {
                BouquetSize.Large
            }
            else -> {
                null
            }
        }
        _searchConditions.value = _searchConditions.value.copy(
            bouquetSize = bouquetSize
        )
    }

    fun isSizeChosen(size : String) : Boolean {
        when(size) {
            "Маленький" -> {
                return _searchConditions.value.bouquetSize is BouquetSize.Small
            }
            "Средний" -> {
                return _searchConditions.value.bouquetSize is BouquetSize.Medium
            }
            "Большой" -> {
                return _searchConditions.value.bouquetSize is BouquetSize.Big
            }
            "Огромный"-> {
                return _searchConditions.value.bouquetSize is BouquetSize.Large
            }
            else -> {
                return false
            }
        }
    }

    fun onSearchInput(text : String) {
        _searchConditions.value = _searchConditions.value.copy(
            search = if (text == "") null else text
        )
    }

    fun setCategory(categoryId : Int) {
        _searchConditions.value = _searchConditions.value.copy(
            categoryId = if (categoryId == NO_CATEGORY_CONSTANT) null else categoryId
        )
    }

    fun changeSortCriteria(criteria : String) {
        val sortCriteria = when(criteria) {
            "По возрастанию цены" -> {
                SortCriteria.PriceAscending
            }
            "По убыванию цены" -> {
                SortCriteria.PriceDescending
            }
            "Сначала новые" -> {
                SortCriteria.New
            }
            else -> {
                null
            }
        }
        _searchConditions.value = _searchConditions.value.copy(
            sortCriteria = sortCriteria
        )
    }

    fun isSortCriteriaChosen(criteria : String) : Boolean {
        when(criteria) {
            "По возрастанию цены" -> {
                return _searchConditions.value.sortCriteria is SortCriteria.PriceAscending
            }
            "По убыванию цены"  -> {
                return _searchConditions.value.sortCriteria is SortCriteria.PriceDescending
            }
            "Сначала новые" -> {
                return _searchConditions.value.sortCriteria is SortCriteria.New
            }
            else -> {
                return false
            }
        }
    }

    fun clearFilters() {
        _searchConditions.value = _searchConditions.value.copy(
            minPrice = null,
            maxPrice = null,
            include = null,
            bouquetSize = null
        )
        sorts.value = sorts.value.map {
            it.copy(isSelected = false)
        }
    }

    fun clearSortCriteria() {
        _searchConditions.value = _searchConditions.value.copy(
            sortCriteria = null
        )
    }
}