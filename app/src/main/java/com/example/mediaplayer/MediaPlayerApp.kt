package com.example.mediaplayer

import android.app.Application
import android.content.Context
import com.example.mediaplayer.di.AppComponent
import com.example.mediaplayer.di.DaggerAppComponent
import com.squareup.picasso.Picasso

val Context.appComponent: AppComponent
    get() = when (this) {
        is MediaPlayerApp -> appComponent
        else -> applicationContext.appComponent
    }

class MediaPlayerApp : Application() {

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder().build()

        configurePicasso()
    }

    private fun configurePicasso() {
        val built = Picasso.Builder(this).build()

        built.setIndicatorsEnabled(false)
        built.isLoggingEnabled = false

        Picasso.setSingletonInstance(built)
    }
}