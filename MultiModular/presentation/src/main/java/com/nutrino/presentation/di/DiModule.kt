package com.nutrino.presentation.di

import com.nutrino.domain.Repository.Repository
import com.nutrino.domain.usecase.GetAllMemesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DiModule {
    @Provides
    fun provideUseCase(repository: Repository): GetAllMemesUseCase {
        return GetAllMemesUseCase(repository)
    }


}