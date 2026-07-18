package com.example.myapplication.domain

import app.cash.turbine.test
import com.example.myapplication.data.repository.expense.ExpenseRepository
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
import org.junit.Before
import org.junit.Test

class AddExpenseUseCaseTest {

    private lateinit var useCase: AddExpenseUseCase
    private val repository: ExpenseRepository = mockk()

    @Before
    fun setUp() {
        useCase = AddExpenseUseCase(repository)
    }

    @Test
    fun `addExpense should call repository and return result`() = runTest {
        // Given
        val expense = Expense(1, "Lunch", ExpenseCategory.FOOD, 1000L, 15.0)
        val expectedResult = Resource.Success(Unit)
        coEvery { repository.addExpense(expense) } returns expectedResult

        // When
        val result = useCase.addExpense(expense)

        // Then
        assertEquals(expectedResult, result)
        coVerify { repository.addExpense(expense) }
    }

    @Test
    fun `getAllExpenses should return flow from repository`() = runTest {
        // Given
        val expenses = listOf(
            Expense(1, "Lunch", ExpenseCategory.FOOD, 1000L, 15.0),
            Expense(2, "Taxi", ExpenseCategory.TRANSPORT, 2000L, 20.0)
        )
        every { repository.getExpenses() } returns flowOf(expenses)

        // When & Then
        useCase.getAllExpenses().test {
            val result = awaitItem()
            assertEquals(expenses, result)
            awaitComplete()
        }
    }
}
