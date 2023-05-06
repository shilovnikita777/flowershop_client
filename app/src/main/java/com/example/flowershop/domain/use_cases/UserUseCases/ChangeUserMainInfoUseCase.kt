package com.example.flowershop.domain.use_cases.UserUseCases

import com.example.flowershop.domain.model.User
import com.example.flowershop.domain.repository.UserRepository
import javax.inject.Inject

class ChangeUserMainInfoUseCase @Inject constructor(
    private val repository: UserRepository
){
    operator fun invoke(id: Int, userData: User.Data) = repository.changeUserMainInfo(id, userData)
}