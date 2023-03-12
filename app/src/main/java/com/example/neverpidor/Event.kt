package com.example.neverpidor

open class Event<T>(private val content: T) {

    private var hasBeenHandled = false
        

    fun getContent(): T? {
        return if (!hasBeenHandled) {
            hasBeenHandled = true
            content
        } else null
    }

}