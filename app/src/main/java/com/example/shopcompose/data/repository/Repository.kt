package com.example.shopcompose.data.repository

import com.example.shopcompose.data.data_structure.ClothesItem
import com.example.shopcompose.utils.Resource
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun getAllData(): Flow<Resource<List<ClothesItem>>>
}