package com.example.myapplication.data.repository.expense

import com.example.myapplication.data.local.dao.ExpenseDao
import com.example.myapplication.data.mapper.toDoMain
import com.example.myapplication.data.mapper.toEntity
import com.example.myapplication.model.expense.Expense
import com.example.myapplication.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExpenseRepositoryImpl @Inject constructor(private val dao: ExpenseDao) : ExpenseRepository {
    override suspend fun addExpense(expense: Expense): Resource<Unit> {
        return try {
            withContext(Dispatchers.IO) {
                dao.addExpense(expense.toEntity())
            }
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(
                e.message ?: "Insertion error",
                throwable = e
            )
        }
    }

    override fun getExpenses(): Flow<List<Expense>> {
        return dao.getExpenses().map { expenseEntityList ->
            expenseEntityList.map { entity ->
                entity.toDoMain()
            }
        }
    }
}