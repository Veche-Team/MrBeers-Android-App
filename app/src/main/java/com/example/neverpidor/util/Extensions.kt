package com.example.neverpidor.util

import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.disableErrorMessage() {
    this.error = null
    this.isErrorEnabled = false
}