package com.example.mediaplayer.data.repository

import com.example.mediaplayer.model.Track
import io.reactivex.Single

interface TracksRepository {
    fun searchTracks(query: String): Single<List<Track>>
}