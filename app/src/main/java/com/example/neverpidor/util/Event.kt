package com.example.neverpidor.util

open class Event<T>(private val content: T) {

    private var hasBeenHandled = false

    fun getContent(): T? {
        return if (!hasBeenHandled) {
            hasBeenHandled = true
            content
        } else null
    }
}