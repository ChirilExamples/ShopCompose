package com.example.shopcompose.data.remote

import javax.inject.Inject

class ShoppingRemoteDatasource @Inject constructor(private val shoppingService: ShoppingService){
    suspend fun getAllShoppings() =  shoppingService.getAllShoppings()
}
