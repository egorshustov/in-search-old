package com.egorshustov.vpoiske.util

const val DEFAULT_SEARCH_USERS_COUNT = 1000
const val DEFAULT_GET_COUNTRIES_COUNT = 1000
const val DEFAULT_GET_CITIES_COUNT = 1000
const val DEFAULT_SEARCH_USERS_FIELDS = "last_seen,contacts"
const val DEFAULT_GET_USER_FIELDS =
    "photo_id,sex,bdate,city,country,counters,photo_max,photo_max_orig, contacts, relation, can_write_private_message, can_send_friend_request"
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

enum class Relation(val value: Int) {
    NOT_DEFINED(0),
    NOT_MARRIED(1),
    HAS_FRIEND(2),
    ENGAGED(3),
    MARRIED(4),
    ALL_COMPLICATED(5),
    IN_ACTIVE_SEARCH(6),
    IN_LOVE(7),
    IN_CIVIL_MARRIAGE(8)
}
