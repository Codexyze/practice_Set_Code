package com.nutrino.paggingwithhilt.Data.Pagging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.room.RoomDatabase
import com.nutrino.paggingwithhilt.Data.Local.UserDatabase
import com.nutrino.paggingwithhilt.Data.Remote.User
import com.nutrino.paggingwithhilt.Domain.UseCases.GetAllUserUseCase
import io.ktor.client.HttpClient
import javax.inject.Inject
