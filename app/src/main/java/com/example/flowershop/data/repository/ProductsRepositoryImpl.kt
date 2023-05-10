package com.example.flowershop.data.repository

import com.example.flowershop.data.helpers.apiRequestFlow
import com.example.flowershop.data.network.ProductsApiService
import com.example.flowershop.domain.repository.ProductsRepository
import com.example.flowershop.presentation.model.SearchConditions
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(
    private val productsApiService: ProductsApiService
) : ProductsRepository{
    override fun getPopularProducts() = apiRequestFlow {
        productsApiService.getPopularProducts()
    }

    override fun getNewProducts() = apiRequestFlow {
        productsApiService.getNewProducts()
    }

    override fun getPromocodes() = apiRequestFlow {
        productsApiService.getPromocodes()
    }

    override fun getCategories() = apiRequestFlow {
        productsApiService.getCategories()
    }

    override fun getAllProducts() = apiRequestFlow {
        productsApiService.getAllProducts()
    }

    override fun getProductsByCategory(categoryId: Int) = apiRequestFlow {
        productsApiService.getProductsByCategories(categoryId)
    }

    override fun getProductById(productId: Int, type : String) = apiRequestFlow {
        productsApiService.getProductById(productId)
    }

    override fun getDecorations() = apiRequestFlow {
        productsApiService.getDecorations()
    }

    override fun getTables() = apiRequestFlow {
        productsApiService.getTables()
    }

    override fun getFlowers(searchConditions: SearchConditions) = apiRequestFlow {
        productsApiService.getFlowers(
            minPrice = searchConditions.minPrice,
            maxPrice = searchConditions.maxPrice,
            sorts = searchConditions.include,
            search = searchConditions.search,
            sort = searchConditions.sortCriteria?.value
        )
    }

    override fun getProducts(searchConditions: SearchConditions) = apiRequestFlow {
        productsApiService.getProducts(
            categoryId = searchConditions.categoryId,
            minPrice = searchConditions.minPrice,
            maxPrice = searchConditions.maxPrice,
            sorts = searchConditions.include,
            size = searchConditions.bouquetSize?.size,
            search = searchConditions.search,
            sort = searchConditions.sortCriteria?.value
        )
    }
}