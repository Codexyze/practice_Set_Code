package com.nutrino.domain.usecase

import com.nutrino.domain.Repository.Repository
import com.nutrino.domain.mappers.Meme
import com.nutrino.domain.state.ResultState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetAllMemesUseCase @Inject constructor(
    private val repository: Repository
){
    suspend operator fun invoke(): Flow<ResultState<Meme?>> {
        return repository.getAllMemes()
    }
}