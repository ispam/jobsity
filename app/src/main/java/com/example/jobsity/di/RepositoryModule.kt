package com.example.jobsity.di

import com.example.jobsity.data.TVMazeService
import com.example.jobsity.data.local.ShowRepository
import com.example.jobsity.data.local.ShowRepositoryImpl
import com.example.jobsity.di.annotations.IoDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module(includes = [AppModule::class, NetworkModule::class])
@InstallIn(value = [ViewModelComponent::class])
object RepositoryModule {

    @Provides
    fun provideMainRepositoryImpl(
        @IoDispatcher
        dispatcher: CoroutineDispatcher,
        tvMazeService: TVMazeService
    ): ShowRepository = ShowRepositoryImpl(dispatcher, tvMazeService)

}
