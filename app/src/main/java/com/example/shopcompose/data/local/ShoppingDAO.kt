package com.example.shopcompose.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.shopcompose.data.data_structure.ClothesItem

@Dao
interface ShoppingDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(clothesItem: List<ClothesItem>)

    @Query("SELECT * FROM Clothes_items") //  Where category = 'jewelery'
    fun getAllShoppings(): LiveData<List<ClothesItem>>

    //  query for detail second fragment
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDetails(product: ClothesItem)

    @Query("SELECT * FROM Clothes_items Where id = :id")
    fun getShopping(id: Int): LiveData<ClothesItem>

    //  query for sort by category
    @Query("SELECT * FROM Clothes_items Where category = :category")
    fun getSortCategory(category: String): LiveData<List<ClothesItem>>

    //  query for sort by price
    @Query("SELECT * FROM Clothes_items Where price <= :price")
    fun getSortPrice(price: Double): LiveData<List<ClothesItem>>
}
