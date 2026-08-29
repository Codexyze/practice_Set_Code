package com.nutrino.worddocstools.di.modules

import com.nutrino.worddocstools.domain.repository.ExcelRepository
import com.nutrino.worddocstools.infrastructure.repository.ExcelRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ExcelRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindExcelRepository(
        impl: ExcelRepositoryImpl
    ): ExcelRepository
}

@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {

    @Provides
    @Singleton
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}
