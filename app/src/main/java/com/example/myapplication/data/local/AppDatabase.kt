package com.example.myapplication.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.data.local.dao.ExpenseDao
import com.example.myapplication.data.local.entity.ExpenseEntity


@Database(entities = [ExpenseEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
}