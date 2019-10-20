package com.egorshustov.vpoiske.util

const val DEFAULT_SEARCH_USERS_COUNT = 1000
const val DEFAULT_SEARCH_USERS_FIELDS = "last_seen,contacts"
const val DEFAULT_API_VERSION = "5.102"

enum class Sex(val value: Int) {
    ANY(0),
    FEMALE(1),
    MALE(2)
}

enum class HasPhoto(val value: Int) {
    NOT_NECESSARY(0),
    NECESSARY(1)
}

