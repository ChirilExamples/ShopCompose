package com.example.shopcompose.data.remote

import javax.inject.Inject

class ShoppingRemoteDatasource @Inject constructor(private val shoppingService: ShoppingService){
    suspend fun getAllShoppings() =  shoppingService.getAllShoppings()
//    suspend fun getShoppingsDetails(id: Int) = getResult { shoppingService.getShoppingsDetails(id) }
//    suspend fun getPriceSort(price: Double) = getResult { shoppingService.getPriceSort(price) }
//    suspend fun getCategorySort(category: String) =
//        getResult { shoppingService.getCategorySort(category) }
}
