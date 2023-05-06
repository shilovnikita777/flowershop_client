package com.example.flowershop.domain.mapper

abstract class Mapper<From,To> {
    abstract fun map(from: From): To
}