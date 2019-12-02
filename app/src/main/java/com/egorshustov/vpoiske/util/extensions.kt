package com.egorshustov.vpoiske.util

fun String.extractDigits(): String = replace("\\D+".toRegex(), "")

fun String.extractInt(): Int? = extractDigits().toIntOrNull()

val currentUnixMillis: Long
    get() = System.currentTimeMillis()

val currentUnixSeconds: Int
    get() = (System.currentTimeMillis() / MILLIS_IN_SECOND).toInt()