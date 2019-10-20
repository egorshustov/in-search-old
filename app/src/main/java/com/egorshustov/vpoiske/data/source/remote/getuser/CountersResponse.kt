package com.egorshustov.vpoiske.data.source.remote.getuser

import com.google.gson.annotations.SerializedName

data class CountersResponse(
    val albums: Int?,
    val videos: Int?,
    val audios: Int?,
    val photos: Int?,
    val notes: Int?,
    val gifts: Int?,
    val friends: Int?,
    val groups: Int?,
    @SerializedName("mutual_friends")
    val mutualFriends: Int?,
    @SerializedName("user_photos")
    val userPhotos: Int?,
    @SerializedName("user_videos")
    val userVideos: Int?,
    val followers: Int?,
    val subscriptions: Int?,
    val pages: Int?
)