package com.egorshustov.vpoiske.data.source.remote.getuser

import com.egorshustov.vpoiske.data.User
import com.egorshustov.vpoiske.data.source.remote.getcities.CityResponse
import com.egorshustov.vpoiske.data.source.remote.getcountries.CountryResponse
import com.egorshustov.vpoiske.util.NO_VALUE
import com.egorshustov.vpoiske.util.Sex
import com.google.gson.annotations.SerializedName

data class UserResponse(
    val id: Long?,
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
    fun toEntity() =
        User(
            id ?: NO_VALUE.toLong(),
            firstName ?: "",
            lastName ?: "",
            isClosed ?: false,
            canAccessClosed ?: true,
            sex ?: Sex.ANY.value,
            bDate ?: "",
            city?.id ?: NO_VALUE,
            city?.title ?: "",
            country?.id ?: NO_VALUE,
            country?.title ?: "",
            photoMax ?: "",
            photoMaxOrig ?: "",
            photoId ?: "",
            canWritePrivateMessage ?: NO_VALUE,
            canSendFriendRequest ?: NO_VALUE,
            mobilePhone ?: "",
            homePhone ?: "",
            relation ?: NO_VALUE,
            counters?.albums ?: NO_VALUE,
            counters?.videos ?: NO_VALUE,
            counters?.audios ?: NO_VALUE,
            counters?.photos ?: NO_VALUE,
            counters?.notes ?: NO_VALUE,
            counters?.gifts ?: NO_VALUE,
            counters?.friends ?: NO_VALUE,
            counters?.groups ?: NO_VALUE,
            counters?.mutualFriends ?: NO_VALUE,
            counters?.userPhotos ?: NO_VALUE,
            counters?.userVideos ?: NO_VALUE,
            counters?.followers ?: NO_VALUE,
            counters?.subscriptions ?: NO_VALUE,
            counters?.pages ?: NO_VALUE
        )
}