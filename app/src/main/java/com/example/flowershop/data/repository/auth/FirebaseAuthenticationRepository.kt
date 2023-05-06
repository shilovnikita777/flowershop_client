package com.example.flowershop.data.repository.auth

import android.util.Log
import com.example.flowershop.data.TestData
import com.example.flowershop.domain.model.User
import com.example.flowershop.domain.repository.AuthenticationRepository
import com.example.flowershop.util.Constants.NO_USER_CONSTANT
import com.example.flowershop.data.helpers.Response
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

//class FirebaseAuthenticationRepository @Inject constructor(
//    private val firebaseAuth: FirebaseAuth,
//    private val data: TestData
//) : AuthenticationRepository {
//
//    override fun isUserAuthenticated(): Int {
//        if (firebaseAuth.currentUser != null) {
//            //
//            val user = User(
//                id = 0,
//                data = User.Data(
//                    username = "торопыга",
//                    mail = "email@mail.ru",
//                    image = "https://sun9-77.userapi.com/impg/MOMtMHf2acGcCuUOy744AsnYAY8wrueNh9N-bQ/3QxQRp9bPHY.jpg?size=960x1280&quality=95&sign=6d6728aa68e2ff60c8a7c5e6498c7806&type=album"
//                )
//            )
//            data.addUser(user)
//            //
//            return 0
//        }
//        return NO_USER_CONSTANT
//    }
//
//    override fun signIn(email: String, password: String): Flow<Response<Int>> = flow {
//
//        emit(Response.Loading)
//        Log.d("xd", "${firebaseAuth.currentUser?.uid} - 2.")
//
//        try {
//            val status = firebaseAuth.signInWithEmailAndPassword(email,password).await()
//            val userFb = status.user
//            if (userFb != null) {
//                val id = 0
//                val user = User(
//                    id = id,
//                    data = User.Data(
//                        username = "торопыга",
//                        mail = email,
//                        image = "https://sun9-77.userapi.com/impg/MOMtMHf2acGcCuUOy744AsnYAY8wrueNh9N-bQ/3QxQRp9bPHY.jpg?size=960x1280&quality=95&sign=6d6728aa68e2ff60c8a7c5e6498c7806&type=album"
//                    )
//                )
//                data.addUser(user)
//                emit(Response.Success(data.users[0].id))
//            } else {
//                //Log.d("xd", "${firebaseAuth.currentUser?.uid} - 6.")
//                emit(Response.Error("Unexpected Error - 1"))
//            }
//        } catch(e: Exception) {
//            emit(Response.Error(e.localizedMessage ?:"Unexpected Error - 2"))
//        }
//    }
//
//    override fun signUp(email: String, username: String, password: String): Flow<Response<Int>> = flow {
//
//        emit(Response.Loading)
//        try {
//            val status = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
//            val userFb = status.user
//            if (userFb != null) {
//                val id = 0
//                val user = User(
//                    id = id,
//                    data = User.Data(
//                        username = username,
//                        mail = email,
//                        image = "https://sun9-77.userapi.com/impg/MOMtMHf2acGcCuUOy744AsnYAY8wrueNh9N-bQ/3QxQRp9bPHY.jpg?size=960x1280&quality=95&sign=6d6728aa68e2ff60c8a7c5e6498c7806&type=album"
//                    )
//                )
//                data.addUser(user)
//                emit(Response.Success(id))
//            } else {
//                emit(Response.Error("Unexpected Error"))
//            }
//        } catch(e: Exception) {
//            emit(Response.Error(e.localizedMessage ?: "Unexpected Error"))
//            //Log.d("xd", "$email : $password : $status - 2")
//        }
//    }
//}