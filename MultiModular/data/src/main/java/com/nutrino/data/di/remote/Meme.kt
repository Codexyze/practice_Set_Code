package com.nutrino.data.di.remote

import com.nutrino.domain.mappers.Meme
import kotlinx.serialization.Serializable

@Serializable
data class MemeXDto(
    val author: String,
    val nsfw: Boolean,
    val postLink: String,
    val preview: List<String>,
    val spoiler: Boolean,
    val subreddit: String,
    val title: String,
    val ups: Int,
    val url: String
) {

}