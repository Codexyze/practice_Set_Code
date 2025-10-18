package com.nutrino.domain.mappers



// :domain module
data class Meme(
    val count: Int,
    val memes: List<MemeX>
)

data class MemeX(
    val author: String,
    val nsfw: Boolean,
    val postLink: String,
    val preview: List<String>,
    val spoiler: Boolean,
    val subreddit: String,
    val title: String,
    val ups: Int,
    val url: String
)



