package com.example.myapplication.ui.expense

import app.cash.turbine.test
import com.example.myapplication.MainDispatcherRule
import com.example.myapplication.domain.AddExpenseUseCase
import com.example.myapplication.model.expense.Expense
import com.example.myapplication.model.expense.ExpenseCategory
import com.example.myapplication.utils.Resource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ExpenseViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: ExpenseViewModel
    private val useCase: AddExpenseUseCase = mockk()

    @Before
    fun setUp() {
        // Mock default behavior for init
        every { useCase.getAllExpenses() } returns flowOf(emptyList())
        viewModel = ExpenseViewModel(useCase)
    }

    @Test
    fun `init should observe expenses`() = runTest {
        val expenses = listOf(
            Expense(1, "Lunch", ExpenseCategory.FOOD, 1000L, 15.0)
        )
        every { useCase.getAllExpenses() } returns flowOf(expenses)

        // Re-init to capture the new flow
        val vm = ExpenseViewModel(useCase)

        vm.expenseUIState.test {
            val state = awaitItem()
            assertEquals(expenses, state.data)
        }
    }

    @Test
    fun `onTitleChange should update form state`() = runTest {
        viewModel.onTitleChange("New Title")
        assertEquals("New Title", viewModel.form.value.title)
    }

    @Test
    fun `saveExpense should show error when title is blank`() = runTest {
        viewModel.onTitleChange("")
        viewModel.saveExpense()
        assertEquals("Title cannot be empty", viewModel.form.value.titleError)
    }

    @Test
    fun `saveExpense should show error when category is blank`() = runTest {
        viewModel.onTitleChange("Lunch")
        viewModel.onCategoryChange("")
        viewModel.saveExpense()
        assertEquals("Category cannot be empty", viewModel.form.value.categoryError)
    }

    @Test
    fun `saveExpense should show error when amount is invalid`() = runTest {
        viewModel.onTitleChange("Lunch")
        viewModel.onCategoryChange("FOOD")
        viewModel.onAmountChange("invalid")
        viewModel.saveExpense()
        assertEquals("Enter a valid amount", viewModel.form.value.amountError)
    }

    @Test
    fun `saveExpense should call useCase and emit success event`() = runTest {
        // Given
        viewModel.onTitleChange("Lunch")
        viewModel.onCategoryChange("FOOD")
        viewModel.onAmountChange("15.0")
        
        coEvery { useCase.addExpense(any()) } returns Resource.Success(Unit)

        viewModel.expenseUIEvent.test {
            // When
            viewModel.saveExpense()

            // Then
            val event = awaitItem()
            assertEquals(ExpenseUiEvent.SaveSuccess, event)
            
            // Verify form reset
            assertEquals("", viewModel.form.value.title)
            assertNull(viewModel.form.value.titleError)
        }

        coVerify { useCase.addExpense(match { 
            it.title == "Lunch" && it.amount == 15.0 && it.category == ExpenseCategory.FOOD
        }) }
    }

    @Test
    fun `saveExpense should emit error event when useCase fails`() = runTest {
        // Given
        viewModel.onTitleChange("Lunch")
        viewModel.onCategoryChange("FOOD")
        viewModel.onAmountChange("15.0")
        
        val errorMessage = "Failed to save"
        coEvery { useCase.addExpense(any()) } returns Resource.Error(errorMessage, Exception())

        viewModel.expenseUIEvent.test {
            // When
            viewModel.saveExpense()

            // Then
            val event = awaitItem()
            assertEquals(ExpenseUiEvent.SaveError(errorMessage), event)
        }
    }
}
