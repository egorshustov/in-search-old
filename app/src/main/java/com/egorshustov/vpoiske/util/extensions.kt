package com.egorshustov.vpoiske.util

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import com.egorshustov.vpoiske.data.Search
import com.egorshustov.vpoiske.data.SearchWithUsers
import com.egorshustov.vpoiske.data.source.remote.CustomException
import com.egorshustov.vpoiske.main.MainViewModel
import com.egorshustov.vpoiske.search.SearchViewModel
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

fun String.extractDigits(): String = replace("\\D+".toRegex(), "")

fun String.extractInt(): Int? = extractDigits().toIntOrNull()

fun String.removeDots(): String = replace(".", "")

val currentUnixMillis: Long
    get() = System.currentTimeMillis()

val currentUnixSeconds: Int
    get() = (System.currentTimeMillis() / MILLIS_IN_SECOND).toInt()

fun NavController.safeNavigate(navDirections: NavDirections) {
    try {
        navigate(navDirections)
    } catch (e: Exception) {
        Timber.e(e)
    }
}

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

fun SearchWithUsers.getUserPhoto50Url(userOrder: Int): String? =
    userList.getOrNull(userOrder)?.photo50

fun CustomException.needToWait(): Boolean =
    vkErrorCode == VkApiErrors.TOO_MANY_REQUESTS_PER_SECOND.code
            || vkErrorCode == VkApiErrors.FLOOD_CONTROL.code

fun openUser(
    mainViewModel: MainViewModel?,
    searchViewModel: SearchViewModel?,
    userId: Long
) {
    mainViewModel?.openUser(userId)
    searchViewModel?.openUser(userId)
}