package com.example.flowershop.domain.use_cases.UserUseCases

import com.example.flowershop.presentation.model.UserEditInfo
import com.example.flowershop.domain.repository.UserRepository
import javax.inject.Inject

class ChangeUserMainInfoUseCase @Inject constructor(
    private val repository: UserRepository
){
    operator fun invoke(userData : UserEditInfo) = repository.changeUserMainInfo(userData)
}