package com.example.neverpidor.util

import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.disableErrorMessage() {
    this.error = null
    this.isErrorEnabled = false
}


fun Double.format(digitsAfterDot: Int): String {
    val str = this.toString()
    var (beforeDot, afterDot) = str.split('.')
    if (afterDot.length == digitsAfterDot) return this.toString()
    if (afterDot.length < digitsAfterDot) {
        afterDot += '0'
    } else {
        afterDot = afterDot.dropLast(1)
    }
    return "$beforeDot.$afterDot"
}