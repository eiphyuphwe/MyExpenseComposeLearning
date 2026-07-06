package com.example.myapplication.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class ViewModelFactory<T : ViewModel>(
    private val creator: () -> T
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T2 : ViewModel> create(modelClass: Class<T2>): T2 = creator() as T2
}
