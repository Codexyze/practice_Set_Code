package com.nutrino.presentation.states

import com.nutrino.domain.mappers.Meme

data class GetAllMemesState(
    val isLoading: Boolean = false ,
    val data:Meme ?= null ,
    val error: String =""
)