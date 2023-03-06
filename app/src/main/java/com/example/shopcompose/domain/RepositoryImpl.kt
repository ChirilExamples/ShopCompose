package com.example.shopcompose.domain

import android.util.Log
import com.example.shopcompose.data.data_structure.ClothesItem
import com.example.shopcompose.data.local.ShoppingDAO
import com.example.shopcompose.data.remote.ShoppingService
import com.example.shopcompose.data.repository.Repository
import com.example.shopcompose.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val remoteDatasource: ShoppingService
) : Repository {
    override suspend fun getAllData(): Flow<Resource<List<ClothesItem>>> = flow {
        emit(Resource.Loading())
        try {
            Log.i("DebugNetworkRepo", "Pass getAllNews ${remoteDatasource.getAllShoppings()}")
            val response = remoteDatasource.getAllShoppings()

            if (response.isSuccessful)
                response.body()?.let {
                    emit(Resource.Success(it))
                    Log.i("DebugNetworkRepoLet", response.toString())
                }
            else emit(Resource.Error(response.code().toString()))
        } catch (e: HttpException) {
            emit(Resource.Error("Could not load data"))
            Log.i("DebugNetworkRepo", "load data error")
        } catch (e: IOException) {
            emit(Resource.Error("Check internet or server"))
            Log.i("DebugNetworkRepo", "check internet error")
        }
    }


//    fun getShoppings() = remoteDatasource.getAllShoppings()

//    suspend fun insertShoppings() = localDatasource.insertAll()


//    fun getShoppingDetailsData(id: Int) = performGetOperation(
//        databaseQuery = { localDatasource.getShopping(id) },
//        networkCall = { remoteDatasource.getShoppingsDetails(id) },
//        saveCallResult = { localDatasource.insertDetails(it) }
//    )
}
