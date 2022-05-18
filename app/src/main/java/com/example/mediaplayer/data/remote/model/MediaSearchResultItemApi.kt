package com.example.mediaplayer.data.remote.model

import com.example.mediaplayer.model.Track

data class MediaSearchResultItemApi(
    val trackId: Int,
    val wrapperType: String,
    val artistName: String,
    val trackName: String,
    val previewUrl: String?,
    val artworkUrl60: String?,
    val artworkUrl100: String?
) {
    fun toTrack(): Track {
        return Track(
            trackId = trackId,
            type = wrapperType,
            artistName = artistName,
            trackName = trackName,
            playUrl = previewUrl,
            previewImage60 = artworkUrl60,
            previewImage100 = artworkUrl100
        )
    }
}