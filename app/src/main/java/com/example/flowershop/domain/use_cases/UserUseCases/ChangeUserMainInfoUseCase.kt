package com.example.flowershop.domain.use_cases.UserUseCases

import android.net.Uri
import com.example.flowershop.domain.model.User
import com.example.flowershop.domain.repository.UserRepository
import javax.inject.Inject

class ChangeUserMainInfoUseCase @Inject constructor(
    private val repository: UserRepository
){
    operator fun invoke(username : String, image: String) = repository.changeUserMainInfo(username, image)
}