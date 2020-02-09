package com.egorshustov.vpoiske.data

import androidx.room.Embedded
import androidx.room.Relation

data class SearchWithUsers(
    @Embedded val search: Search,
    @Relation(
        parentColumn = "id",
        entityColumn = "search_id"
    )
    val userList: List<User>
)