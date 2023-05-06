package com.example.flowershop.domain.model

import com.example.flowershop.util.Constants.SERVER_PATH

data class Image(
    private val relativePath: String
) {
    fun getPath() : String {
        return "$SERVER_URL$relativePath"
    }

    companion object {
        const val SERVER_URL = SERVER_PATH
    }
}