package com.egorshustov.vpoiske.data.source.remote.getuser

import com.egorshustov.vpoiske.data.User
import com.egorshustov.vpoiske.data.source.remote.getcities.CityResponse
import com.egorshustov.vpoiske.data.source.remote.getcountries.CountryResponse
import com.egorshustov.vpoiske.util.Sex
import com.google.gson.annotations.SerializedName

data class UserResponse(
    val id: Int?,
    @SerializedName("first_name")
    val firstName: String?,
    @SerializedName("last_name")
    val lastName: String?,
    @SerializedName("is_closed")
    val isClosed: Boolean?,
    @SerializedName("can_access_closed")
    val canAccessClosed: Boolean?,
    val sex: Int?,
    @SerializedName("bdate")
    val bDate: String?,
    val city: CityResponse?,
    val country: CountryResponse?,
    @SerializedName("photo_max")
    val photoMax: String?,
    @SerializedName("photo_max_orig")
    val photoMaxOrig: String?,
    @SerializedName("photo_id")
    val photoId: String?,
    @SerializedName("can_write_private_message")
    val canWritePrivateMessage: Int?,
    @SerializedName("can_send_friend_request")
    val canSendFriendRequest: Int?,
    @SerializedName("mobile_phone")
    val mobilePhone: String?,
    @SerializedName("home_phone")
    val homePhone: String?,
    val relation: Int?,
    val counters: CountersResponse?
) {
    //todo set -1 and "" to constants
    fun toEntity() =
        User(
            id ?: -1,
            firstName ?: "",
            lastName ?: "",
            isClosed ?: false,
            canAccessClosed ?: true,
            sex ?: Sex.ANY.value,
            bDate ?: "",
            city?.id ?: -1,
            city?.title ?: "",
            country?.id ?: -1,
            country?.title ?: "",
            photoMax ?: "",
            photoMaxOrig ?: "",
            photoId ?: "",
            canWritePrivateMessage ?: -1,
            canSendFriendRequest ?: -1,
            mobilePhone ?: "",
            homePhone ?: "",
            relation ?: -1,
            counters?.albums ?: -1,
            counters?.videos ?: -1,
            counters?.audios ?: -1,
            counters?.photos ?: -1,
            counters?.notes ?: -1,
            counters?.gifts ?: -1,
            counters?.friends ?: -1,
            counters?.groups ?: -1,
            counters?.mutualFriends ?: -1,
            counters?.userPhotos ?: -1,
            counters?.userVideos ?: -1,
            counters?.followers ?: -1,
            counters?.subscriptions ?: -1,
            counters?.pages ?: -1,
            isFavorite = false,
            inBlacklist = false,
            searchId = -1
        )
}