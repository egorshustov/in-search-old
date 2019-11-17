package com.egorshustov.vpoiske.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.egorshustov.vpoiske.util.NO_VALUE

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val id: Long,
    @ColumnInfo(name = "first_name")
    val firstName: String,
    @ColumnInfo(name = "last_name")
    val lastName: String,
    @ColumnInfo(name = "is_closed")
    val isClosed: Boolean,
    @ColumnInfo(name = "can_access_closed")
    val canAccessClosed: Boolean,
    val sex: Int,
    @ColumnInfo(name = "b_date")
    val bDate: String,
    @ColumnInfo(name = "city_id")
    val cityId: Int,
    @ColumnInfo(name = "city_title")
    val cityTitle: String,
    @ColumnInfo(name = "country_id")
    val countryId: Int,
    @ColumnInfo(name = "country_title")
    val countryTitle: String,
    @ColumnInfo(name = "photo_max")
    val photoMax: String,
    @ColumnInfo(name = "photo_max_orig")
    val photoMaxOrig: String,
    @ColumnInfo(name = "photo_id")
    val photoId: String,
    @ColumnInfo(name = "can_write_private_message")
    val canWritePrivateMessage: Int,
    @ColumnInfo(name = "can_send_friend_request")
    val canSendFriendRequest: Int,
    @ColumnInfo(name = "mobile_phone")
    val mobilePhone: String,
    @ColumnInfo(name = "home_phone")
    val homePhone: String,
    val relation: Int,
    // counters:
    val albums: Int,
    val videos: Int,
    val audios: Int,
    val photos: Int,
    val notes: Int,
    val gifts: Int,
    val friends: Int,
    val groups: Int,
    @ColumnInfo(name = "mutual_friends")
    val mutualFriends: Int,
    @ColumnInfo(name = "user_photos")
    val userPhotos: Int,
    @ColumnInfo(name = "user_videos")
    val userVideos: Int,
    val followers: Int,
    val subscriptions: Int,
    val pages: Int
) {
    // custom fields:
    @ColumnInfo(name = "is_favorite")
    var isFavorite: Boolean = false
    @ColumnInfo(name = "in_blacklist")
    var inBlacklist: Boolean = false
    @ColumnInfo(name = "search_id")
    var searchId: Long = NO_VALUE.toLong()
}