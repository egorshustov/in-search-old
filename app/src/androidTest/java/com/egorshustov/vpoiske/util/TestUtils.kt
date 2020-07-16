package com.egorshustov.vpoiske.util

import com.egorshustov.vpoiske.data.Search
import com.egorshustov.vpoiske.data.User
import com.egorshustov.vpoiske.data.source.remote.getuser.UserResponse
import com.egorshustov.vpoiske.data.source.remote.searchusers.SearchUsersInnerResponse

const val NO_VALUE = -1
/**
 * [User] objects used for tests.
 */
val testUsers = arrayListOf(
    User(
        1, "first_name1", "last_name1", false, true,
        Sex.MALE.value, "01.01.1991", 1, "Москва", 1, "Россия",
        "", "", "", "", 0, 0,
        "+71111111111", "111111", Relation.HAS_FRIEND.value ?: NO_VALUE,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1
    ).apply { searchId = 1 },
    User(
        2, "first_name2", "last_name2", false, true,
        Sex.FEMALE.value, "02.02.1992", 1, "Киев", 2, "Украина",
        "", "", "", "", 0, 0,
        "+72222222222", "222222", Relation.IN_LOVE.value ?: NO_VALUE,
        2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2
    ).apply { searchId = 2 }
)
val testUser = User(
    3, "first_name3", "last_name3", false, true,
    Sex.FEMALE.value, "03.03.1993", 2, "Санкт-Петербург", 1, "Россия",
    "", "", "", "", 0, 0,
    "+73333333333", "333333", Relation.ALL_COMPLICATED.value ?: NO_VALUE,
    3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3
).apply { searchId = 3 }

/**
 * [Search] object used for tests.
 */
val testSearch = Search(
    1, "Россия", 1, "Москва", Sex.FEMALE.value, 18, 32,
    Relation.NOT_MARRIED.value, false, 100, 3, 50,
    300, 50, 100
).apply {
    id = 2
    startUnixSeconds = 1594894171
}

/**
 * Fake response objects used for tests.
 */
val fakeSearchUsersInnerResponse = SearchUsersInnerResponse(null, null)

val fakeUserResponse = UserResponse(
    null,
    null,
    null,
    null,
    null,
    null,
    null,
    null,
    null,
    null,
    null,
    null,
    null,
    null,
    null,
    null,
    null,
    null,
    null
)