package com.egorshustov.vpoiske.util

fun String.extractDigits(): String = replace("\\D+".toRegex(), "")
fun String.extractInt(): Int? = extractDigits().toIntOrNull()