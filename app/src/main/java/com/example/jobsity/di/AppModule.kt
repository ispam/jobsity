package com.example.jobsity.di

import com.example.jobsity.Jobsity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(value = [SingletonComponent::class])
object AppModule {

    @Provides
    fun provideApplication(app: Jobsity): Jobsity = app
}