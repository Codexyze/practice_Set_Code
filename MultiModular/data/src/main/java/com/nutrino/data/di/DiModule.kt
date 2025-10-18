package com.nutrino.data.di

import com.nutrino.data.di.repoImpl.RepoImpl
import com.nutrino.domain.Repository.Repository
import com.nutrino.domain.usecase.GetAllMemesUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DiModule {

    @Provides
    @Singleton
    fun provideKtorClient(): HttpClient {
        val client = HttpClient(CIO){
            install(ContentNegotiation){
                json(Json{
                    ignoreUnknownKeys=true
                    isLenient = true
                    prettyPrint=true
                })
            }

        }
        return client
    }

    @Provides
    @Singleton
    fun providerepository(httpClient: HttpClient): Repository{
        return RepoImpl(httpClient = httpClient)
    }




}