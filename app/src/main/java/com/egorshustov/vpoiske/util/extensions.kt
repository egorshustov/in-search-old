package com.egorshustov.vpoiske.util

import android.content.Context
import android.view.View
import android.widget.Toast
import com.egorshustov.vpoiske.data.Search
import com.egorshustov.vpoiske.data.SearchWithUsers
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

fun String.extractDigits(): String = replace("\\D+".toRegex(), "")

fun String.extractInt(): Int? = extractDigits().toIntOrNull()

fun String.removeDots(): String = replace(".", "")

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

fun Search.getAgeRangeText(): String = "Возраст: от ${ageFrom ?: "любого"} до ${ageTo ?: "любого"}"

fun Search.getWithPhoneOnlyText(): String =
    "Только с телефоном: ${if (withPhoneOnly) "да" else "нет"}"

fun Search.isCurrentYear(): Boolean {
    val searchYear =
        Calendar.getInstance().apply { timeInMillis = startUnixSeconds.toLong() * MILLIS_IN_SECOND }
            .get(Calendar.YEAR)
    val currentYear =
        Calendar.getInstance().apply { timeInMillis = currentUnixMillis }.get(Calendar.YEAR)
    return searchYear == currentYear
}

fun Search.isCurrentDay(): Boolean {
    val searchDayOfYear =
        Calendar.getInstance().apply { timeInMillis = startUnixSeconds.toLong() * MILLIS_IN_SECOND }
            .get(Calendar.DAY_OF_YEAR)
    val currentDayOfYear =
        Calendar.getInstance().apply { timeInMillis = currentUnixMillis }.get(Calendar.DAY_OF_YEAR)
    return (searchDayOfYear == currentDayOfYear) && isCurrentYear()
}

fun Search.getDateTimeText(): String {
    val searchCalendar =
        Calendar.getInstance().apply { timeInMillis = startUnixSeconds.toLong() * MILLIS_IN_SECOND }
    val formatPattern = when {
        isCurrentDay() -> "HH:mm"
        isCurrentYear() -> "d MMM"
        else -> "d MMM yyyy"
    }
    return SimpleDateFormat(formatPattern, Locale.getDefault()).format(searchCalendar.time)
        .removeDots()
}

fun SearchWithUsers.getUsersCount(): Int = userList.size

fun SearchWithUsers.getFavoritesCount() = userList.filter { it.isFavorite }.size