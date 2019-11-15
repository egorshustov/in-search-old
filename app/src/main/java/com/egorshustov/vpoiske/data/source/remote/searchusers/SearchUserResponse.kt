package com.egorshustov.vpoiske.data.source.remote.searchusers

import com.egorshustov.vpoiske.data.User
import com.egorshustov.vpoiske.data.source.remote.getcities.CityResponse
import com.egorshustov.vpoiske.data.source.remote.getcountries.CountryResponse
import com.egorshustov.vpoiske.util.Sex
import com.google.gson.annotations.SerializedName

data class SearchUserResponse(
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
    @SerializedName("last_seen")
    val lastSeen: LastSeen?,
    @SerializedName("followers_count")
    val followersCount: Int?
) {
    fun hasCorrectPhone() = isMobilePhoneCorrect() || isHomePhoneCorrect()

    private fun isMobilePhoneCorrect(): Boolean {
        //todo add phone check
        return true
    }

    private fun isHomePhoneCorrect(): Boolean {
        //todo add phone check
        return true
    }

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
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            followersCount ?: -1,
            -1,
            -1,
            isFavorite = false,
            inBlacklist = false,
            searchId = -1
        )
}