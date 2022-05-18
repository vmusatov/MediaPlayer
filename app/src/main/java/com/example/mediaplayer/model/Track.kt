package com.example.mediaplayer.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Track(
    val trackId: Int,
    val type: String,
    val artistName: String,
    val trackName: String,
    val playUrl: String?,
    val previewImage60: String?,
    val previewImage100: String?
) : Parcelable