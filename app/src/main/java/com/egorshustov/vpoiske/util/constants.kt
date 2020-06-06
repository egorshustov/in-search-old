package com.egorshustov.vpoiske.util

import com.egorshustov.vpoiske.data.City
import com.egorshustov.vpoiske.data.Country

const val V_POISKE_PREFERENCES_FILENAME = "com.egorshustov.vpoiskepreferences"
const val PREF_KEY_CURRENT_THEME_ID = "com.egorshustov.vpoiske.PREF_KEY_CURRENT_THEME_ID"
const val PREF_KEY_CURRENT_SPAN_COUNT = "com.egorshustov.vpoiske.PREF_KEY_CURRENT_SPAN_COUNT"

const val NO_VALUE = -1

const val DEFAULT_SPAN_COUNT = 3
const val MAX_SPAN_COUNT = 3

const val DEFAULT_SEARCH_USERS_COUNT = 1000
const val DEFAULT_GET_COUNTRIES_COUNT = 1000
const val DEFAULT_GET_CITIES_COUNT = 1000
const val SEARCH_USERS_FRIENDS_LIMIT_SET_FIELDS = "last_seen,contacts,followers_count"
const val SEARCH_USERS_FRIENDS_LIMIT_NOT_SET_FIELDS =
    "last_seen,contacts,followers_count,photo_id,sex,bdate,city,country,photo_50,photo_max,photo_max_orig,contacts,relation,can_write_private_message,can_send_friend_request"
const val DEFAULT_GET_USER_FIELDS =
    "photo_id,sex,bdate,city,country,counters,photo_50,photo_max,photo_max_orig,contacts,relation,can_write_private_message,can_send_friend_request"
const val DEFAULT_API_VERSION = "5.102"

val DEFAULT_COUNTRY_TITLE = Country(-1, "Страна")
val DEFAULT_CITY_TITLE = City(-1, "Город", "", "")
val DEFAULT_AGE_FROM: Int? = null
val DEFAULT_AGE_TO: Int? = null
const val DEFAULT_FOUND_USERS_LIMIT: Int = 100
const val DEFAULT_FOLLOWERS_MIN_COUNT: Int = 0
const val DEFAULT_FOLLOWERS_MAX_COUNT: Int = 150
const val DEFAULT_DAYS_INTERVAL: Int = 3

const val MILLIS_IN_SECOND = 1000
const val SECONDS_IN_MINUTE = 60
const val MINUTES_IN_HOUR = 60
const val HOURS_IN_DAY = 24
const val SECONDS_IN_DAY = SECONDS_IN_MINUTE * MINUTES_IN_HOUR * HOURS_IN_DAY
const val MAX_DAYS_IN_MONTH = 31
const val MONTHS_IN_YEAR = 12

const val ERROR_DELAY_IN_MILLIS: Long = 3000
const val PAUSE_DELAY_IN_MILLIS: Long = 500