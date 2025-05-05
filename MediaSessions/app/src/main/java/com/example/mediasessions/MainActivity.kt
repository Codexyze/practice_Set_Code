package com.example.mediasessions

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.session.MediaSessionManager
import android.os.Build
import androidx.media3.session.MediaSession
import androidx.media3.ui.PlayerNotificationManager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.Keep
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.WindowCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSessionService
import androidx.media3.ui.PlayerView
import com.example.mediasessions.ui.theme.MediaSessionsTheme
//
//class MainActivity : ComponentActivity() {
//    private lateinit var exoPlayer: ExoPlayer
//    private lateinit var mediaSession: MediaSession
//    private lateinit var mediaSessionConnector: MediaSessionManager
//    override fun onCreate(savedInstanceState: Bundle?) {
//        exoPlayer = ExoPlayer.Builder(this).build()
//
//        // Prepare Media Item (local asset or online mp3)
//        val mediaItem = MediaItem.fromUri("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3")
//        exoPlayer.setMediaItem(mediaItem)
//        exoPlayer.prepare()
//
//        // 2. Setup MediaSession
//        mediaSession = MediaSession.Builder(this, exoPlayer).build()
//
//        // 3. Play music automatically
//        exoPlayer.playWhenReady = true
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            MediaSessionsTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//
//                }
//            }
//        }
//    }
//}
//
//class MainActivity : ComponentActivity() {
//
//    private lateinit var exoPlayer: ExoPlayer
//    private lateinit var mediaSession: MediaSession
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        // 1. Create ExoPlayer
//        exoPlayer = ExoPlayer.Builder(this).build()
//
//        // 2. Create MediaItem
//        val mediaItem = MediaItem.fromUri("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3")
//        exoPlayer.setMediaItem(mediaItem)
//        exoPlayer.prepare()
//
//        // 3. Create MediaSession
//        mediaSession = MediaSession.Builder(this, exoPlayer).build()
//
//        // 4. Play
//        exoPlayer.playWhenReady = true
//
//        // 5. Compose UI
//        setContent {
//            Surface(
//                modifier = Modifier.fillMaxSize(),
//                color = MaterialTheme.colorScheme.background
//            ) {
//                Column(
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Center
//                ) {
//                    Button(onClick = {
//                        if (exoPlayer.isPlaying) {
//                            exoPlayer.pause()
//                        } else {
//                            exoPlayer.play()
//                        }
//                    }) {
//                        Text(if (exoPlayer.isPlaying) "Pause" else "Play")
//                    }
//                }
//            }
//        }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        mediaSession.release()
//        exoPlayer.release()
//    }
//}

