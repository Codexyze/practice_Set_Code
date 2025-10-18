package com.nutrino.data.di.remote

import com.nutrino.domain.mappers.Meme
import com.nutrino.domain.mappers.MemeX
import kotlinx.serialization.Serializable

@Serializable
data class MemeDto(
    val count: Int,
    val memes: List<MemeXDto>
)
