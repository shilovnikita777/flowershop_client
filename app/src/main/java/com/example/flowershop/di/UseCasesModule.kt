package com.example.flowershop.di

import com.example.flowershop.data.TestData
import com.example.flowershop.domain.repository.AuthenticationRepository
import com.example.flowershop.domain.repository.ProductsRepository
import com.example.flowershop.domain.repository.UserRepository
import com.example.flowershop.domain.use_cases.AuthenticationUseCases.*
import com.example.flowershop.domain.use_cases.ProductsUseCases.*
import com.example.flowershop.domain.use_cases.UserUseCases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCasesModule {

//    @Singleton
//    @Provides
//    fun provideFirebaseAuthentication(): FirebaseAuth = FirebaseAuth.getInstance()
//
//    @Singleton
//    @Provides
//    fun provideAuthenticationRepository(
//        firebaseAuth: FirebaseAuth,
//        data : TestData
//    ) : AuthenticationRepository = FirebaseAuthenticationRepository(firebaseAuth,data)

    @Singleton
    @Provides
    fun provideAuthorizationUseCases(
        repository : AuthenticationRepository
    ) = AuthenticationUseCases(
        signInUseCase = SignInUseCase(repository),
        signUpUseCase = SignUpUseCase(repository),
        isUserAuthenticatedUseCase = IsUserAuthenticatedUseCase(repository),
        logoutUseCase = LogoutUseCase(repository)
    )

    @Singleton
    @Provides
    fun provideTestData() : TestData = TestData()

//    @Singleton
//    @Provides
//    fun provideProductsRepository(
//        data: TestData
//    ) : ProductsRepository = TestProductsRepositoryImpl(data)

    @Singleton
    @Provides
    fun provideProductsUseCases(
        repository : ProductsRepository
    ) = ProductsUseCases(
        getAllProductsUseCase = GetAllProductsUseCase(repository),
        getCategoriesUseCase = GetCategoriesUseCase(repository),
        getNewProductsUseCase = GetNewProductsUseCase(repository),
        getPopularProductsUseCase = GetPopularProductsUseCase(repository),
        getPromocodesUseCase = GetPromocodesUseCase(repository),
        getProductsByCategoryUseCase = GetProductsByCategoryUseCase(repository),
        getProductByIdUseCase = GetProductByIdUseCase(repository),
        getDecorationsUseCase = GetDecorationsUseCase(repository),
        getTablesUseCase = GetTablesUseCase(repository),
        getFlowersUseCase = GetFlowersUseCase(repository),
        getProductsUseCase = GetProductsUseCase(repository),
        getSortsUseCase = GetSortsUseCase(repository)
    )

//    @Singleton
//    @Provides
//    fun provideUserRepository(
//        data: TestData
//    ) : UserRepository = TestUserRepositoryImpl(data)

    @Singleton
    @Provides
    fun provideUserUseCases(
        repository : UserRepository
    ) = UserUseCases(
        getBagByUserIdUseCase = GetBagByUserIdUseCase(repository),
        addProductToBagUseCase = AddProductToBagUseCase(repository),
        removeProductFromBagUseCase = RemoveProductFromBagUseCase(repository),
        isProductInBagUseCase = IsProductInBagUseCase(repository),
        addProductToFavouriteUseCase = AddProductToFavouriteUseCase(repository),
        removeProductFromFavouriteUseCase = RemoveProductFromFavouriteUseCase(repository),
        isProductInFavouriteUseCase = IsProductInFavouriteUseCase(repository),
        getUsernameUseCase = GetUsernameUseCase(repository),
        changeProductCountUseCase = ChangeProductCountUseCase(repository),
        getUserMainInfoUseCase = GetUserMainInfoUseCase(repository),
        getFavouriteByUserIdUseCase = GetFavouriteByUserIdUseCase(repository),
        changeUserMainInfoUseCase = ChangeUserMainInfoUseCase(repository),
        updateProductInBagUseCase = UpdateProductInBagUseCase(repository),
        addAuthorToBagUseCase = AddAuthorToBagUseCase(repository),
        getAuthorBouquetByIdUseCase = GetAuthorBouquetByIdUseCase(repository),
        isAuthorBouquetInBagUseCase = IsAuthorBouquetInBagUseCase(repository),
        updateAuthorBouquetInBagUseCase = UpdateAuthorBouquetInBagUseCase(repository),
        deleteAccountUseCase = DeleteAccountUseCase(repository),
        makeOrderUseCase = MakeOrderUseCase(repository),
        getOrderHistoryUseCase = GetOrderHistoryUseCase(repository),
        getOrderByIdUseCase = GetOrderByIdUseCase(repository),
        usePromocodeUseCase = UsePromocodeUseCase(repository)
    )

//    @Singleton
//    @Provides
//    fun provideUserDatastore(
//        @ApplicationContext context: Context
//    ) : UserDatastore {
//        return UserDatastore(context)
//    }

//    @Singleton
//    @Provides
//    fun provideTokenManager(
//        @ApplicationContext context: Context
//    ) : TokenManager {
//        return TokenManager(context)
//    }
}