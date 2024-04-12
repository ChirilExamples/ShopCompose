package com.example.shopcompose.presentation

import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopcompose.data.data_structure.ClothesItem
import com.example.shopcompose.data.repository.Repository
import com.example.shopcompose.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingListViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _itemsList: MutableStateFlow<Resource<List<ClothesItem>>> =
        MutableStateFlow(Resource.Loading())
    val itemsList: StateFlow<Resource<List<ClothesItem>>> = _itemsList

    val scrollState = LazyListState()

    val catState = MutableStateFlow("All")
    val priceState = MutableStateFlow(999999)

    fun updateCat(cat: String) {
        if (catState.value != cat) {
            catState.value = cat
        }
    }

    fun updatePrice(price: Int) {
        if (priceState.value != price) {
            priceState.update { price }
        }
    }

    init {
        getData()
    }

    fun getData() = viewModelScope.launch(Dispatchers.IO) {
        repository.getAllData().collectLatest { _itemsList.emit(it) }
//        Log.i("DebugNetworkVM", itemsList.value.data.toString())
    }
}
