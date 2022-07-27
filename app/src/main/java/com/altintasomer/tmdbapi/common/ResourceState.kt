package com.altintasomer.tmdbapi.common

sealed class ResourceState<T>(val data: T? = null,val message: String? = null) {
    class Success<T>(data: T?): ResourceState<T>(data = data)
    class Error<T>(message: String?): ResourceState<T>(data = null,message = message)
    class Loading<T>(data: T? = null): ResourceState<T>(data = data)
}