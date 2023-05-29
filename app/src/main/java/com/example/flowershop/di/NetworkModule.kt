package com.example.flowershop.di

import com.example.flowershop.data.helpers.AuthInterceptor
import com.example.flowershop.data.helpers.jsondeserializer.*
import com.example.flowershop.data.helpers.jsonserializer.FlowerSerializer
import com.example.flowershop.domain.model.*
import com.example.flowershop.util.Constants.API_URL
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.time.LocalDate
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofitBuilder(
        moshiConverterFactory: MoshiConverterFactory,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit.Builder =
        Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(gsonConverterFactory)

    @Singleton
    @Provides
    fun provideGson() : Gson =
        GsonBuilder()
            .registerTypeAdapter(Flower::class.java, FlowerDeserializer())
            .registerTypeAdapter(Bouquet::class.java, BouquetDeserializer())
            .registerTypeAdapter(ProductWithCount::class.java, ProductWithCountDeserializer())
            .registerTypeAdapter(Image::class.java, ImageDeserializer())
            .registerTypeAdapter(Flower::class.java, FlowerSerializer())
            .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter().nullSafe())
            .create()

    @Singleton
    @Provides
    fun provideGsonConverterFactory(gson : Gson) : GsonConverterFactory =
        GsonConverterFactory.create(gson)

    @Singleton
    @Provides
    fun provideMoshi() : Moshi =
        Moshi.Builder()
            .build()

    @Singleton
    @Provides
    fun provideMoshiConverterFactory(moshi : Moshi) : MoshiConverterFactory =
        MoshiConverterFactory.create(moshi)
}