package com.nutrino.maps_in_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.mapsplatform.transportation.consumer.model.MarkerType
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.nutrino.maps_in_compose.ui.theme.Maps_In_ComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Maps_In_ComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                   MountainMap(
                       paddingValues = innerPadding
                   )
                }
            }
        }
    }
}


//

@Composable
fun MountainMap(
    paddingValues: PaddingValues,
) {
    // 🔹 State to track whether map is loaded
    var isMapLoaded by remember { mutableStateOf(false) }

    // 🔹 State for storing markers added by user taps
    var markers by remember { mutableStateOf(listOf<LatLng>()) }

    // 🔹 Camera position state (controls zoom, tilt, etc.)
    val cameraPositionState = rememberCameraPositionState {
        // Initial camera position (let’s start near the Himalayas for fun 🏔️)
        position = CameraPosition.fromLatLngZoom(LatLng(27.9881, 86.9250), 6f)
    }

    // 🔹 Map UI settings (compass, zoom controls, gestures)
    val mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                compassEnabled = true,          // Show compass
                zoomControlsEnabled = true,     // Show + / - buttons
                scrollGesturesEnabled = true,   // Allow scrolling
                zoomGesturesEnabled = true      // Allow pinch zoom
            )
        )
    }

    // 🔹 Map properties (map type: Normal, Satellite, Hybrid, Terrain)
    var mapProperties by remember {
        mutableStateOf(
            MapProperties(
                mapType = MapType.NORMAL,      // Default map style
                isTrafficEnabled = true,       // Show live traffic if available
                isBuildingEnabled = true       // Show 3D buildings
            )
        )
    }

    // UI wrapper
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        // 🔹 Toggle buttons for switching map type
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { mapProperties = mapProperties.copy(mapType = MapType.NORMAL) }) {
                Text("Normal")
            }
            Button(onClick = { mapProperties = mapProperties.copy(mapType = MapType.SATELLITE) }) {
                Text("Satellite")
            }
            Button(onClick = { mapProperties = mapProperties.copy(mapType = MapType.HYBRID) }) {
                Text("Hybrid")
            }
        }

        // 🔹 Main Map
        Box(modifier = Modifier.fillMaxSize()) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = mapProperties,
                uiSettings = mapUiSettings,
                onMapLoaded = { isMapLoaded = true },

                // 👇 Add marker on short tap
                onMapClick = { latLng ->
                    markers = markers + latLng
                },

                // 👇 Add marker on long tap
                onMapLongClick = { latLng ->
                    markers = markers + latLng
                }
            ) {
                // 🔹 Render all user-added markers
                markers.forEachIndexed { index, latLng ->
                    Marker(
                        state = MarkerState(position = latLng),
                        title = "Marker #${index + 1}",
                        snippet = "Lat: ${latLng.latitude}, Lng: ${latLng.longitude}"
                    )
                }
            }

            // 🔹 Simple overlay while loading
            if (!isMapLoaded) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
