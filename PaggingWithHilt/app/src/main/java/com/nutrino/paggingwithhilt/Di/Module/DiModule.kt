package com.nutrino.paggingwithhilt.Di.Module

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nutrino.paggingwithhilt.Data.Local.UserDatabase
import com.nutrino.paggingwithhilt.Data.RepoImpl.ApiServiceRepoImpl
import com.nutrino.paggingwithhilt.Domain.Repository.ApiServiceRepo
import com.nutrino.paggingwithhilt.Domain.UseCases.GetAllUserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DiModule {

    @Provides
    @Singleton
    fun RoomDatabaseObj(@ApplicationContext context: Context): UserDatabase{
        return Room.databaseBuilder(context = context, name = "my_db", klass = UserDatabase::class.java)
            .fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun HttpClientProvider(): HttpClient{
       val http = HttpClient(engineFactory = CIO){
           install(ContentNegotiation){
               json(
                   Json(){
                       this.isLenient = true
                       ignoreUnknownKeys = true
                   }
               )

           }
       }
        return  http
    }

    @Singleton
    fun apiServiceObj(httpClient: HttpClient): ApiServiceRepo{
        return ApiServiceRepoImpl(httpClient = httpClient)
    }

    @Singleton
    fun apiServiceUseCaseObj(apiServiceRepo: ApiServiceRepo): GetAllUserUseCase{
        return GetAllUserUseCase(apiServiceRepo = apiServiceRepo)
    }
}