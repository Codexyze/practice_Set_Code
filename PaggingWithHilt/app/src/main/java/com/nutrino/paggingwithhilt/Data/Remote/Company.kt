package com.nutrino.paggingwithhilt.Data.Remote

import kotlinx.serialization.Serializable

@Serializable
data class Company(
    val bs: String,
    val catchPhrase: String,
    val name: String
)