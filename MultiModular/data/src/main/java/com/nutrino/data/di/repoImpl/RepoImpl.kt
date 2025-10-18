package com.nutrino.data.di.repoImpl

import com.nutrino.data.di.mappers.toDomain
import com.nutrino.data.di.remote.MemeDto
import com.nutrino.domain.Repository.Repository
import com.nutrino.domain.mappers.Meme
import com.nutrino.domain.state.ResultState
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class RepoImpl(private val httpClient: HttpClient): Repository {
    override suspend fun getAllMemes(): Flow<ResultState<Meme?>> = flow{
        emit(ResultState.Loading)
        try {
            val memeDto = httpClient.get("https://meme-api.com/gimme/50").body<MemeDto>()
            val result =  memeDto.toDomain()
            emit(ResultState.Success(
                result
            ))

        }catch (e: Exception){
            emit(ResultState.Error("${e.toString()}"))

        }




    }

}