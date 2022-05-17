package com.example.mediaplayer.di

import com.example.mediaplayer.data.di.RepositoryModule
import com.example.mediaplayer.data.di.RetrofitModule
import com.example.mediaplayer.ui.search.SearchFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        RetrofitModule::class,
        RepositoryModule::class
    ]
)
interface AppComponent {

    fun inject(searchFragment: SearchFragment)
}