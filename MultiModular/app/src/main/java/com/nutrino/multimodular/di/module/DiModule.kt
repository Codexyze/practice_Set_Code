package com.nutrino.multimodular.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

import javax.inject.Singleton

//@Module
//@InstallIn(SingletonComponent::class)
//object UseCaseModule {
//
//    @Provides
//    @Singleton
//    fun provideGetAllMemesUseCase(
//        repository: Repository
//    ): GetAllMemesUseCase {
//        return GetAllMemesUseCase(repository)
//    }
//}