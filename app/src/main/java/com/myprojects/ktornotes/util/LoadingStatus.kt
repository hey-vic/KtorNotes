package com.myprojects.ktornotes.util

sealed class LoadingStatus<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T? = null) : LoadingStatus<T>(data)
    class Error<T>(message: String? = null, data: T? = null) : LoadingStatus<T>(data, message)
    class Loading<T>(data: T? = null) : LoadingStatus<T>(data)
}
