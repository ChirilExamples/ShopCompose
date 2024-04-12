package com.example.shopcompose.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.shopcompose.data.data_structure.ClothesItem
import com.example.shopcompose.data.data_structure.Rating
import com.example.shopcompose.data.repository.Repository
import com.example.shopcompose.domain.utils.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class ShoppingListViewModelTest {

    @get:Rule
    val instantTaskExecutionRule = InstantTaskExecutorRule()

    private val shoppingList = listOf(
        ClothesItem(1, "unknown", "Thing", "", Rating(1, 3.0), 16.50, "Human"),
        ClothesItem(2, "male", "some", "", Rating(1, 3.0), 16.50, "Alien")
    )
    private val flow = flow<Resource<List<ClothesItem>>> {
        emit(Resource.Success(shoppingList))
    }

    private val repository: Repository = mockk {
        coEvery { getAllData() } returns flow
    }

    private val viewModel = ShoppingListViewModel(repository)

    @Test
    fun updateCatUpdatesTheValueOfCatState() {
        val actualState = viewModel.catState.value
        val toUpdateStr = "Women"
        assertEquals("All", actualState)
        viewModel.updateCat(toUpdateStr)
        val updatedStr = viewModel.catState.value
        assertEquals(toUpdateStr, updatedStr)
    }

    @Test
    fun updatePriceUpdatesTheValueOfPriceState() {
        val actualState = viewModel.priceState.value
        val toUpdateStr = 50
        assertEquals(999999, actualState)
        viewModel.updatePrice(toUpdateStr)
        val updatedStr = viewModel.priceState.value
        assertEquals(toUpdateStr, updatedStr)
    }

    @Test
    fun `get shoppings should return emit list of things`() = runBlocking {
        viewModel.getData()
        delay(1000)
        assertTrue(viewModel.itemsList.value is Resource.Success)
        assertEquals(shoppingList, (viewModel.itemsList.value as Resource.Success).data)
    }
}