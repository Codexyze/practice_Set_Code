package com.example.adsmobtest.AdScreens

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback


fun interstialADS(activity: Activity) {
    val adRequest = AdRequest.Builder().build()

    InterstitialAd.load(activity,"ca-app-pub-3940256099942544/1033173712", adRequest, object : InterstitialAdLoadCallback() {
        override fun onAdFailedToLoad(adError: LoadAdError) {
            adError?.toString()?.let { Log.d(TAG, it) }

        }

        override fun onAdLoaded(interstitialAd: InterstitialAd) {
            Log.d(TAG, "Ad was loaded.".toString())
            interstitialAd.show(activity)
        }
    })
}
@Composable
fun SafeInterstitialAdScreen() {
    val context = LocalContext.current
    val activity = context as? ComponentActivity

    var interstitialAd by remember { mutableStateOf<InterstitialAd?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var loadFailed by remember { mutableStateOf(false) }

    // Load Interstitial safely
    fun loadAd() {
        isLoading = true
        loadFailed = false

        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            context,
            "ca-app-pub-3940256099942544/1033173712", // Test ID
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    isLoading = false
                    loadFailed = false
                    Log.d("SafeInterstitialAd", "Ad Loaded")
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    interstitialAd = null
                    isLoading = false
                    loadFailed = true
                    Log.d("SafeInterstitialAd", "Failed: ${adError.message}")
                }
            }
        )
    }

    // Load ad on first composition
    LaunchedEffect(Unit) { loadAd() }

    // Clean up reference when Composable leaves
    DisposableEffect(Unit) {
        onDispose {
            interstitialAd = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Safe Interstitial Ad Screen",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        when {
            isLoading -> {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(8.dp))
                Text("Loading ad...")
            }
            loadFailed -> {
                Text("Ad failed to load", color = Color.Red)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { loadAd() }) {
                    Text("Retry")
                }
            }
            else -> {
                Button(
                    onClick = {
                        activity?.let { interstitialAd?.show(it) }
                        interstitialAd = null // Reset after showing
                        loadAd() // Preload next ad
                    },
                    enabled = interstitialAd != null
                ) {
                    Text("Show Interstitial Ad")
                }
            }
        }
    }
}

