package com.nutrino.domain.Repository
import com.nutrino.domain.mappers.Meme
import kotlinx.coroutines.flow.flow


import com.nutrino.domain.state.ResultState
import kotlinx.coroutines.flow.Flow


interface Repository {
    suspend fun getAllMemes(): Flow<ResultState<Meme?>>
}



