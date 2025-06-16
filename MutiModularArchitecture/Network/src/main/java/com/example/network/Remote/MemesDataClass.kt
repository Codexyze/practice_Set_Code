package com.example.network.Remote

import kotlinx.serialization.Serializable

@Serializable
data class Meme(
    val count: Int = 0,
    val memes: List<MemeX> = emptyList()
)
@Serializable
data class MemeX(
    val author: String = "",
    val nsfw: Boolean = false,
    val postLink: String = "",
    val preview: List<String> = emptyList(),
    val spoiler: Boolean = false,
    val subreddit: String = "",
    val title: String = "",
    val ups: Int = 0,
    val url: String = ""
)