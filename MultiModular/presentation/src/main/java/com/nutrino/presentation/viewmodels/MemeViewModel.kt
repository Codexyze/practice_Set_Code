package com.nutrino.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutrino.domain.state.ResultState
import com.nutrino.domain.usecase.GetAllMemesUseCase
import com.nutrino.presentation.states.GetAllMemesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemeViewModel
    @Inject constructor(private val getAllMemesUseCase: GetAllMemesUseCase)
    : ViewModel()
{
        private val _getAllMemesState = MutableStateFlow(GetAllMemesState())
        val getAllMemesState = _getAllMemesState.asStateFlow()
init {
    getAllMemes()
}

       fun getAllMemes(){
           viewModelScope.launch (Dispatchers.IO){
               getAllMemesUseCase.invoke().collect {result->
                   when(result){
                       is ResultState.Loading->{
                            _getAllMemesState.value = GetAllMemesState(isLoading = true)
                       }
                       is ResultState.Success->{
                           _getAllMemesState.value = GetAllMemesState(data = result.data, isLoading = false)
                       }
                       is ResultState.Error->{
                           _getAllMemesState.value = GetAllMemesState(error = result.message ?: "An unexpected error occurred",
                               isLoading = false)
                       }
                   }

               }
           }

       }



}