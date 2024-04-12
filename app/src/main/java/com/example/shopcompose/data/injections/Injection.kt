package com.example.shopcompose.data.injections

import android.content.Context
import com.example.shopcompose.data.remote.ShoppingRemoteDatasource
import com.example.shopcompose.data.remote.ShoppingService
import com.example.shopcompose.data.repository.Repository
import com.example.shopcompose.domain.RepositoryImpl
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class Injection {

    @Provides
    fun provideGSON(): Gson = GsonBuilder().create()

    @Singleton
    @Provides
    fun provideAPIService(gson: Gson): Retrofit = Retrofit.Builder()
        .baseUrl("https://fakestoreapi.com/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    fun provideShoppingsService(retrofit: Retrofit): ShoppingService =
        retrofit.create(ShoppingService::class.java)

    @Singleton
    @Provides
    fun provideRocketRemoteDatasource(shoppingService: ShoppingService) =
        ShoppingRemoteDatasource(shoppingService)

    @Singleton
    @Provides
    fun provideRepository(remoteDatasource: ShoppingService): Repository = RepositoryImpl(remoteDatasource)

}
