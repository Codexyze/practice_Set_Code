package com.nutrino.mlmodelinandroid.presenation

import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.nutrino.mlmodelinandroid.ml.Audionoisemodel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

private const val TAG = "AudioNoiseScreen"

@Composable
fun AudioNoiseScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    var status by remember { mutableStateOf("Select an audio file") }
    var isProcessing by remember { mutableStateOf(false) }

    val startProcessing: (Uri) -> Unit = { uri ->
        scope.launch {
            isProcessing = true
            status = "Processing..."
            withContext(Dispatchers.IO) {
                runAudioModel(context, uri) { resultStatus ->
                    scope.launch {
                        status = resultStatus
                        isProcessing = false
                    }
                }
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            selectedUri?.let { startProcessing(it) }
        } else {
            status = "Storage permission is required to save the file."
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedUri = uri
        if (uri != null) {
            status = "File selected"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Audio Noise Reduction", style = MaterialTheme.typography.headlineMedium)

        Button(
            onClick = { launcher.launch("audio/*") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Pick Audio File")
        }

        selectedUri?.let { uri ->
            Button(
                onClick = {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q &&
                        ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) 
                        != PackageManager.PERMISSION_GRANTED) {
                        permissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    } else {
                        startProcessing(uri)
                    }
                },
                enabled = !isProcessing,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isProcessing) "Processing..." else "Reduce Noise")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = status, style = MaterialTheme.typography.bodyMedium)
    }
}

private fun runAudioModel(context: Context, uri: Uri, onResult: (String) -> Unit) {
    try {
        val model = Audionoisemodel.newInstance(context)

        // Load audio bytes
        val inputStream = context.contentResolver.openInputStream(uri)
        val audioBytes = inputStream?.readBytes() ?: throw Exception("Could not read audio file")
        inputStream.close()

        // Prepare input buffer [1, 128, 512, 1] FLOAT32
        // Size = 1 * 128 * 512 * 1 * 4 bytes per float
        val numSamples = 1 * 128 * 512 * 1
        val byteBuffer = ByteBuffer.allocateDirect(numSamples * 4)
        byteBuffer.order(ByteOrder.nativeOrder())

        // Logic: Convert incoming bytes (usually PCM16) to FLOAT32 if they aren't already
        // For simplicity, we'll assume the input is PCM16 and convert it.
        val shortBuffer = ByteBuffer.wrap(audioBytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer()
        var samplesProcessed = 0
        while (shortBuffer.hasRemaining() && samplesProcessed < numSamples) {
            val sample = shortBuffer.get().toFloat() / 32768.0f
            byteBuffer.putFloat(sample)
            samplesProcessed++
        }
        
        // Pad with zeros if input was too short
        while (samplesProcessed < numSamples) {
            byteBuffer.putFloat(0.0f)
            samplesProcessed++
        }
        byteBuffer.rewind()

        // Runs model inference and gets result.
        val outputs = model.process(byteBuffer)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        // Convert FLOAT32 output back to PCM16 for saving as a proper WAV
        val outputFloats = outputFeature0.asFloatBuffer()
        val pcmBytes = ByteArray(numSamples * 2)
        val pcmBuffer = ByteBuffer.wrap(pcmBytes).order(ByteOrder.LITTLE_ENDIAN)
        
        for (i in 0 until numSamples) {
            if (outputFloats.hasRemaining()) {
                val f = outputFloats.get()
                val s = (f * 32767).toInt().coerceIn(-32768, 32767).toShort()
                pcmBuffer.putShort(s)
            } else {
                pcmBuffer.putShort(0)
            }
        }

        // Save output to public Downloads folder
        val fileName = "noise_reduced_${System.currentTimeMillis()}.wav"
        val resolver = context.contentResolver
        
        val outputUri: Uri?
        var finalPath = ""

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, "audio/wav")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                    put(MediaStore.MediaColumns.IS_PENDING, 1)
                }
                
                // Use Downloads collection on API 29+
                val collection = MediaStore.Downloads.EXTERNAL_CONTENT_URI
                outputUri = resolver.insert(collection, contentValues)
                
                outputUri?.let { uri ->
                    resolver.openOutputStream(uri).use { os ->
                        if (os == null) throw Exception("Could not open output stream")
                        writeWavHeader(os, 1, 44100, 16, pcmBytes.size)
                        os.write(pcmBytes)
                    }
                    contentValues.clear()
                    contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                    resolver.update(uri, contentValues, null, null)
                    finalPath = "Downloads/$fileName"
                }
            } else {
                // Fallback for older Android versions
                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                if (!downloadsDir.exists()) downloadsDir.mkdirs()
                
                val file = File(downloadsDir, fileName)
                FileOutputStream(file).use { fos ->
                    writeWavHeader(fos, 1, 44100, 16, pcmBytes.size)
                    fos.write(pcmBytes)
                }
                outputUri = Uri.fromFile(file)
                finalPath = file.absolutePath
                
                // Refresh gallery/file system
                val intent = android.content.Intent(android.content.Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                intent.data = outputUri
                context.sendBroadcast(intent)
            }

            if (outputUri == null) throw Exception("Failed to create file")

            // Releases model resources if no longer used.
            model.close()
            onResult("Success! Saved to $finalPath")
            Log.d(TAG, "Output saved to: $finalPath")

        } catch (e: Exception) {
            model.close()
            throw e
        }

    } catch (e: Exception) {
        Log.e(TAG, "Error processing audio: ${e.message}", e)
        onResult("Error: ${e.message}")
    }
}

private fun writeWavHeader(out: OutputStream, channels: Int, sampleRate: Int, bitDepth: Int, dataSize: Int) {
    val totalSize = 36 + dataSize
    val byteRate = sampleRate * channels * bitDepth / 8
    
    val header = ByteBuffer.allocate(44).order(ByteOrder.LITTLE_ENDIAN)
    header.put("RIFF".toByteArray())
    header.putInt(totalSize)
    header.put("WAVE".toByteArray())
    header.put("fmt ".toByteArray())
    header.putInt(16) // Subchunk1Size
    header.putShort(1.toShort()) // AudioFormat (PCM = 1)
    header.putShort(channels.toShort())
    header.putInt(sampleRate)
    header.putInt(byteRate)
    header.putShort((channels * bitDepth / 8).toShort()) // BlockAlign
    header.putShort(bitDepth.toShort())
    header.put("data".toByteArray())
    header.putInt(dataSize)
    
    out.write(header.array())
}
