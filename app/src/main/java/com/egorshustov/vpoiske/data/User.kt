package com.egorshustov.vpoiske.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val id: Int,
    val firstName: String,
    val lastName: String,
    val isClosed: Boolean,
    val canAccessClosed: Boolean,
    val sex: Int,
    val bDate: String,
    val cityId: Int,
    val cityTitle: String,
    val countryId: Int,
    val countryTitle: String,
    val photoMax: String,
    val photoMaxOrig: String,
    val photoId: String,
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
    val onlineFriends: Int,
    val mutualFriends: Int,
    val userVideos: Int,
    val followers: Int,
    val subscriptions: Int,
    val pages: Int,
    // custom fields:
    val isFavorite: Boolean,
    val inBlacklist: Boolean,
    val searchId: Int
)