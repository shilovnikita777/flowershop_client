package com.example.flowershop.data.model.Request

class ChangeCountRequest(
    val productId : Int,
    val count : Int,
    val isAuthor : Boolean?
)