package com.example.bookapp.utils

sealed class Resource<T> (
    val data : T? = null,
    val msg : String? = null
) {
    class Success<T> (data: T) : Resource<T>(data)
    class Error<T> (msg : String) : Resource<T>(msg = msg)
    class Loading<T> : Resource<T>()
}
