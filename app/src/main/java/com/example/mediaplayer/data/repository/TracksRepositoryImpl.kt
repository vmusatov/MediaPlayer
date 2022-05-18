package com.example.mediaplayer.data.repository

import com.example.mediaplayer.data.remote.MediaApi
import com.example.mediaplayer.model.MediaType
import com.example.mediaplayer.model.Track
import io.reactivex.Single

class TracksRepositoryImpl(
    private val mediaApi: MediaApi
) : TracksRepository {

    override fun searchTracks(query: String): Single<List<Track>> {
        return mediaApi.searchMedia(query)
            .map { result ->
                return@map result.items
                    .filter { it.wrapperType.lowercase() == MediaType.TRACK.text }
                    .map { it.toTrack() }
            }
    }
}