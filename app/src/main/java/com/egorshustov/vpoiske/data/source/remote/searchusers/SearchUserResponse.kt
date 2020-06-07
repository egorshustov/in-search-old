package com.egorshustov.vpoiske.data.source.remote.searchusers

import com.egorshustov.vpoiske.data.User
import com.egorshustov.vpoiske.data.source.remote.getcities.CityResponse
import com.egorshustov.vpoiske.data.source.remote.getcountries.CountryResponse
import com.egorshustov.vpoiske.util.NO_VALUE
import com.egorshustov.vpoiske.util.Sex
import com.google.gson.annotations.SerializedName

data class SearchUserResponse(
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
    @SerializedName("photo_50")
    val photo50: String?,
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
    @SerializedName("last_seen")
    val lastSeen: LastSeen?,
    @SerializedName("followers_count")
    val followersCount: Int?
) {
    val hasCorrectPhone: Boolean
        get() = isMobilePhoneCorrect() || isHomePhoneCorrect()

    private fun isMobilePhoneCorrect(): Boolean {
        //todo add phone check
        return mobilePhone != null
    }

    private fun isHomePhoneCorrect(): Boolean {
        //todo add phone check
        return true
    }

    fun toEntity() =
        User(
            id ?: NO_VALUE.toLong(),
            firstName.orEmpty(),
            lastName.orEmpty(),
            isClosed ?: false,
            canAccessClosed ?: true,
            sex ?: Sex.ANY.value,
            bDate.orEmpty(),
            city?.id ?: NO_VALUE,
            city?.title.orEmpty(),
            country?.id ?: NO_VALUE,
            country?.title.orEmpty(),
            photo50.orEmpty(),
            photoMax.orEmpty(),
            photoMaxOrig.orEmpty(),
            photoId.orEmpty(),
            canWritePrivateMessage ?: NO_VALUE,
            canSendFriendRequest ?: NO_VALUE,
            if (isMobilePhoneCorrect()) mobilePhone.orEmpty() else "",
            if (isHomePhoneCorrect()) homePhone.orEmpty() else "",
            relation ?: NO_VALUE,
            NO_VALUE,
            NO_VALUE,
            NO_VALUE,
            NO_VALUE,
            NO_VALUE,
            NO_VALUE,
            NO_VALUE,
            NO_VALUE,
            NO_VALUE,
            NO_VALUE,
            NO_VALUE,
            followersCount ?: NO_VALUE,
            NO_VALUE,
            NO_VALUE
        )
}