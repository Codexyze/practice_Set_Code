package com.example.mutimodulararchitecture.AppViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.network.Remote.Meme
import com.example.network.Repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(private val repository: Repository):ViewModel() {
    private val _memesState = MutableStateFlow<Meme>(Meme())
    val memesState = _memesState.asStateFlow()

    init {
        getmemes()

    }
    fun getmemes(){
        viewModelScope.launch {
            repository.getmemes().collect{ memes ->
                if (memes != null) {
                    _memesState.value = memes
                }
            }
        }

    }

}