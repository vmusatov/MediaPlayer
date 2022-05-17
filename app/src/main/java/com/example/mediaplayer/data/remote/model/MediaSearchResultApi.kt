package com.example.mediaplayer.data.remote.model

import com.google.gson.annotations.SerializedName

data class MediaSearchResultApi(
    @SerializedName("results")
    val items: List<MediaSearchResultItemApi>
)