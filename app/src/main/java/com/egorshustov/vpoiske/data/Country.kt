package com.egorshustov.vpoiske.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "countries")
data class Country(
    @PrimaryKey
    val id: Int,
    val title: String
)