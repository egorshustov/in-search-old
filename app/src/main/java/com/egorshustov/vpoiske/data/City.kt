package com.egorshustov.vpoiske.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cities")
data class City(
    @PrimaryKey
    val id: Int,
    val title: String,
    val area: String?,
    val region: String?
)