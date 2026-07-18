package com.example.myapplication.data.local.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myapplication.data.local.entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface ExpenseDao {

    @Insert
    suspend fun addExpense(expenseEntity: ExpenseEntity): Long

    @Query("Select * from expenses Order by date DESC")
    fun getExpenses(): Flow<List<ExpenseEntity>>
}