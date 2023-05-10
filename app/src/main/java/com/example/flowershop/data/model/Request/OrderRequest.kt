package com.example.flowershop.data.model.Request

import java.time.LocalDate
import java.util.*

class OrderRequest(
    val date : LocalDate,
    val phone : String,
    val address : String,
    val fullname : String
)