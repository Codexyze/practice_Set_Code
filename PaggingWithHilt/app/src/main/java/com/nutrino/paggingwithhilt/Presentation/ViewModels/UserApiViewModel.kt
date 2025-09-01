package com.nutrino.paggingwithhilt.Presentation.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.nutrino.paggingwithhilt.Data.Local.UserDatabase
import com.nutrino.paggingwithhilt.Data.Pagging.UserPaggingSouce
import com.nutrino.paggingwithhilt.Data.Remote.User
import com.nutrino.paggingwithhilt.Domain.UseCases.GetAllUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserApiViewModel @Inject constructor(private val apiService: GetAllUserUseCase,
    private val database: UserDatabase): ViewModel( ) {
    val userPager = Pager(
        config = PagingConfig(pageSize = 10) ,
        pagingSourceFactory = {
            UserPaggingSouce(
                apiUserUseCase = apiService,
                database = database
            )

        }
    ).flow.cachedIn(viewModelScope)


}