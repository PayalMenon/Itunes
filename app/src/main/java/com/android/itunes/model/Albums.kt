package com.android.itunes.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class Albums(
    @SerializedName("resultCount")
    val count : Int,
    val results: List<Album>
)

@Parcelize
data class Album (
    val artistId : Long?,
    val collectionId : Long?,
    val trackId : Long?,
    val artistName : String?,
    val collectionName : String?,
    val trackName : String?,
    val collectionArtistName : String?,
    val previewUrl : String?,
    @SerializedName("artworkUrl100")
    val listImage : String?,
    @SerializedName("collectionViewUrl")
    val albumImage : String?,
    @SerializedName("primaryGenreName")
    val genre : String?
) : Parcelable