package com.example.mediaplayer.ui.player

import android.content.Context
import com.example.mediaplayer.model.Track
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class Player(context: Context) {

    private val player: ExoPlayer = ExoPlayer.Builder(context)
        .build()
        .apply { prepare() }

    val currentDuration: Long get() = player.duration
    val currentPosition: Long get() = player.currentPosition

    val progressObservable: Observable<Long> =
        Observable
            .interval(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
            .map { currentPosition }

    fun setTrack(track: Track): Boolean {
        if (track.playUrl.isNullOrEmpty()) {
            return false
        }

        player.setMediaItem(MediaItem.fromUri(track.playUrl))
        return true
    }

    fun play() {
        if (currentPosition >= currentDuration) {
            player.seekTo(0)
        }
        player.play()
    }

    fun seekTo(position: Long) = player.seekTo(position)
    fun pause() = player.pause()
    fun release() = player.release()
}

