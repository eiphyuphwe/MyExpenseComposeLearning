package com.example.myapplication.data.repository.expense

import app.cash.turbine.test
import com.example.myapplication.data.local.dao.ExpenseDao
import com.example.myapplication.data.local.entity.ExpenseEntity
import com.example.myapplication.model.expense.Expense
import com.example.myapplication.model.expense.ExpenseCategory
import com.example.myapplication.utils.Resource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ExpenseRepositoryImplTest {

    private lateinit var repository: ExpenseRepositoryImpl
    private val dao: ExpenseDao = mockk()

    @Before
    fun setUp() {
        repository = ExpenseRepositoryImpl(dao)
    }

    @Test
    fun `addExpense should return Success when dao succeeds`() = runTest {
        // Given
        val expense = Expense(1, "Lunch", ExpenseCategory.FOOD, 1000L, 15.0)
        coEvery { dao.addExpense(any()) } returns 1L

        // When
        val result = repository.addExpense(expense)

        // Then
        assertTrue(result is Resource.Success)
        coVerify { dao.addExpense(match { 
            it.title == "Lunch" && it.amount == 15.0 && it.category == "FOOD"
        }) }
    }

    @Test
    fun `addExpense should return Error when dao throws exception`() = runTest {
        // Given
        val expense = Expense(1, "Lunch", ExpenseCategory.FOOD, 1000L, 15.0)
        val errorMessage = "Database Error"
        coEvery { dao.addExpense(any()) } throws Exception(errorMessage)

        // When
        val result = repository.addExpense(expense)

        // Then
        assertTrue(result is Resource.Error)
        assertEquals(errorMessage, (result as Resource.Error).message)
    }

    @Test
    fun `getExpenses should return list of Expenses mapped from entities`() = runTest {
        // Given
        val entities = listOf(
            ExpenseEntity(1, "Lunch", "FOOD", 1000L, 15.0),
            ExpenseEntity(2, "Taxi", "TRANSPORT", 2000L, 20.0)
        )
        every { dao.getExpenses() } returns flowOf(entities)

        // When & Then
        repository.getExpenses().test {
            val result = awaitItem()
            assertEquals(2, result.size)
            
            assertEquals(1L, result[0].id)
            assertEquals("Lunch", result[0].title)
            assertEquals(ExpenseCategory.FOOD, result[0].category)
            
            assertEquals(2L, result[1].id)
            assertEquals("Taxi", result[1].title)
            assertEquals(ExpenseCategory.TRANSPORT, result[1].category)
            
            awaitComplete()
        }
    }
}
