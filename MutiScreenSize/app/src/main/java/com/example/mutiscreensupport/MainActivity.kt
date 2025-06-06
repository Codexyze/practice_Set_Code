package com.example.mutiscreensupport

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mutiscreensupport.ui.theme.MutiScreenSupportTheme

// The main activity that launches the Compose UI
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Compose entry point - sets the whole UI content
        setContent {

            // Applying our custom app theme
            MutiScreenSupportTheme {

                // Call our custom function to get screen size info
                val windowInfo = rememberWindowInfo()

                // Check if the current screen is COMPACT (phone or small screen)
                if (windowInfo.screenWidthInfo is WindowInfo.WindowType.Compact) {

                    // Show both lists stacked vertically using LazyColumn (scrollable column)
                    LazyColumn(
                        modifier = Modifier.fillMaxSize() // Take full screen space
                    ) {

                        // First list: 10 items, each item with cyan background
                        items(10) {
                            Text(
                                text = "Item $it",          // Displaying item index
                                fontSize = 25.sp,          // Set text size
                                modifier = Modifier
                                    .fillMaxWidth()        // Make text take full width
                                    .background(Color.Cyan) // Background color
                                    .padding(16.dp)        // Add padding around text
                            )
                        }

                        // Second list: another 10 items, but green background
                        items(10) {
                            Text(
                                text = "Item $it",
                                fontSize = 25.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.Green)
                                    .padding(16.dp)
                            )
                        }
                    }

                } else {
                    // If the screen is MEDIUM or EXPANDED (tablet or big screen)

                    // Use a Row to place two LazyColumns side by side
                    Row(
                        modifier = Modifier.fillMaxWidth() // Row takes full width
                    ) {

                        // Left list (1st column)
                        LazyColumn(
                            modifier = Modifier.weight(1f) // Take 50% of Row width
                        ) {
                            items(10) {
                                Text(
                                    text = "Item $it",
                                    fontSize = 25.sp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.Green)
                                        .padding(16.dp)
                                )
                            }
                        }

                        // Right list (2nd column)
                        LazyColumn(
                            modifier = Modifier.weight(1f) // Also take 50% of Row width
                        ) {
                            items(10) {
                                Text(
                                    text = "Item $it",
                                    fontSize = 25.sp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.Cyan)
                                        .padding(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun rememberWindowInfo(): WindowInfo {
    // Grab the current screen configuration using Compose's built-in way
    val configuration = LocalConfiguration.current

    // Return a data object that contains categorized size info
    return WindowInfo(
        // Categorize screen WIDTH based on common breakpoints (Google-style)
        screenWidthInfo = when {
            configuration.screenWidthDp < 600 -> WindowInfo.WindowType.Compact     // Phones
            configuration.screenWidthDp < 840 -> WindowInfo.WindowType.Medium      // Small tablets
            else -> WindowInfo.WindowType.Expanded                                 // Tablets / desktop
        },

        // Categorize screen HEIGHT similarly
        screenHeightInfo = when {
            configuration.screenHeightDp < 480 -> WindowInfo.WindowType.Compact    // Short screens
            configuration.screenHeightDp < 900 -> WindowInfo.WindowType.Medium     // Medium height
            else -> WindowInfo.WindowType.Expanded                                 // Tall screens
        },

        // Also store exact width and height in dp
        screenWidth = configuration.screenWidthDp.dp,
        screenHeight = configuration.screenHeightDp.dp
    )
}
// Holds screen size category (Compact, Medium, Expanded) and actual dimensions
data class WindowInfo(
    val screenWidthInfo: WindowType,    // Enum-like value for screen width size
    val screenHeightInfo: WindowType,   // Enum-like value for screen height size
    val screenWidth: Dp,                // Actual width in Dp units
    val screenHeight: Dp                // Actual height in Dp units
) {
    // Represents 3 distinct size types using a sealed class for safety
    sealed class WindowType {
        object Compact : WindowType()    // Small screen (e.g. phones)
        object Medium : WindowType()     // Mid-sized screen (e.g. small tablets)
        object Expanded : WindowType()   // Large screens (e.g. tablets, desktops)
    }
}
