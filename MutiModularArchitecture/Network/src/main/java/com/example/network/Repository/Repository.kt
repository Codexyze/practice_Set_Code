package com.example.network.Repository

import android.util.Log
import com.example.network.Remote.Meme
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class Repository @Inject constructor(private val httpClient: HttpClient) {

    suspend fun getmemes() = flow{
        try {
            val memes=httpClient.get("https://meme-api.com/gimme/50").body<Meme>()
            Log.d("Memes", memes.toString())
            emit(memes)
        }catch (e:Exception){
            Log.d("Memes", e.toString())
            emit(null)
        }


    }


}