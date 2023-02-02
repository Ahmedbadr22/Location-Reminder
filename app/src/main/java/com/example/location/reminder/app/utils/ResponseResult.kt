package com.example.location.reminder.app.utils

sealed class ResponseResult<T>(
    val data: T? = null,
    val exception: Exception? = null
) {
    class Success<T>(data: T?) : ResponseResult<T>(data)
    class Error<T>(exception: Exception) : ResponseResult<T>(exception = exception)
    class Loading<T> : ResponseResult<T>()
}