//class MainActivity : ComponentActivity() {
//
//    private lateinit var exoPlayer: ExoPlayer
//    private lateinit var mediaSession: MediaSession
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        // Step 1: Create ExoPlayer
//        exoPlayer = ExoPlayer.Builder(this).build()
//
//        // Step 2: Create MediaItem (your audio URL)
//        val mediaItem = MediaItem.fromUri("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3")
//        exoPlayer.setMediaItem(mediaItem)
//        exoPlayer.prepare()
//
//        // Step 3: Create MediaSession (IMPORTANT for Android to know this app is playing media)
//        mediaSession = MediaSession.Builder(this, exoPlayer).build()
//
//        // Step 4: Create Notification Channel (required for Android 8+)
//        createNotificationChannel()
//
//        // Step 5: Show initial Notification
//        showMediaNotification()
//
//        // Step 6: Start playing
//        exoPlayer.playWhenReady = true
//
//        // Step 7: Compose UI
//        setContent {
//            MediaSessionsTheme {
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    var isPlaying by remember { mutableStateOf(exoPlayer.isPlaying) }
//
//                    Column(
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        verticalArrangement = Arrangement.Center
//                    ) {
//                        Button(
//                            onClick = {
//                                if (exoPlayer.isPlaying) {
//                                    exoPlayer.pause()
//                                } else {
//                                    exoPlayer.play()
//                                }
//                                isPlaying = exoPlayer.isPlaying
//                                showMediaNotification() // Update notification after play/pause
//                            },
//                            modifier = Modifier.padding(16.dp)
//                        ) {
//                            Text(text = if (isPlaying) "Pause" else "Play")
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        mediaSession.release()
//        exoPlayer.release()
//    }
//
//    // Step 4 Function: Create Notification Channel (Android 8+)
//    private fun createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                "media_playback_channel",
//                "Media Playback",
//                NotificationManager.IMPORTANCE_HIGH
//            ).apply {
//                description = "Channel for media playback controls"
//            }
//            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            manager.createNotificationChannel(channel)
//        }
//    }
//
//    // Step 5 Function: Create and Show Media Notification
//    private fun showMediaNotification() {
//        val notification = NotificationCompat.Builder(this, "media_playback_channel")
//            .setContentTitle("Playing Awesome Song 🎵")
//            .setContentText("SoundHelix Song 1")
//            .setSmallIcon(android.R.drawable.ic_media_play) // Default play icon
//            .setStyle(
//                NotificationCompat.MediaStyle()
//                    .setMediaSession(mediaSession.sessionCompatToken)
//                    .setShowActionsInCompactView(0)
//            )
//            .addAction(
//                if (exoPlayer.isPlaying) {
//                    NotificationCompat.Action(
//                        android.R.drawable.ic_media_pause,
//                        "Pause",
//                        null // You can later add PendingIntent for actions
//                    )
//                } else {
//                    NotificationCompat.Action(
//                        android.R.drawable.ic_media_play,
//                        "Play",
//                        null
//                    )
//                }
//            )
//            .setOnlyAlertOnce(true)
//            .setOngoing(exoPlayer.isPlaying) // Notification will stick when playing
//            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
//            .build()
//
//        with(NotificationManagerCompat.from(this)) {
//            notify(1, notification) // Notification ID = 1
//        }
//    }
//}
//
//
//class MainActivity : ComponentActivity() {
//
//    private lateinit var exoPlayer: ExoPlayer
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        WindowCompat.setDecorFitsSystemWindows(window, false)
//
//        // Initialize ExoPlayer
//        exoPlayer = ExoPlayer.Builder(this).build().apply {
//            val mediaItem = MediaItem.fromUri("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3")
//            setMediaItem(mediaItem)
//            prepare()
//            playWhenReady = true
//        }
//
//        setContent {
//            MaterialTheme {
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    ExoPlayerScreen(exoPlayer)
//                }
//            }
//        }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        exoPlayer.release()
//    }
//}
//
//@OptIn(UnstableApi::class)
//@Composable
//fun ExoPlayerScreen(player: ExoPlayer) {
//    var isPlaying by remember { mutableStateOf(player.isPlaying) }
//
//    LaunchedEffect(player) {
//        val listener = object : Player.Listener {
//            override fun onIsPlayingChanged(isPlayingNew: Boolean) {
//                isPlaying = isPlayingNew
//            }
//        }
//        player.addListener(listener)
//
//        // Clean up when Composable is destroyed
//
//    }
//
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center,
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        Button(
//            onClick = {
//                if (player.isPlaying) {
//                    player.pause()
//                } else {
//                    player.play()
//                }
//            },
//            modifier = Modifier.padding(bottom = 16.dp)
//        ) {
//            Text(text = if (isPlaying) "Pause ⏸️" else "Play ▶️")
//        }
//
//        AndroidView(
//            factory = { context ->
//                PlayerView(context).apply {
//                    this.player = player
//                    this.useController = true // Show Play/Pause inside PlayerView
//                    this.setShowNextButton(false)
//                    this.setShowPreviousButton(false)
//                }
//            },
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(250.dp)
//        )
//    }
//}

@UnstableApi
class MainActivity : ComponentActivity() {

    private lateinit var exoPlayer: ExoPlayer
    private var playerNotificationManager: PlayerNotificationManager? = null
    private lateinit var mediaSession: MediaSession

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Initialize ExoPlayer
        exoPlayer = ExoPlayer.Builder(this).build().apply {
            val mediaItem = MediaItem.fromUri("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3")
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true
        }

        // Initialize MediaSession
        mediaSession = MediaSession.Builder(this, exoPlayer).build()

        // Initialize Notification
        playerNotificationManager = PlayerNotificationManager.Builder(
            this,
            1, // Notification ID
            "media_channel" // Notification Channel ID
        )
            .setMediaDescriptionAdapter(DescriptionAdapter())
            .setSmallIconResourceId(R.drawable.ic_launcher_foreground) // your app icon
            .build()
            .apply {
                setPlayer(exoPlayer)
            }

        // Create notification channel if needed
        createNotificationChannel()

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ExoPlayerScreen(exoPlayer)
                }
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "media_channel",
                "Media Playback",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        playerNotificationManager?.setPlayer(null)
        mediaSession.release()
        exoPlayer.release()
    }

    inner class DescriptionAdapter : PlayerNotificationManager.MediaDescriptionAdapter {
        override fun getCurrentContentTitle(player: Player): CharSequence {
            return "Playing Music 🎵"
        }

        override fun createCurrentContentIntent(player: Player) = null

        override fun getCurrentContentText(player: Player): CharSequence? {
            return "Now playing from SoundHelix!"
        }

        override fun getCurrentLargeIcon(
            player: Player,
            callback: PlayerNotificationManager.BitmapCallback
        ) = null
    }
}

