package com.example.mutimodulararchitecture.Screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mutimodulararchitecture.AppViewModel.MyViewModel

@Composable
fun MemeScreen(viewModel: MyViewModel= hiltViewModel()) {
    LaunchedEffect(Unit){
        viewModel.getmemes()
    }
    val memeState = viewModel.memesState.collectAsState()
    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            items(memeState.value.memes){ meme ->
                Text(meme.url.toString())
            }
        }
    }

}