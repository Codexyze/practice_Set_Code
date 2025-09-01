package com.nutrino.paggingwithhilt.Data.Remote

import kotlinx.serialization.Serializable

@Serializable
data class Geo(
    val lat: String,
    val lng: String
)