package com.example.flowershop.presentation.screens.ProfilePageScreens

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flowershop.data.UserDatastore
import com.example.flowershop.domain.model.User
import com.example.flowershop.domain.use_cases.UserUseCases.UserUseCases
import com.example.flowershop.util.Constants.NO_USER_CONSTANT
import com.example.flowershop.data.helpers.Response
import com.example.flowershop.data.model.Response.UserMainInfoResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userUseCases: UserUseCases,
    private val userDatastore: UserDatastore
) : ViewModel() {

    private val _userMainInfoResponse = mutableStateOf<Response<UserMainInfoResponse>>(Response.Loading)
    val userMainInfoResponse : State<Response<UserMainInfoResponse>> = _userMainInfoResponse

    private var _userId = -1

    var isDialogShown by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            userDatastore.getUserId.collect {
                if (it != NO_USER_CONSTANT) {
                    _userId = it
                    getUserMainInfo()
                }
            }
        }
    }

    private fun getUserMainInfo() {
        viewModelScope.launch {
            userUseCases.getUserMainInfoUseCase(_userId).collect {
                _userMainInfoResponse.value = it
            }
        }
    }

    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            //userDatastore.deleteUserId()
            //FirebaseAuth.getInstance().signOut()
            onSuccess()
        }
    }

    fun onExitClicked() {
        isDialogShown = true
    }

    fun onDismissDialog() {
        isDialogShown = false
    }
}