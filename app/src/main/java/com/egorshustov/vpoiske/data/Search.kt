package com.egorshustov.vpoiske.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.egorshustov.vpoiske.util.NO_VALUE

@Entity(tableName = "searches")
data class Search(
    val cityId: Int,
    @ColumnInfo(name = "city_title")
    val cityTitle: String,
    @ColumnInfo(name = "country_id")
    val countryId: Int,
    @ColumnInfo(name = "country_title")
    val countryTitle: String,
    @ColumnInfo(name = "age_from")
    val ageFrom: Int?,
    @ColumnInfo(name = "age_to")
    val ageTo: Int?,
    val relation: Int?,
    val sex: Int,
    @ColumnInfo(name = "with_phone_only")
    val withPhoneOnly: Boolean,
    @ColumnInfo(name = "founded_users_limit")
    val foundedUsersLimit: Int,
    @ColumnInfo(name = "friends_min_count")
    val friendsMinCount: Int?,
    @ColumnInfo(name = "friends_max_count")
    val friendsMaxCount: Int?,
    @ColumnInfo(name = "followers_min_count")
    val followersMinCount: Int,
    @ColumnInfo(name = "followers_max_count")
    val followersMaxCount: Int,
    @ColumnInfo(name = "days_interval")
    val daysInterval: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    @ColumnInfo(name = "start_unix_seconds")
    var startUnixSeconds: Int = NO_VALUE
}