@OptIn(UnstableApi::class)
@Composable
fun ExoPlayerScreen(player: ExoPlayer) {
    var isPlaying by remember { mutableStateOf(player.isPlaying) }

    LaunchedEffect(player) {
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(isPlayingNew: Boolean) {
                isPlaying = isPlayingNew
            }
        }
        player.addListener(listener)


    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(
            onClick = {
                if (player.isPlaying) {
                    player.pause()
                } else {
                    player.play()
                }
            },
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text(text = if (isPlaying) "Pause ⏸️" else "Play ▶️")
        }

        AndroidView(
            factory = { context ->
                PlayerView(context).apply {
                    this.player = player
                    this.useController = true
                    this.setShowNextButton(false)
                    this.setShowPreviousButton(false)
                    this.setShowNextButton(true)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        )
    }
}


//
//private const val NOTIFICATION_ID = 101
//private const val NOTIFICATION_CHANNEL_NAME = "notification channel 1"
//private const val NOTIFICATION_CHANNEL_ID = "notification channel id 1"
//class NotificationManager @Inject constructor(
//    @ApplicationContext private val context: Context,
//    private val exoPlayer: ExoPlayer,
//) {
//
//    private val notificationManager: NotificationManagerCompat = NotificationManagerCompat.from(context)
//    init {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            createNotificationChannel()
//        }
//    }
//
//    @UnstableApi
//    private fun buildNotification(mediaSession: MediaSession) {
//        PlayerNotificationManager.Builder(
//            context,
//            NOTIFICATION_ID,
//            NOTIFICATION_CHANNEL_ID
//        )
//            .setMediaDescriptionAdapter(
//                JetAudioNotificationAdapter(
//                    context = context,
//                    pendingIntent = mediaSession.sessionActivity
//                )
//            )
//            .setSmallIconResourceId(R.drawable.lythmlogoasset)
//            .build()
//            .also {
//                it.setUseFastForwardActionInCompactView(true)
//                it.setUseRewindActionInCompactView(true)
//                it.setUseNextActionInCompactView(true)
//                it.setPriority(NotificationCompat.PRIORITY_LOW)
//                it.setPlayer(exoPlayer)
//            }
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    @UnstableApi
//    fun startNotificationService(
//        mediaSessionService: MediaSessionService,
//        mediaSession: MediaSession,
//    ) {
//        buildNotification(mediaSession)
//        startForeGroundNotificationService(mediaSessionService)
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun startForeGroundNotificationService(mediaSessionService: MediaSessionService) {
//        val notification = Notification.Builder(context,
//            NOTIFICATION_CHANNEL_ID
//        )
//            .setCategory(Notification.CATEGORY_SERVICE)
//            .build()
//        mediaSessionService.startForeground(NOTIFICATION_ID, notification)
//    }
//
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun createNotificationChannel() {
//        val channel = NotificationChannel(
//            NOTIFICATION_CHANNEL_ID,
//            NOTIFICATION_CHANNEL_NAME,
//            NotificationManager.IMPORTANCE_LOW
//        )
//        notificationManager.createNotificationChannel(channel)
//    }
//
//
//
//}
//
//
//
//@UnstableApi
//class JetAudioNotificationAdapter(
//    private val context: Context,
//    private val pendingIntent: PendingIntent?,
//) : PlayerNotificationManager.MediaDescriptionAdapter {
//    override fun getCurrentContentTitle(player: Player): CharSequence =
//        player.mediaMetadata.albumTitle ?: "Unknown"
//
//    override fun createCurrentContentIntent(player: Player): PendingIntent? = pendingIntent
//
//    override fun getCurrentContentText(player: Player): CharSequence =
//        player.mediaMetadata.displayTitle ?: "Unknown"
//
//    override fun getCurrentLargeIcon(
//        player: Player,
//        callback: PlayerNotificationManager.BitmapCallback,
//    ): Bitmap? {
//
//        return null
//    }
//}
//
//
//
//@AndroidEntryPoint
//class JetAudioService : MediaSessionService() {
//    @Inject
//    lateinit var mediaSession: MediaSession
//
//    @Inject
//    lateinit var notificationManager: com.example.lhythm.core.LocalNotification.NotificationManager
//
//    @UnstableApi
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            notificationManager.startNotificationService(
//                mediaSession = mediaSession,
//                mediaSessionService = this
//            )
//        }
//        return super.onStartCommand(intent, flags, startId)
//    }
//
//    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession =
//        mediaSession
//
//    override fun onDestroy() {
//        super.onDestroy()
//        mediaSession.apply {
//            release()
//            if (player.playbackState != Player.STATE_IDLE) {
//                player.seekTo(0)
//                player.playWhenReady = false
//                player.stop()
//            }
//        }
//    }
//}
