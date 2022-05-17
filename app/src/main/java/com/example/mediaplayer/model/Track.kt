package com.example.mediaplayer.model

data class Track(
    val trackId: Int,
    val type: String,
    val artistName: String,
    val trackName: String,
    val playUrl: String,
    val previewImage60: String,
    val previewImage100: String
)