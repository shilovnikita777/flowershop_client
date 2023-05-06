package com.example.flowershop.di

import com.example.flowershop.domain.mapper.Mapper
import com.example.flowershop.domain.model.Promocode
import com.example.flowershop.presentation.mapper.PromocodeMapper
import com.example.flowershop.presentation.model.PromocodeUI
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MapperModule {

    @Binds
    abstract fun bindPromocodeMapper(
        mapper: PromocodeMapper
    ) : Mapper<Promocode,PromocodeUI>

}