package com.example.speechtotext

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.util.Locale

// -------------------- STATE --------------------

sealed class SpeechState {
    object Idle : SpeechState()
    object Listening : SpeechState()
    data class Result(val text: String) : SpeechState()
    data class Error(val message: String) : SpeechState()
}

// -------------------- VIEWMODEL --------------------

class SpeechViewModel(private val context: Context) : ViewModel() {

    var state by mutableStateOf<SpeechState>(SpeechState.Idle)
        private set

    private val recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
    }

    private val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {

        setRecognitionListener(object : RecognitionListener {

            override fun onResults(results: Bundle?) {
                val text = results
                    ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    ?.firstOrNull()
                    ?: ""

                state = if (text.isNotBlank()) {
                    SpeechState.Result(text)
                } else {
                    SpeechState.Error("No speech detected")
                }
            }

            override fun onError(error: Int) {
                state = SpeechState.Error("Error, try again")
            }

            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
            override fun onPartialResults(partialResults: Bundle?) {}
        })
    }

    fun toggleListening() {
        when (state) {
            is SpeechState.Listening -> {
                speechRecognizer.stopListening()
                state = SpeechState.Idle
            }

            else -> {
                state = SpeechState.Listening
                speechRecognizer.startListening(recognizerIntent)
            }
        }
    }

    fun onPermissionDenied() {
        state = SpeechState.Error("Mic permission required")
    }

    override fun onCleared() {
        speechRecognizer.destroy()
        super.onCleared()
    }
}

// -------------------- FACTORY --------------------

class SpeechViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SpeechViewModel(context) as T
    }
}

// -------------------- ACTIVITY --------------------

class MainActivity : ComponentActivity() {

    private val viewModel: SpeechViewModel by viewModels {
        SpeechViewModelFactory(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SpeechScreen(viewModel)
        }
    }
}

// -------------------- UI --------------------

@Composable
fun SpeechScreen(viewModel: SpeechViewModel) {

    val context = LocalContext.current

    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
        if (granted) {
            viewModel.toggleListening()
        } else {
            viewModel.onPermissionDenied()
        }
    }

    val state = viewModel.state

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Button(onClick = {
            if (!hasPermission) {
                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            } else {
                viewModel.toggleListening()
            }
        }) {
            Text(if (state is SpeechState.Listening) "Stop" else "Start")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = when (state) {
                SpeechState.Idle -> "Tap and speak"
                SpeechState.Listening -> "Listening..."
                is SpeechState.Result -> state.text
                is SpeechState.Error -> state.message
            }
        )
    }
}