package com.example.shopcompose.domain

import junit.framework.TestCase.assertEquals
import org.junit.Test

class ShoppingListViewModelTest() {

    val repository = FakeRepository()
    private val viewModel = ShoppingListViewModel()

    val listPrice = listOf<Int>(
        5,
        10,
        25,
        50,
        100,
        150,
        200,
        250,
        300,
    )

    @Test
    fun getDataPriceTest() {
        viewModel.getDataPrice(listPrice)


        assertEquals()
    }
}