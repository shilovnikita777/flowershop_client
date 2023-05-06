package com.example.flowershop.di

import com.example.flowershop.data.TestData
import com.example.flowershop.data.repository.ProductsRepositoryImpl
import com.example.flowershop.data.repository.UserRepositoryImpl
import com.example.flowershop.data.repository.authApi.AuthRepositoryImpl
import com.example.flowershop.data.repository.test.TestProductsRepositoryImpl
import com.example.flowershop.domain.repository.AuthenticationRepository
import com.example.flowershop.domain.repository.ProductsRepository
import com.example.flowershop.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindAuthRepository(
        repository : AuthRepositoryImpl
    ) : AuthenticationRepository

    @Singleton
    @Binds
    abstract fun provideProductsRepository(
        repository: ProductsRepositoryImpl
    ) : ProductsRepository

    @Singleton
    @Binds
    abstract fun provideUserRepository(
        repository: UserRepositoryImpl
    ) : UserRepository
}