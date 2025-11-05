package com.example.adsmobtest.AdScreens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError

@Composable
fun BannerAds(){
    AndroidView(factory = {
        AdView(it).apply {
            setAdSize(AdSize.BANNER)
            adUnitId="ca-app-pub-3940256099942544/9214589741"
           loadAd(AdRequest.Builder().build())
        }
    }, modifier = Modifier.fillMaxWidth())

}

//
//
//sealed class AdState {
//    object Idle : AdState()
//    object Loading : AdState()
//    object Loaded : AdState()
//    data class Failed(val error: String) : AdState()
//    object Opened : AdState()
//    object Closed : AdState()
//}
//
//@Composable
//fun BannerAds(
//    modifier: Modifier = Modifier.fillMaxWidth(),
//    onAdStateChange: (AdState) -> Unit = {}
//) {
//    val adState = remember { mutableStateOf<AdState>(AdState.Idle) }
//    val adViewRef = remember { mutableStateOf<AdView?>(null) }
//
//    // propagate state changes to the caller
//    LaunchedEffect(adState.value) {
//        onAdStateChange(adState.value)
//    }
//
//    AndroidView(
//        factory = { ctx ->
//            AdView(ctx).apply {
//                adSize = AdSize.BANNER
//                adUnitId = "ca-app-pub-3940256099942544/9214589741"
//                adListener = object : AdListener() {
//                    override fun onAdLoaded() {
//                        adState.value = AdState.Loaded
//                    }
//                    override fun onAdFailedToLoad(error: LoadAdError) {
//                        adState.value = AdState.Failed(error.message ?: "Unknown")
//                    }
//                    override fun onAdOpened() {
//                        adState.value = AdState.Opened
//                    }
//                    override fun onAdClosed() {
//                        adState.value = AdState.Closed
//                    }
//                }
//                adState.value = AdState.Loading
//                loadAd(AdRequest.Builder().build())
//                adViewRef.value = this
//            }
//        },
//        modifier = modifier
//    )
//
//    DisposableEffect(adViewRef.value) {
//        onDispose {
//            adViewRef.value?.destroy()
//            adViewRef.value = null
//        }
//    }
//}