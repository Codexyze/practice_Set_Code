package com.nutrino.paggingwithhilt.Data.RepoImpl

import com.nutrino.paggingwithhilt.Data.Remote.User
import com.nutrino.paggingwithhilt.Domain.Repository.ApiServiceRepo
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import javax.inject.Inject



class ApiServiceRepoImpl @Inject constructor(private val httpClient: HttpClient) : ApiServiceRepo {
    override suspend fun getAllUsers(page: Int): List<User> {

        try {
            val data = httpClient.get("https://jsonplaceholder.typicode.com/users") {
                this.parameter("page", page)

            }.body<List<User>>()
            return data

        }catch (e: Exception){

            return emptyList()

        }


    }
}