package com.nutrino.paggingwithhilt.Domain.Repository

import com.nutrino.paggingwithhilt.Data.Remote.User


interface ApiServiceRepo {

    suspend fun getAllUsers(page: Int): List<User?>

}