package com.example.adsmobtest.AdScreens

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Manager class to encapsulate RewardedAd loading/showing logic.
 */
class RewardedAdsManager(private val context: Context) {
    private var rewardedAd: RewardedAd? = null

    private val _state = MutableStateFlow<RewardedAdState>(RewardedAdState.Idle)
    val state: StateFlow<RewardedAdState> = _state

    fun load(adUnitId: String = TEST_AD_UNIT_ID) {
        if (_state.value is RewardedAdState.Loading) return
        _state.value = RewardedAdState.Loading
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(context, adUnitId, adRequest, object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(TAG, "RewardedAd failed: ${adError.message}")
                rewardedAd = null
                _state.value = RewardedAdState.Error(adError.message ?: "Unknown error")
            }
            override fun onAdLoaded(ad: RewardedAd) {
                Log.d(TAG, "RewardedAd loaded")
                rewardedAd = ad
                _state.value = RewardedAdState.Loaded
                setFullScreenCallbacks(ad)
            }
        })
    }

    private fun setFullScreenCallbacks(ad: RewardedAd) {
        ad.fullScreenContentCallback = object : com.google.android.gms.ads.FullScreenContentCallback() {
            override fun onAdShowedFullScreenContent() {
                _state.value = RewardedAdState.Showing
            }
            override fun onAdDismissedFullScreenContent() {
                rewardedAd = null
                _state.value = RewardedAdState.Dismissed
                // Preload next
                load()
            }
            override fun onAdFailedToShowFullScreenContent(p0: com.google.android.gms.ads.AdError) {
                rewardedAd = null
                _state.value = RewardedAdState.Error(p0.message ?: "Show failed")
            }
        }
    }

    fun show(activity: Activity, onReward: (RewardItem) -> Unit) {
        val ad = rewardedAd ?: run {
            _state.value = RewardedAdState.Error("Ad not ready")
            return
        }
        ad.show(activity, OnUserEarnedRewardListener { rewardItem ->
            Log.d(TAG, "User earned reward: ${rewardItem.amount} ${rewardItem.type}")
            onReward(rewardItem)
            _state.value = RewardedAdState.RewardEarned(rewardItem.amount, rewardItem.type)
        })
    }

    companion object {
        private const val TAG = "RewardedAdsManager"
        // Google test rewarded ad unit id
        const val TEST_AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917"
    }
}

sealed class RewardedAdState {
    object Idle : RewardedAdState()
    object Loading : RewardedAdState()
    object Loaded : RewardedAdState()
    object Showing : RewardedAdState()
    data class RewardEarned(val amount: Int, val type: String) : RewardedAdState()
    object Dismissed : RewardedAdState()
    data class Error(val message: String) : RewardedAdState()
}

