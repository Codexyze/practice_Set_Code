package com.example.exoplayernotification.di

import android.content.Context
import com.example.exoplayernotification.data.RepImpl.AudioRepoImpl
import com.example.exoplayernotification.domain.Repository.AudioRepository
import com.example.exoplayernotification.domain.UseCases.GetAllSongUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DiModule {
    @Singleton
    @Provides
    fun provideAudioRepo(@ApplicationContext context: Context): AudioRepository {
        return AudioRepoImpl(context = context)
    }

    @Singleton
    @Provides
    fun GetAllSongUseCase(audioRepository: AudioRepository): GetAllSongUseCase {
        return GetAllSongUseCase(audioRepository = audioRepository)
    }
}