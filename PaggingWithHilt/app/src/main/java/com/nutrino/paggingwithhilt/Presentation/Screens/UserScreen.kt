package com.nutrino.paggingwithhilt.Presentation.Screens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.nutrino.paggingwithhilt.Data.Remote.User
import com.nutrino.paggingwithhilt.Presentation.ViewModels.UserApiViewModel

@Composable
fun UserListScreen(viewModel: UserApiViewModel = hiltViewModel()) {
    // collectAsLazyPagingItems converts PagingData flow to items for LazyColumn
    val userPagingItems = viewModel.userPager.collectAsLazyPagingItems()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        // itemCount is dynamic, depends on loaded pages
        items(userPagingItems.itemCount) { index ->
            val user = userPagingItems[index] // get item at index
            user?.let { UserItem(user) } // display each user
        }
    }
}

// Individual user card
@Composable
fun UserItem(user: User, isDark: Boolean = isSystemInDarkTheme()) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp), // margin around card
        elevation = CardDefaults.cardElevation(4.dp) // shadow elevation
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Spacer(modifier = Modifier.width(16.dp)) // spacing before content
            Column {
                Text(text = user.name, style = MaterialTheme.typography.titleMedium)
                Text(text = user.email, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
