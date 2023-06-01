package com.example.flowershop.di

import com.example.flowershop.data.network.AuthApiService
import com.example.flowershop.data.network.ProductsApiService
import com.example.flowershop.data.network.UserApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Singleton
    @Provides
    fun provideAuthApiService(retrofit: Retrofit.Builder): AuthApiService =
        retrofit
            .build()
            .create(AuthApiService::class.java)

    @Singleton
    @Provides
    fun provideUserApiService(
        okHttpClient: OkHttpClient,
        retrofit: Retrofit.Builder
    ): UserApiService =
        retrofit
            .client(okHttpClient)
            .build()
            .create(UserApiService::class.java)

    @Singleton
    @Provides
    fun provideProductsApiService(
        okHttpClient: OkHttpClient,
        retrofit: Retrofit.Builder
    ): ProductsApiService =
        retrofit
            .client(okHttpClient)
            .build()
            .create(ProductsApiService::class.java)
}