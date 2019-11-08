package com.egorshustov.vpoiske.util

fun String.extractInt(): Int? = replace("\\D+".toRegex(), "").toIntOrNull()