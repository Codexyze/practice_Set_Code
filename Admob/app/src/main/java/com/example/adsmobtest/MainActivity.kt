package com.example.adsmobtest

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adsmobtest.AdScreens.BannerAds
import com.example.adsmobtest.AdScreens.RewardedAdScreen
import com.example.adsmobtest.AdScreens.RewardedAdsManager
import com.example.adsmobtest.AdScreens.RewardedAdState
import com.example.adsmobtest.AdScreens.SafeInterstitialAdScreen
import com.example.adsmobtest.AdScreens.interstialADS
import com.example.adsmobtest.ui.theme.AdsMobTestTheme
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AdsMobTestTheme {
                // Hold points at the Activity composition root so they survive ad screen recompositions
                var points by remember { mutableStateOf(0) }
                AdDemoScreen(
                    addPoints = { amount, type ->
                        // Example business logic: increment local points; placeholder for API call
                        points += amount
                        // You could trigger a repository upload here.
                    },
                    currentPoints = points
                )
            }
        }
    }
}

@Composable
private fun AdDemoScreen(
    addPoints: (Int, String) -> Unit,
    currentPoints: Int,
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val activity = context as? Activity
    val manager = remember { RewardedAdsManager(context) }
    val state by manager.state.collectAsState()
    var backgroundColor by remember { mutableStateOf(Color(0xFFF2F2F2)) }
    var rewardMessage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) { manager.load() }

    // Update background when reward earned
    LaunchedEffect(state) {
        if (state is RewardedAdState.RewardEarned) {
            backgroundColor = Color.Blue
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Rewarded Ad Demo - Single Screen", style = MaterialTheme.typography.titleLarge)
        Text("Points: $currentPoints", style = MaterialTheme.typography.titleMedium, color = Color.DarkGray)
        when (val s = state) {
            RewardedAdState.Idle, RewardedAdState.Loading -> {
                RowLoading()
            }
            RewardedAdState.Loaded -> {
                Button(onClick = {
                    if (activity != null) {
                        manager.show(activity) { reward ->
                            rewardMessage = "Earned: ${reward.amount} ${reward.type}"
                            addPoints(reward.amount, reward.type)
                        }
                    }
                }) { Text("Show Rewarded Ad") }
            }
            RewardedAdState.Showing -> {
                Text("Ad is showing...")
            }
            is RewardedAdState.RewardEarned -> {
                if (rewardMessage.isEmpty()) rewardMessage = "Earned: ${s.amount} ${s.type}"
                Text(rewardMessage, color = Color.White)
                Button(onClick = {
                    // reset for next round
                    backgroundColor = Color(0xFFF2F2F2)
                    rewardMessage = ""
                    manager.load()
                }) { Text("Watch Again") }
            }
            RewardedAdState.Dismissed -> {
                Text("Ad dismissed (no reward)")
                Button(onClick = { manager.load() }) { Text("Load Again") }
            }
            is RewardedAdState.Error -> {
                Text("Error: ${s.message}", color = MaterialTheme.colorScheme.error)
                Button(onClick = { manager.load() }) { Text("Retry") }
            }
        }

        if (rewardMessage.isNotEmpty()) {
            Text("Status: $rewardMessage")
        }
        Spacer(Modifier.weight(1f))
        // Banner anchored at bottom
        Box(Modifier.fillMaxWidth()) { BannerAds() }
    }
}

@Composable
private fun RowLoading() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        CircularProgressIndicator(modifier = Modifier.height(24.dp))
        Spacer(Modifier.width(12.dp))
        Text("Loading rewarded ad...")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldExample() {
    var presses by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Top app bar")
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "Bottom app bar",
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { presses++ }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                modifier = Modifier.padding(8.dp),
                text =
                """
                    This is an example of a scaffold. It uses the Scaffold composable's parameters to create a screen with a simple top app bar, bottom app bar, and floating action button.

                    It also contains some basic inner content, such as this text.

                    You have pressed the floating action button $presses times.
                """.trimIndent(),
            )
        }
    }
}