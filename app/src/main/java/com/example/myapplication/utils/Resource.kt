package com.example.myapplication.utils

sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val message: String,
        val throwable: Throwable) : Resource<Nothing>()
    data object Loading: Resource<Nothing>()
}