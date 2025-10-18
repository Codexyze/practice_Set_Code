package com.nutrino.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.nutrino.domain.mappers.Meme
import com.nutrino.domain.mappers.MemeX
import com.nutrino.presentation.viewmodels.MemeViewModel
//
//@Composable
//fun GetAllMemeScreen(
//    memeViewModel: MemeViewModel = hiltViewModel()
//) {
//
//    val getAllMemeState = memeViewModel.getAllMemesState.collectAsState()
//    when{
//        getAllMemeState.value.data != null -> {
//            Text(
//                text = "Total Memes: ${getAllMemeState.value.data?.count}"
//            )
//        }
//        getAllMemeState.value.isLoading->{
//            LinearProgressIndicator()
//        }
//
//    }
//
//}

@Composable
fun GetAllMemeScreen(
    memeViewModel: MemeViewModel = hiltViewModel()
) {
    val getAllMemeState by memeViewModel.getAllMemesState.collectAsState()

    when {
        getAllMemeState.isLoading -> {
            // Centered Progress Bar
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LinearProgressIndicator()
            }
        }

        getAllMemeState.error != null && !getAllMemeState.error.isEmpty() -> {
            // Show Error Text
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = getAllMemeState.error,
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
            }
        }

        getAllMemeState.data != null -> {
            val memes = getAllMemeState.data!!.memes

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(memes) { meme ->
                    MemeCard(memeItem = meme)
                }
            }
        }
    }
}
@Composable
fun MemeCard(memeItem: MemeX) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        elevation = CardDefaults.elevatedCardElevation(8.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = memeItem.title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Author: ${memeItem.author}",
                fontSize = 12.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Show Image
            AsyncImage(
                model = memeItem.url,
                contentDescription = memeItem.title,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        }
    }
}
