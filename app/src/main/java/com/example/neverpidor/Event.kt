package com.example.neverpidor

open class Event<T>(private val content: T) {

    var hasBeenHandled = false
        private set

    fun getContent(): T? {
        return if (!hasBeenHandled) {
            hasBeenHandled = true
            content
        } else null
    }

    fun peekContent(): T = content
}