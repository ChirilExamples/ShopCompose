package com.example.shopcompose.data.remote

import com.example.shopcompose.data.data_structure.ClothesItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ShoppingService {
    @GET("products")
    suspend fun getAllShoppings(): Response<List<ClothesItem>>

//    @GET("products/{id}") //  id supposed to be provided by click event
//    suspend fun getShoppingsDetails(@Path("id") id: Int): Response<ClothesItem>
}
