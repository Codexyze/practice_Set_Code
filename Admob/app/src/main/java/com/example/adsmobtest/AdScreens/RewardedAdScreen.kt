package com.example.adsmobtest.AdScreens

import android.app.Activity
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun RewardedAdScreen(
    modifier: Modifier = Modifier,
    autoShow: Boolean = false,
    onRewardEarned: (amount: Int, type: String) -> Unit = { _, _ -> },
    onDismissed: () -> Unit = {}
) {
    val context = LocalContext.current
    val manager = remember { RewardedAdsManager(context) }
    val state by manager.state.collectAsState()
    val activity = context as? Activity

    LaunchedEffect(Unit) { manager.load() }

    // Auto-show logic: when state becomes Loaded and autoShow enabled
    LaunchedEffect(autoShow, state) {
        if (autoShow && state is RewardedAdState.Loaded && activity != null) {
            manager.show(activity) { reward ->
                onRewardEarned(reward.amount, reward.type)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Rewarded Ad Demo",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        when (val s = state) {
            is RewardedAdState.Idle -> { Text("Idle") }
            is RewardedAdState.Loading -> {
                CircularProgressIndicator(); Spacer(modifier = Modifier.height(8.dp)); Text("Loading rewarded ad...")
            }
            is RewardedAdState.Loaded -> {
                if (!autoShow) {
                    Button(onClick = {
                        if (activity != null) {
                            manager.show(activity) { reward -> onRewardEarned(reward.amount, reward.type) }
                        }
                    }) { Text("Show Rewarded Ad") }
                } else {
                    Text("Ready - will show automatically")
                }
            }
            is RewardedAdState.Showing -> { Text("Ad is showing...") }
            is RewardedAdState.RewardEarned -> {
                // Already invoked onRewardEarned inside show(); avoid invoking again here.
                Text("Reward earned: ${s.amount} ${s.type}")
            }
            is RewardedAdState.Dismissed -> { Text("Ad dismissed"); onDismissed() }
            is RewardedAdState.Error -> {
                Text("Error: ${s.message}", color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { manager.load() }) { Text("Retry Load") }
            }
        }
    }
}
