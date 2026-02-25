package com.example.adsmobtest

import android.app.Activity
import android.os.Bundle
import android.util.Log
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.adsmobtest.AdScreens.BannerAds
import com.example.adsmobtest.AdScreens.RewardedAdsManager
import com.example.adsmobtest.AdScreens.RewardedAdState
import com.example.adsmobtest.ui.theme.AdsMobTestTheme
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize AdMob SDK on background thread
        CoroutineScope(Dispatchers.IO).launch {
            MobileAds.initialize(this@MainActivity) {}
        }

        setContent {
            AdsMobTestTheme {
                // Hold points at the Activity composition root so they survive recompositions
                var points by remember { mutableStateOf(0) }

                AdDemoScreen(
                    addPoints = { amount, type ->
                        // ── YOUR BUSINESS LOGIC / API CALL GOES HERE ──
                        points += amount
                        Log.d("MainActivity", "Added $amount $type → total: $points")
                        // Example: apiService.grantReward(userId, amount, type)
                    },
                    currentPoints = points
                )
            }
        }
    }
}

/**
 * Single-screen composable that handles rewarded ad loading, showing,
 * reward granting, dismiss, and error states.
 *
 * @param addPoints    Lambda invoked when the user earns a reward.
 *                     Receives (amount, type) from AdMob Console reward settings.
 *                     ► Put your API call / DB update / point logic here.
 * @param currentPoints  Current point total to display.
 */
@Composable
private fun AdDemoScreen(
    addPoints: (amount: Int, type: String) -> Unit,
    currentPoints: Int,
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val activity = context as? Activity
    val manager = remember { RewardedAdsManager(context) }
    val state by manager.state.collectAsState()

    // ✅ Use `remember` (NOT `rememberSaveable`) for Color — Color can't be saved in a Bundle
    var backgroundColor by remember { mutableStateOf(Color(0xFFF2F2F2)) }
    var rewardMessage by remember { mutableStateOf("") }

    // Start loading the ad immediately
    LaunchedEffect(Unit) { manager.load() }

    // Change background to blue when reward is earned
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
        Text("Rewarded Ad Demo", style = MaterialTheme.typography.titleLarge)
        Text(
            "Points: $currentPoints",
            style = MaterialTheme.typography.titleMedium,
            color = Color.DarkGray
        )

        when (val s = state) {
            // ── IDLE / LOADING ──
            RewardedAdState.Idle, RewardedAdState.Loading -> {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    Spacer(Modifier.width(12.dp))
                    Text("Loading rewarded ad...")
                }
            }

            // ── LOADED — show "Watch Ad" button ──
            RewardedAdState.Loaded -> {
                Button(onClick = {
                    if (activity != null) {
                        manager.show(activity) { reward ->
                            // reward.amount → from AdMob Console "Reward amount"
                            // reward.type   → from AdMob Console "Reward item"
                            rewardMessage = "Earned: ${reward.amount} ${reward.type}"
                            addPoints(reward.amount, reward.type)
                        }
                    }
                }) { Text("🎬 Watch Ad to Earn Reward") }
            }

            // ── AD IS PLAYING ──
            RewardedAdState.Showing -> {
                Text("Ad is showing...")
            }

            // ── REWARD EARNED ──
            is RewardedAdState.RewardEarned -> {
                if (rewardMessage.isEmpty()) rewardMessage = "Earned: ${s.amount} ${s.type}"
                Text(rewardMessage, color = Color.White, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                Button(onClick = {
                    // Reset for next round
                    backgroundColor = Color(0xFFF2F2F2)
                    rewardMessage = ""
                    manager.load()
                }) { Text("Watch Again") }
            }

            // ── DISMISSED (user closed without completing) ──
            RewardedAdState.Dismissed -> {
                Text("Ad dismissed — no reward earned", color = Color.Gray)
                Button(onClick = { manager.load() }) { Text("Try Again") }
            }

            // ── ERROR ──
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

