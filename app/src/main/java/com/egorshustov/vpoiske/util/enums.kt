package com.egorshustov.vpoiske.util

import androidx.annotation.StyleRes
import com.egorshustov.vpoiske.R

enum class VPoiskeTheme(@StyleRes val themeId: Int) {
    LIGHT_THEME(R.style.VPoiskeTheme_NoActionBar_Light),
    DARK_THEME(R.style.VPoiskeTheme_NoActionBar_Dark);

    fun getNext() = values()[((ordinal + 1) % values().size)]
}

enum class Sex(val value: Int) {
    ANY(0),
    FEMALE(1),
    MALE(2)
}

enum class HasPhoto(val value: Int) {
    NOT_NECESSARY(0),
    NECESSARY(1)
}

enum class Relation(
    val value: Int?,
    private val descriptionFemale: String,
    private val descriptionMale: String
) {
    NOT_DEFINED(null, "Любое", "Любое"),
    NOT_MARRIED(1, "Не замужем", "Не женат"),
    HAS_FRIEND(2, "Есть друг", "Есть подруга"),
    ENGAGED(3, "Помолвлена", "Помолвлен"),
    MARRIED(4, "Замужем", "Женат"),
    ALL_COMPLICATED(5, "Всё сложно", "Всё сложно"),
    IN_ACTIVE_SEARCH(6, "В активном поиске", "В активном поиске"),
    IN_LOVE(7, "Влюблена", "Влюблён"),
    IN_CIVIL_MARRIAGE(8, "В гражданском браке", "В гражданском браке");

    override fun toString() = if (sex == Sex.MALE) descriptionMale else descriptionFemale

    companion object {
        var sex = Sex.FEMALE
    }
}

enum class SortType(val value: Int) {
    BY_POPULARITY(0),
    BY_REGISTRATION_DATE(1)
}

enum class VkApiErrors(val code: Int) {
    UNKNOWN_ERROR_OCCURED(1),
    APPLICATION_IS_DISABLED(2),
    UNKNOWN_METHOD_PASSED(3),
    INCORRECT_SIGNATURE(4),
    USER_AUTHORIZATION_FAILED(5),
    TOO_MANY_REQUESTS_PER_SECOND(6),
    PERMISSION_IS_DENIED(7),
    INVALID_REQUEST(8),
    FLOOD_CONTROL(9),
    INTERNAL_SERVER_ERROR(10),
    APPLICATION_SHOULD_BE_DISABLED_OR_USER_AUTHORIZED(11),
    CAPTCHA_NEEDED(14),
    ACCESS_DENIED(15),
    HTTP_AUTHORIZATION_FAILED(16),
    VALIDATION_REQUIRED(17),
    USER_WAS_DELETED_OR_BANNED(18),
    METHOD_WAS_DISABLED(23),
    CONFIRMATION_REQUIRED(24),
    APPLICATION_AUTHORIZATION_FAILED(28),
    RATE_LIMIT_REACHED(29),
    PARAMETER_WAS_MISSING_OR_INVALID(100)
}