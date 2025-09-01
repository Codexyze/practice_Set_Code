package com.nutrino.paggingwithhilt.Domain.UseCases

import com.nutrino.paggingwithhilt.Data.Remote.User
import com.nutrino.paggingwithhilt.Domain.Repository.ApiServiceRepo
import javax.inject.Inject

class GetAllUserUseCase @Inject constructor(private val apiServiceRepo: ApiServiceRepo)  {
    suspend operator fun invoke(page: Int): List<User?>{
        return apiServiceRepo.getAllUsers(page = page)
    }
}