package com.example.paging3.data.KtorClient

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object KtorClient {
    val ktorClent= HttpClient {
          install(ContentNegotiation){
              json(json = Json {
                  ignoreUnknownKeys = true
              })
          }
    }
}