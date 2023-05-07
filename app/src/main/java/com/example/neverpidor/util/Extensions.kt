package com.example.neverpidor.util

fun Double.format(digitsAfterDot: Int): String {
    val str = this.toString()
    var (beforeDot, afterDot) = str.split('.')
    if (afterDot.length == digitsAfterDot) return this.toString()
    if (afterDot.length < digitsAfterDot) {
        afterDot += '0'
    } else {
        afterDot = afterDot.dropLast(afterDot.length - digitsAfterDot)
    }
    return "$beforeDot.$afterDot"
}