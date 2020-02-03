package com.egorshustov.vpoiske.util

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

fun String.extractDigits(): String = replace("\\D+".toRegex(), "")

fun String.extractInt(): Int? = extractDigits().toIntOrNull()

val currentUnixMillis: Long
    get() = System.currentTimeMillis()

val currentUnixSeconds: Int
    get() = (System.currentTimeMillis() / MILLIS_IN_SECOND).toInt()

/**
 * Universal way for the project to display a toast message to a user.
 * @param message The text that should be shown to the user
 * @param duration How long to display the message
 */
fun Context.showMessage(message: String, duration: MessageLength = MessageLength.LONG) {
    val toastDuration = if (duration == MessageLength.LONG) {
        Toast.LENGTH_LONG
    } else {
        Toast.LENGTH_SHORT
    }
    Toast.makeText(this, message, toastDuration).show()
}

/**
 * Universal way for the project to display a snackBar to a user.
 * @param message The text that should be shown to the user
 * @param duration How long to display the message
 */
fun View.snackBar(message: String, duration: MessageLength = MessageLength.LONG) {
    val snackBarDuration = if (duration == MessageLength.LONG) {
        Snackbar.LENGTH_LONG
    } else {
        Snackbar.LENGTH_SHORT
    }
    Snackbar.make(this, message, snackBarDuration).show()
}

enum class MessageLength { SHORT, LONG }