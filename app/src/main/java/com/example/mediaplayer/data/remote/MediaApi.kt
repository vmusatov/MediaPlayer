package com.example.mediaplayer.data.remote

import com.example.mediaplayer.data.remote.model.MediaSearchResultApi
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MediaApi {

    @GET("search")
    fun searchMedia(
        @Query("term") query: String,
    ): Single<MediaSearchResultApi>

}