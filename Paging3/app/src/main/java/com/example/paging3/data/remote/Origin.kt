package com.example.paging3.data.remote
import kotlinx.serialization.Serializable

@Serializable
data class Origin(
    val name: String,
    val url: String
)