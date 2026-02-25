# 📘 AdMob Rewarded Ads — Complete Implementation Guide

> **Project:** AdsMobTest  
> **Language:** Kotlin · Jetpack Compose  
> **AdMob SDK:** `com.google.android.gms:play-services-ads:23.6.0`

---

## Table of Contents

1. [What Are Rewarded Ads?](#1-what-are-rewarded-ads)
2. [How Rewards Work — The `RewardItem` Interface](#2-how-rewards-work--the-rewarditem-interface)
3. [AdMob Console Setup (Step-by-Step)](#3-admob-console-setup-step-by-step)
4. [Pre-requisites in Your Android Project](#4-pre-requisites-in-your-android-project)
5. [Architecture Overview](#5-architecture-overview)
6. [Full Code — `RewardedAdsManager.kt`](#6-full-code--rewardedadsmanagerkt)
7. [Full Code — `RewardedAdScreen.kt` (Jetpack Compose UI)](#7-full-code--rewardedadscreenkt-jetpack-compose-ui)
8. [Full Code — `MainActivity.kt` (Wiring It All Together)](#8-full-code--mainactivitykt-wiring-it-all-together)
9. [How to Fetch Reward Data on Success](#9-how-to-fetch-reward-data-on-success)
10. [Handling All Ad Events (Success, Cancel, Error)](#10-handling-all-ad-events-success-cancel-error)
11. [Real-World Example — Granting Points via API](#11-real-world-example--granting-points-via-api)
12. [Common Crashes & Fixes](#12-common-crashes--fixes)
13. [Testing Checklist](#13-testing-checklist)
14. [FAQ](#14-faq)

---

## 1. What Are Rewarded Ads?

Rewarded ads are **full-screen video ads** that the user **opts-in** to watch. In return, the user receives an **in-app reward** (coins, points, extra lives, premium content, etc.).

**Flow:**
```
User clicks "Watch Ad" → Ad loads & plays full-screen → User watches to completion
→ OnUserEarnedRewardListener fires → You receive RewardItem { amount, type }
→ You grant the reward (add points, unlock feature, call API, etc.)
```

If the user **dismisses** the ad before completion, the reward callback is **NOT** fired. You only get the `onAdDismissedFullScreenContent` callback.

---

## 2. How Rewards Work — The `RewardItem` Interface

When a user **successfully watches** a rewarded ad to completion, AdMob invokes the `OnUserEarnedRewardListener` with a `RewardItem`:

```kotlin
public interface RewardItem {
    int getAmount();  // The reward quantity (e.g., 10)
    String getType(); // The reward identifier (e.g., "coins")
}
```

### Where Do `amount` and `type` Come From?

They come from **what you configure in the AdMob Console** when creating the ad unit:

| Field               | Console Label         | Example Value | Maps To              |
|---------------------|-----------------------|---------------|----------------------|
| Reward amount       | "Enter reward amount" | `10`          | `rewardItem.amount`  |
| Reward item / type  | "Enter reward item"   | `coins`       | `rewardItem.type`    |

> 🔑 **Key Insight:** You set these values **once** in AdMob Console per ad unit. The SDK delivers them to your app automatically when the user earns the reward. You **do not** hardcode them in your app — you **read** them from the callback.

### Example: Reading the Reward

```kotlin
rewardedAd.show(activity) { rewardItem ->
    val amount = rewardItem.amount  // e.g., 10
    val type   = rewardItem.type    // e.g., "coins"

    // Now do something with it:
    when (type) {
        "coins"  -> addCoins(amount)
        "gems"   -> addGems(amount)
        "lives"  -> addLives(amount)
        else     -> addGenericPoints(amount)
    }
}
```

---

## 3. AdMob Console Setup (Step-by-Step)

### Step 1: Create an AdMob Account
1. Go to [https://admob.google.com](https://admob.google.com)
2. Sign in with your Google account
3. Accept the Terms of Service

### Step 2: Register Your App
1. Click **Apps** → **Add App**
2. Select platform: **Android**
3. If published on Play Store, search for it. Otherwise select **"No, I haven't published"**
4. Enter your app name → Click **Add**
5. Note down the **App ID** (format: `ca-app-pub-XXXXXXXXXXXXXXXX~YYYYYYYYYY`)

### Step 3: Create a Rewarded Ad Unit
1. In your app page, click **Ad units** → **Add ad unit**
2. Select **Rewarded** ad format
3. Configure:

   | Setting              | What to Enter                        | Example             |
   |----------------------|--------------------------------------|----------------------|
   | **Ad unit name**     | A descriptive name (internal only)   | `reward_watch_video` |
   | **Reward amount**    | How many units to grant per watch    | `1` or `10` or `50`  |
   | **Reward item**      | Label/type of the reward             | `coins` or `gems`    |

   > ⚠️ These values are what your app receives in `rewardItem.amount` and `rewardItem.type`!

4. Leave **Partner bidding** unchecked (unless using a mediation partner)
5. Click **Create ad unit**
6. Note down the **Ad Unit ID** (format: `ca-app-pub-XXXXXXXXXXXXXXXX/YYYYYYYYYY`)

### Step 4: Use Test Ad Unit IDs During Development!

> 🚨 **NEVER use production ad unit IDs during development.** Your account can get banned.

| Ad Format      | Test Ad Unit ID                           |
|----------------|-------------------------------------------|
| **Rewarded**   | `ca-app-pub-3940256099942544/5224354917`   |
| Banner         | `ca-app-pub-3940256099942544/9214589741`   |
| Interstitial   | `ca-app-pub-3940256099942544/1033173712`   |

When using test IDs, the reward item returned is always `{ amount: 10, type: "coins" }` (Google's default test reward).

### Step 5: Replace Test IDs with Production IDs Before Publishing

In your app code and `AndroidManifest.xml`:
- `AndroidManifest.xml` → replace the `meta-data` value with your **real App ID**
- `RewardedAdsManager.kt` → replace `TEST_AD_UNIT_ID` with your **real Ad Unit ID**

---

## 4. Pre-requisites in Your Android Project

### 4.1 Gradle Dependency

In `app/build.gradle.kts`:
```kotlin
dependencies {
    implementation("com.google.android.gms:play-services-ads:23.6.0")
}
```

### 4.2 AndroidManifest.xml

```xml
<application ...>
    <!-- Your real App ID (use test ID during development) -->
    <meta-data
        android:name="com.google.android.gms.ads.APPLICATION_ID"
        android:value="ca-app-pub-3940256099942544~3347511713"/>
</application>
```

### 4.3 Initialize the SDK (in `MainActivity.onCreate`)

```kotlin
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize on a background thread (recommended by Google)
        CoroutineScope(Dispatchers.IO).launch {
            MobileAds.initialize(this@MainActivity) {}
        }
        // ... setContent { }
    }
}
```

---

## 5. Architecture Overview

```
┌──────────────────────────────────────────────────────────┐
│                    MainActivity                          │
│  ┌────────────────────────────────────────────────────┐  │
│  │  var points by remember { mutableStateOf(0) }      │  │
│  │                                                    │  │
│  │  AdDemoScreen(                                     │  │
│  │      addPoints = { amount, type ->                 │  │
│  │          points += amount                          │  │
│  │          // call your API / update DB here         │  │
│  │      },                                            │  │
│  │      currentPoints = points                        │  │
│  │  )                                                 │  │
│  └────────────────────────────────────────────────────┘  │
│                          │                               │
│                          ▼                               │
│  ┌────────────────────────────────────────────────────┐  │
│  │           AdDemoScreen (Composable)                │  │
│  │  ┌──────────────────────────────────────────────┐  │  │
│  │  │      RewardedAdsManager (StateFlow)          │  │  │
│  │  │                                              │  │  │
│  │  │  Idle → Loading → Loaded → Showing           │  │  │
│  │  │                     ↓           ↓            │  │  │
│  │  │              RewardEarned    Dismissed        │  │  │
│  │  │                  ↓                           │  │  │
│  │  │          addPoints(amount, type) called       │  │  │
│  │  └──────────────────────────────────────────────┘  │  │
│  └────────────────────────────────────────────────────┘  │
└──────────────────────────────────────────────────────────┘
```

**State Machine:**

```
Idle ──→ Loading ──→ Loaded ──→ Showing ──┬──→ RewardEarned
              ↑          ↑                 │
              │          │                 └──→ Dismissed
              │          │                          │
              └──────────┴──────── (auto-reload) ◄──┘
              
         Error (from any load/show failure)
              │
              └──→ Retry → Loading
```

---

## 6. Full Code — `RewardedAdsManager.kt`

```kotlin
package com.example.adsmobtest.AdScreens

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Manages the lifecycle of a Rewarded Ad:
 *   load() → show() → reward / dismiss / error
 *
 * Exposes a [StateFlow] of [RewardedAdState] so Compose UI can react.
 */
class RewardedAdsManager(private val context: Context) {

    private var rewardedAd: RewardedAd? = null

    private val _state = MutableStateFlow<RewardedAdState>(RewardedAdState.Idle)
    val state: StateFlow<RewardedAdState> = _state

    /**
     * Loads a rewarded ad. Uses the Google test ad unit ID by default.
     * Replace with your production ad unit ID before publishing.
     */
    fun load(adUnitId: String = TEST_AD_UNIT_ID) {
        if (_state.value is RewardedAdState.Loading) return   // prevent duplicate loads
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
                attachFullScreenCallbacks(ad)
            }
        })
    }

    /**
     * Attaches dismiss / error / show callbacks to the loaded ad.
     */
    private fun attachFullScreenCallbacks(ad: RewardedAd) {
        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdShowedFullScreenContent() {
                _state.value = RewardedAdState.Showing
            }

            override fun onAdDismissedFullScreenContent() {
                rewardedAd = null
                // Only move to Dismissed if a reward wasn't already earned
                if (_state.value !is RewardedAdState.RewardEarned) {
                    _state.value = RewardedAdState.Dismissed
                }
                // Preload the next ad automatically
                load()
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                rewardedAd = null
                _state.value = RewardedAdState.Error(adError.message ?: "Show failed")
            }
        }
    }

    /**
     * Shows the loaded rewarded ad.
     *
     * @param activity  The Activity context needed to show the ad.
     * @param onReward  Lambda called when the user earns a reward.
     *                  Receives the [RewardItem] which contains:
     *                    - amount: Int  (configured in AdMob Console as "Reward amount")
     *                    - type: String (configured in AdMob Console as "Reward item")
     */
    fun show(activity: Activity, onReward: (RewardItem) -> Unit) {
        val ad = rewardedAd ?: run {
            _state.value = RewardedAdState.Error("Ad not ready")
            return
        }
        ad.show(activity, OnUserEarnedRewardListener { rewardItem ->
            // ─── THIS IS WHERE YOU RECEIVE THE REWARD DATA ───
            // rewardItem.amount → the number you set in AdMob Console
            // rewardItem.type   → the label you set in AdMob Console
            Log.d(TAG, "User earned reward: ${rewardItem.amount} ${rewardItem.type}")
            onReward(rewardItem)
            _state.value = RewardedAdState.RewardEarned(rewardItem.amount, rewardItem.type)
        })
    }

    companion object {
        private const val TAG = "RewardedAdsManager"
        // Google's test rewarded ad unit ID — always returns amount=10, type="coins"
        const val TEST_AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917"
    }
}

/**
 * Sealed class representing every possible state of a rewarded ad lifecycle.
 */
sealed class RewardedAdState {
    /** Initial state — nothing has happened yet. */
    object Idle : RewardedAdState()

    /** Ad request sent, waiting for response. */
    object Loading : RewardedAdState()

    /** Ad loaded and ready to show. */
    object Loaded : RewardedAdState()

    /** Ad is currently being displayed full-screen. */
    object Showing : RewardedAdState()

    /**
     * User watched the ad and earned a reward.
     * @param amount The reward quantity (from AdMob Console "Reward amount").
     * @param type   The reward label (from AdMob Console "Reward item").
     */
    data class RewardEarned(val amount: Int, val type: String) : RewardedAdState()

    /** User dismissed the ad without earning a reward. */
    object Dismissed : RewardedAdState()

    /** Something went wrong during load or show. */
    data class Error(val message: String) : RewardedAdState()
}
```

---

## 7. Full Code — `RewardedAdScreen.kt` (Jetpack Compose UI)

This is a **single-screen** composable that handles everything: loading indicator, show button, reward display, error/retry, and background color change.

```kotlin
package com.example.adsmobtest.AdScreens

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

/**
 * A self-contained screen that demonstrates rewarded ads.
 *
 * @param addPoints   Lambda called when the user earns a reward.
 *                    Receives (amount: Int, type: String) from the AdMob reward.
 *                    ► This is where you call your API, update your database,
 *                      or modify local state to grant the reward.
 * @param currentPoints  The current points to display on screen.
 */
@Composable
fun AdDemoScreen(
    addPoints: (amount: Int, type: String) -> Unit,
    currentPoints: Int,
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val manager = remember { RewardedAdsManager(context) }
    val state by manager.state.collectAsState()

    // Use remember (NOT rememberSaveable) for Color — Color can't be saved in a Bundle
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
            RewardedAdState.Idle,
            RewardedAdState.Loading -> {
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
                            // ──────────────────────────────────────────────
                            // reward.amount → e.g. 10  (from AdMob Console)
                            // reward.type   → e.g. "coins" (from AdMob Console)
                            // ──────────────────────────────────────────────
                            rewardMessage = "Earned: ${reward.amount} ${reward.type}"
                            addPoints(reward.amount, reward.type)
                        }
                    }
                }) {
                    Text("🎬 Watch Ad to Earn Reward")
                }
            }

            // ── AD IS PLAYING ──
            RewardedAdState.Showing -> {
                Text("Ad is showing...")
            }

            // ── REWARD EARNED ──
            is RewardedAdState.RewardEarned -> {
                if (rewardMessage.isEmpty()) {
                    rewardMessage = "Earned: ${s.amount} ${s.type}"
                }
                Text(rewardMessage, color = Color.White, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                Button(onClick = {
                    backgroundColor = Color(0xFFF2F2F2) // reset background
                    rewardMessage = ""
                    manager.load() // reload for next watch
                }) {
                    Text("Watch Again")
                }
            }

            // ── DISMISSED (user closed without completing) ──
            RewardedAdState.Dismissed -> {
                Text("Ad dismissed — no reward earned", color = Color.Gray)
                Button(onClick = { manager.load() }) {
                    Text("Try Again")
                }
            }

            // ── ERROR ──
            is RewardedAdState.Error -> {
                Text("Error: ${s.message}", color = MaterialTheme.colorScheme.error)
                Button(onClick = { manager.load() }) {
                    Text("Retry")
                }
            }
        }

        Spacer(Modifier.weight(1f))
        // Banner ad at the bottom
        Box(Modifier.fillMaxWidth()) { BannerAds() }
    }
}
```

---

## 8. Full Code — `MainActivity.kt` (Wiring It All Together)

```kotlin
package com.example.adsmobtest

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import com.example.adsmobtest.AdScreens.AdDemoScreen
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
                // Hold points at Activity-level so they survive recompositions
                var points by remember { mutableStateOf(0) }

                AdDemoScreen(
                    addPoints = { amount, type ->
                        // ─── YOUR BUSINESS LOGIC GOES HERE ───
                        points += amount
                        Log.d("MainActivity", "Added $amount $type → total: $points")

                        // Example: call your backend API
                        // apiService.addReward(userId, amount, type)
                    },
                    currentPoints = points
                )
            }
        }
    }
}
```

---

## 9. How to Fetch Reward Data on Success

### The Data Flow

```
AdMob Console                          Your App
┌─────────────────────┐          ┌─────────────────────────────┐
│ Reward amount: 10   │ ──SDK──► │ rewardItem.amount → 10      │
│ Reward item: coins  │ ──SDK──► │ rewardItem.type   → "coins" │
└─────────────────────┘          └─────────────────────────────┘
                                           │
                                           ▼
                                 ┌─────────────────────────────┐
                                 │ addPoints(10, "coins")       │
                                 │   → points += 10             │
                                 │   → callApi(userId, 10,      │
                                 │              "coins")         │
                                 └─────────────────────────────┘
```

### Step-by-step:

1. **User taps "Watch Ad"** → `manager.show(activity) { reward -> ... }` is called
2. **Ad plays full-screen** → `onAdShowedFullScreenContent()` fires
3. **User watches to completion** → `OnUserEarnedRewardListener` fires with `RewardItem`
4. **Your callback receives:**
   ```kotlin
   reward.amount  // Int  → the "Reward amount" from AdMob Console
   reward.type    // String → the "Reward item" from AdMob Console
   ```
5. **You act on it:**
   ```kotlin
   addPoints(reward.amount, reward.type)
   ```
6. **Ad closes** → `onAdDismissedFullScreenContent()` fires
7. **Manager auto-reloads** the next ad

### What if the user dismisses early?

- `OnUserEarnedRewardListener` is **NOT** called
- `onAdDismissedFullScreenContent()` **IS** called
- State moves to `RewardedAdState.Dismissed`
- No reward is granted

---

## 10. Handling All Ad Events (Success, Cancel, Error)

| Event | Callback | State | What to Do |
|-------|----------|-------|------------|
| Ad loaded successfully | `onAdLoaded()` | `Loaded` | Enable the "Watch Ad" button |
| Ad failed to load | `onAdFailedToLoad(error)` | `Error` | Show error message + retry button |
| Ad started showing | `onAdShowedFullScreenContent()` | `Showing` | Show "Ad is playing..." text |
| **User earned reward** | **`OnUserEarnedRewardListener`** | **`RewardEarned`** | **Grant reward, change UI, call API** |
| User dismissed (no reward) | `onAdDismissedFullScreenContent()` | `Dismissed` | Show "No reward" message |
| Ad failed to show | `onAdFailedToShowFullScreenContent(error)` | `Error` | Show error + retry |

### Important: Reward vs. Dismiss Timing

The `OnUserEarnedRewardListener` fires **BEFORE** `onAdDismissedFullScreenContent()`. This means:

```
Timeline:
  show() called
    → onAdShowedFullScreenContent()         [State: Showing]
    → (user watches video...)
    → OnUserEarnedRewardListener fires      [State: RewardEarned]  ← reward granted here
    → onAdDismissedFullScreenContent()      [State: stays RewardEarned, NOT Dismissed]
```

That's why in the manager we check:
```kotlin
override fun onAdDismissedFullScreenContent() {
    rewardedAd = null
    // Only move to Dismissed if a reward wasn't already earned
    if (_state.value !is RewardedAdState.RewardEarned) {
        _state.value = RewardedAdState.Dismissed
    }
    load() // preload next
}
```

---

## 11. Real-World Example — Granting Points via API

Here's how the `addPoints` lambda can be wired up to a real backend:

```kotlin
// In MainActivity
AdDemoScreen(
    addPoints = { amount, type ->
        // 1. Update local state immediately (optimistic update)
        points += amount

        // 2. Call your backend API
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = myApiService.grantReward(
                    userId = currentUser.id,
                    rewardAmount = amount,
                    rewardType = type,
                    adUnitId = "your-ad-unit-id",
                    timestamp = System.currentTimeMillis()
                )
                if (!response.isSuccessful) {
                    // Rollback on failure
                    withContext(Dispatchers.Main) {
                        points -= amount
                        showError("Failed to save reward")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    points -= amount
                    showError("Network error: ${e.message}")
                }
            }
        }
    },
    currentPoints = points
)
```

### Different Reward Types — Branching Logic

If you have multiple ad units with different reward types configured in AdMob Console:

```kotlin
addPoints = { amount, type ->
    when (type) {
        "coins" -> {
            userCoins += amount
            analytics.logEvent("reward_coins", amount)
        }
        "gems" -> {
            userGems += amount
            unlockPremiumContent()
        }
        "extra_life" -> {
            userLives += amount
            resumeGame()
        }
        else -> {
            // Fallback for unknown types
            userCoins += amount
        }
    }
}
```

---

## 12. Common Crashes & Fixes

### ❌ Crash: `MutableState containing Color cannot be saved using SaveableStateRegistry`

**Cause:** Using `rememberSaveable` with `Color` — `Color` is not `Parcelable`/`Serializable`.

**Fix:** Use `remember` instead of `rememberSaveable` for `Color` state:

```kotlin
// ❌ WRONG — will crash
var backgroundColor by rememberSaveable { mutableStateOf(Color.Red) }

// ✅ CORRECT
var backgroundColor by remember { mutableStateOf(Color.Red) }
```

### ❌ Crash: `IllegalStateException` when showing ad

**Cause:** Passing a non-Activity context or the Activity is destroyed.

**Fix:** Always check:
```kotlin
val activity = context as? Activity
if (activity != null && !activity.isFinishing) {
    manager.show(activity) { ... }
}
```

### ❌ Ad never loads / always errors

**Causes:**
- No internet connection
- Using production ad unit ID in debug builds (use test IDs!)
- AdMob account not fully set up (payment info, tax forms)
- Device not registered as a test device

---

## 13. Testing Checklist

- [ ] Using **test ad unit ID** (`ca-app-pub-3940256099942544/5224354917`) during development
- [ ] `AndroidManifest.xml` has the correct `APPLICATION_ID` meta-data
- [ ] `MobileAds.initialize()` is called before loading any ads
- [ ] Ad loads successfully (check Logcat for `"RewardedAd loaded"`)
- [ ] Tapping "Watch Ad" opens the full-screen ad
- [ ] Watching to completion triggers the reward callback
- [ ] Points increase after reward
- [ ] Background turns blue after reward
- [ ] Dismissing early shows "no reward" message
- [ ] "Watch Again" / "Retry" buttons reload the ad
- [ ] No crashes on configuration change (rotation)
- [ ] Replace test IDs with production IDs before release

---

## 14. FAQ

### Q: Can I change the reward amount/type without updating my app?
**A:** Yes! The reward `amount` and `type` are fetched from AdMob servers at runtime. You change them in the AdMob Console, and your app automatically receives the new values next time an ad loads. No app update needed.

### Q: What happens if the user has no internet?
**A:** `onAdFailedToLoad()` will fire with an error. The state becomes `Error`, and the user sees a "Retry" button.

### Q: Can I have multiple rewarded ad units with different rewards?
**A:** Yes! Create multiple ad units in AdMob Console, each with different reward settings. Pass different ad unit IDs to `manager.load(adUnitId)`.

### Q: Why does the test ad always give 10 coins?
**A:** Google's test ad unit hardcodes `amount=10, type="coins"`. Your production ad unit will return whatever you configured in the Console.

### Q: Should I validate rewards server-side?
**A:** For production apps with real currency/value, **YES**. Use [Server-Side Verification (SSV)](https://developers.google.com/admob/android/rewarded#server-side_verification). AdMob can send a callback to your server when a reward is granted, preventing client-side cheating.

### Q: How do I set up Server-Side Verification (SSV)?

1. In AdMob Console → Ad unit settings → Enable **Server-side verification**
2. Enter your **callback URL** (your backend endpoint)
3. In your app code:
   ```kotlin
   val ssv = ServerSideVerificationOptions.Builder()
       .setCustomData("userId=12345")
       .setUserId("12345")
       .build()
   rewardedAd.setServerSideVerificationOptions(ssv)
   ```
4. Your server receives a GET request from Google with the reward details + a signature to verify.

---

## Summary

| What | Where |
|------|-------|
| Set reward amount & type | **AdMob Console** → Ad unit → Reward settings |
| Read reward data | `rewardItem.amount` and `rewardItem.type` in `OnUserEarnedRewardListener` |
| Grant the reward | Your `addPoints` lambda → update state / call API |
| Handle dismiss | `onAdDismissedFullScreenContent()` → show "no reward" |
| Handle errors | `onAdFailedToLoad()` / `onAdFailedToShowFullScreenContent()` → show retry |
| Change UI on reward | Use `remember { mutableStateOf(Color.Red) }` (NOT `rememberSaveable`) |

---

*Generated for project: AdsMobTest — AdMob Rewarded Ads Implementation Guide*

