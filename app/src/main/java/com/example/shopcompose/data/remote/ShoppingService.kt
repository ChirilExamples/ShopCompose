package com.example.shopcompose.data.remote

import com.example.shopcompose.data.data_structure.ClothesItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ShoppingService {
    @GET("products")
    suspend fun getAllShoppings(): Response<List<ClothesItem>>

//    @GET("products/{id}") //  id in {} because it is supposed to be provided by user
//    suspend fun getShoppingsDetails(@Path("id") id: Int): Response<ClothesItem>
//
//    @GET("products/{price}")
//    suspend fun getPriceSort(@Path("price") price: Double): Response<List<ClothesItem>>
//
//    @GET("products/{category}")
//    suspend fun getCategorySort(@Path("category") category: String): Response<List<ClothesItem>>
}
