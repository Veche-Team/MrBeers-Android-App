package com.example.neverpidor.data.network

import retrofit2.Response

class MyResponse<T>(
    private val status: Status,
    val data: Response<T>?,
    val exception: Exception?
) {
    companion object {
        fun<T> success(data: Response<T>): MyResponse<T> {
            return MyResponse(Status.Success, data, null)
        }
        fun <T> failure(exception: Exception): MyResponse<T> {
            return MyResponse(Status.Failure, null, exception)
        }
    }

    sealed class Status {
        object Success: Status()
        object Failure: Status()
    }
    val failed: Boolean
    get() = status == Status.Failure

    val isSuccessful: Boolean
    get() = !failed && this.data?.isSuccessful == true

    val body: T
    get() = this.data!!.body()!!
}