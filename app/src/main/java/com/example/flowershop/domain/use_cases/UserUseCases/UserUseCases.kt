package com.example.flowershop.domain.use_cases.UserUseCases

data class UserUseCases (
    val getBagByUserIdUseCase: GetBagByUserIdUseCase,
    val addProductToBagUseCase: AddProductToBagUseCase,
    val removeProductFromBagUseCase: RemoveProductFromBagUseCase,
    val isProductInBagUseCase: IsProductInBagUseCase,
    val addProductToFavouriteUseCase: AddProductToFavouriteUseCase,
    val removeProductFromFavouriteUseCase: RemoveProductFromFavouriteUseCase,
    val isProductInFavouriteUseCase: IsProductInFavouriteUseCase,
    val getUsernameUseCase: GetUsernameUseCase,
    val changeProductCountUseCase: ChangeProductCountUseCase,
    val getUserMainInfoUseCase: GetUserMainInfoUseCase,
    val getFavouriteByUserIdUseCase: GetFavouriteByUserIdUseCase,
    val changeUserMainInfoUseCase: ChangeUserMainInfoUseCase,
    val updateProductInBagUseCase: UpdateProductInBagUseCase,
    val addAuthorToBagUseCase: AddAuthorToBagUseCase,
    val getAuthorBouquetByIdUseCase: GetAuthorBouquetByIdUseCase,
    val isAuthorBouquetInBagUseCase: IsAuthorBouquetInBagUseCase,
    val updateAuthorBouquetInBagUseCase: UpdateAuthorBouquetInBagUseCase
)