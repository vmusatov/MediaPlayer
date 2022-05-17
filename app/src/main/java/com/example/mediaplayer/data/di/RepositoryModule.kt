package com.example.mediaplayer.data.di

import com.example.mediaplayer.data.TracksRepository
import com.example.mediaplayer.data.remote.MediaApi
import com.example.mediaplayer.data.repository.TracksRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideSettingsRepository(mediaApi: MediaApi): TracksRepository {
        return TracksRepositoryImpl(mediaApi)
    }
}