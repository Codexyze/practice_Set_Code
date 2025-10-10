package com.nutrino.animination_in_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.nutrino.animination_in_compose.ui.theme.Animination_In_ComposeTheme
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nutrino.animination_in_compose.pratice.AniminatevisiblityDemo
import com.nutrino.animination_in_compose.pratice.FaqExpandableCard
import com.nutrino.animination_in_compose.pratice.FaqScreen
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Animination_In_ComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                   Box(modifier = Modifier.padding(innerPadding)){
                       AnimationShowcase()
                       //AniminatevisiblityDemo()
                       //FaqScreen()
                   }
                }
            }
        }
    }
}



@Composable
fun AnimationShowcase() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            Text(
                "Compose Animation Examples",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // 1. Animate*AsState - Simple value animation
        item { AnimateAsStateExample() }

        // 2. AnimatedVisibility - Show/Hide animations
        item { AnimatedVisibilityExample() }

        // 3. Crossfade - Smooth content transitions
        item { CrossfadeExample() }

        // 4. updateTransition - Multiple property animations
        item { UpdateTransitionExample() }

        // 5. rememberInfiniteTransition - Continuous animations
        item { InfiniteTransitionExample() }

        // 6. Animatable - Low-level animation control
        item { AnimatableExample() }

        // 7. AnimatedContent - Content change animations
        item { AnimatedContentExample() }

        // 8. Spring Animation
        item { SpringAnimationExample() }

        // 9. Keyframe Animation
        item { KeyframeAnimationExample() }

        // 10. Gesture-based Animation
        item { GestureAnimationExample() }

        // 11. Simple Alpha Animation
        item { SimpleAlphaAnimation() }

        // 12 . TextRevealAnimation
        item { TextRevealAnimation() }

        // 13 . TextRevealAnimationContinous
        item { TextRevealAnimationContinous() }
    }
}

// 1. Animate*AsState - Animates a single value based on state change
@Composable
fun AnimateAsStateExample() {
    var isExpanded by remember { mutableStateOf(false) }

    // Animates the size value when isExpanded changes
    val size by animateDpAsState(
        targetValue = if (isExpanded) 200.dp else 100.dp,
        animationSpec = tween(durationMillis = 300), // Duration of 300ms
        label = "size"
    )

    // Animates the color value
    val color by animateColorAsState(
        targetValue = if (isExpanded) Color.Blue else Color.Red,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "color"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("1. Animate*AsState", fontWeight = FontWeight.Bold)
            Text("Tap the box to animate size and color", fontSize = 12.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .size(size)
                    .background(color, RoundedCornerShape(8.dp))
                    .clickable { isExpanded = !isExpanded },
                contentAlignment = Alignment.Center
            ) {
                Text("Tap me!", color = Color.White)
            }
        }
    }
}

// 2. AnimatedVisibility - Animates the appearance and disappearance of content
@Composable
fun AnimatedVisibilityExample() {
    var visible by remember { mutableStateOf(true) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("2. AnimatedVisibility", fontWeight = FontWeight.Bold)
            Text("Slide + Fade animation", fontSize = 12.sp, color = Color.Gray)

            Button(
                onClick = { visible = !visible },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text(if (visible) "Hide" else "Show")
            }

            // Combines slide and fade animations
            AnimatedVisibility(
                visible = visible,
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .background(Color.Green, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Animated Content", color = Color.White)
                }
            }
        }
    }
}

// 3. Crossfade - Smooth transition between different content
@Composable
fun CrossfadeExample() {
    var currentScreen by remember { mutableStateOf("A") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("3. Crossfade", fontWeight = FontWeight.Bold)
            Text("Smooth content transition", fontSize = 12.sp, color = Color.Gray)

            Row(
                modifier = Modifier.padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = { currentScreen = "A" }) { Text("Screen A") }
                Button(onClick = { currentScreen = "B" }) { Text("Screen B") }
                Button(onClick = { currentScreen = "C" }) { Text("Screen C") }
            }

            // Crossfade between different screens
            Crossfade(
                targetState = currentScreen,
                animationSpec = tween(durationMillis = 500),
                label = "screen"
            ) { screen ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(
                            when (screen) {
                                "A" -> Color.Cyan
                                "B" -> Color.Magenta
                                else -> Color.Yellow
                            },
                            RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Screen $screen",
                        color = if (screen == "C") Color.Black else Color.White,
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}

// 4. updateTransition - Animates multiple properties together
@Composable
fun UpdateTransitionExample() {
    var expanded by remember { mutableStateOf(false) }

    // Create a transition that tracks the expanded state
    val transition = updateTransition(targetState = expanded, label = "expand")

    // Animate multiple properties based on the transition
    val borderWidth by transition.animateDp(
        label = "borderWidth",
        transitionSpec = { spring(stiffness = Spring.StiffnessLow) }
    ) { isExpanded ->
        if (isExpanded) 4.dp else 1.dp
    }

    val rotation by transition.animateFloat(
        label = "rotation",
        transitionSpec = { tween(durationMillis = 400) }
    ) { isExpanded ->
        if (isExpanded) 360f else 0f
    }

    val cornerRadius by transition.animateDp(
        label = "corner",
        transitionSpec = { tween(durationMillis = 400) }
    ) { isExpanded ->
        if (isExpanded) 32.dp else 8.dp
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("4. updateTransition", fontWeight = FontWeight.Bold)
            Text("Multiple coordinated animations", fontSize = 12.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .size(150.dp)
                    .rotate(rotation)
                    .clip(RoundedCornerShape(cornerRadius))
                    .background(Color.DarkGray)
                    .clickable { expanded = !expanded }
                    .padding(borderWidth)
                    .background(Color.White, RoundedCornerShape(cornerRadius - borderWidth)),
                contentAlignment = Alignment.Center
            ) {
                Text("Tap to animate", color = Color.Black)
            }
        }
    }
}

// 5. rememberInfiniteTransition - Continuous repeating animations
@Composable
fun InfiniteTransitionExample() {
    // Creates an infinite animation
    val infiniteTransition = rememberInfiniteTransition(label = "infinite")

    // Pulsating scale animation
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    // Rotating animation
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("5. Infinite Transition", fontWeight = FontWeight.Bold)
            Text("Continuous animations", fontSize = 12.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Pulsating circle
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .scale(scale)
                        .background(Color.Red, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Pulse", color = Color.White, fontSize = 12.sp)
                }

                // Rotating square
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .rotate(rotation)
                        .background(Color.Blue, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Spin", color = Color.White, fontSize = 12.sp)
                }
            }
        }
    }
}

// 6. Animatable - Low-level animation API for custom control
@Composable
fun AnimatableExample() {
    // Create an Animatable for custom animation control
    val offsetX = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("6. Animatable", fontWeight = FontWeight.Bold)
            Text("Low-level animation control", fontSize = 12.sp, color = Color.Gray)

            Row(
                modifier = Modifier.padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = {
                    coroutineScope.launch {
                        // Animate to specific value with spring
                        offsetX.animateTo(
                            targetValue = 100f,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioHighBouncy,
                                stiffness = Spring.StiffnessVeryLow
                            )
                        )
                    }
                }) {
                    Text("Spring →")
                }

                Button(onClick = {
                    coroutineScope.launch {
                        // Snap immediately to value
                        offsetX.snapTo(0f)
                    }
                }) {
                    Text("Reset")
                }

                Button(onClick = {
                    coroutineScope.launch {
                        // Decay animation (fling-like)
                        offsetX.animateDecay(
                            initialVelocity = 1000f,
                            animationSpec = exponentialDecay()
                        )
                    }
                }) {
                    Text("Fling →")
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(Color.LightGray, RoundedCornerShape(4.dp))
            ) {
                Box(
                    modifier = Modifier
                        .offset(x = offsetX.value.dp)
                        .size(60.dp)
                        .background(Color.Blue, RoundedCornerShape(4.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Box", color = Color.White)
                }
            }
        }
    }
}

// 7. AnimatedContent - Animates content changes with custom transitions
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedContentExample() {
    var count by remember { mutableStateOf(0) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("7. AnimatedContent", fontWeight = FontWeight.Bold)
            Text("Content change animations", fontSize = 12.sp, color = Color.Gray)

            Row(
                modifier = Modifier.padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = { count-- }) { Text("-") }
                Button(onClick = { count++ }) { Text("+") }
            }

            // Animates content changes with slide animation
            AnimatedContent(
                targetState = count,
                transitionSpec = {
                    // Define how content enters and exits
                    if (targetState > initialState) {
                        slideInVertically { height -> height } + fadeIn() with
                                slideOutVertically { height -> -height } + fadeOut()
                    } else {
                        slideInVertically { height -> -height } + fadeIn() with
                                slideOutVertically { height -> height } + fadeOut()
                    }.using(
                        SizeTransform(clip = false)
                    )
                },
                label = "counter"
            ) { targetCount ->
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(
                            Color(0xFF, 0x88, targetCount * 10 % 256),
                            RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$targetCount",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// 8. Spring Animation - Physics-based animation
@Composable
fun SpringAnimationExample() {
    var stiffness by remember { mutableStateOf(Spring.StiffnessMedium) }
    var dampingRatio by remember { mutableStateOf(Spring.DampingRatioNoBouncy) }
    var targetPosition by remember { mutableStateOf(false) }

    val offset by animateDpAsState(
        targetValue = if (targetPosition) 150.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = dampingRatio,
            stiffness = stiffness
        ),
        label = "spring"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("8. Spring Animation", fontWeight = FontWeight.Bold)
            Text("Physics-based animation", fontSize = 12.sp, color = Color.Gray)

            // Stiffness controls
            Text("Stiffness", fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                FilterChip(
                    onClick = { stiffness = Spring.StiffnessVeryLow },
                    selected = stiffness == Spring.StiffnessVeryLow,
                    label = { Text("Very Low", fontSize = 10.sp) }
                )
                FilterChip(
                    onClick = { stiffness = Spring.StiffnessLow },
                    selected = stiffness == Spring.StiffnessLow,
                    label = { Text("Low", fontSize = 10.sp) }
                )
                FilterChip(
                    onClick = { stiffness = Spring.StiffnessMedium },
                    selected = stiffness == Spring.StiffnessMedium,
                    label = { Text("Medium", fontSize = 10.sp) }
                )
                FilterChip(
                    onClick = { stiffness = Spring.StiffnessHigh },
                    selected = stiffness == Spring.StiffnessHigh,
                    label = { Text("High", fontSize = 10.sp) }
                )
            }

            // Damping ratio controls
            Text("Damping Ratio", fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                FilterChip(
                    onClick = { dampingRatio = Spring.DampingRatioHighBouncy },
                    selected = dampingRatio == Spring.DampingRatioHighBouncy,
                    label = { Text("High Bouncy", fontSize = 10.sp) }
                )
                FilterChip(
                    onClick = { dampingRatio = Spring.DampingRatioMediumBouncy },
                    selected = dampingRatio == Spring.DampingRatioMediumBouncy,
                    label = { Text("Medium Bouncy", fontSize = 10.sp) }
                )
                FilterChip(
                    onClick = { dampingRatio = Spring.DampingRatioLowBouncy },
                    selected = dampingRatio == Spring.DampingRatioLowBouncy,
                    label = { Text("Low Bouncy", fontSize = 10.sp) }
                )
                FilterChip(
                    onClick = { dampingRatio = Spring.DampingRatioNoBouncy },
                    selected = dampingRatio == Spring.DampingRatioNoBouncy,
                    label = { Text("No Bouncy", fontSize = 10.sp) }
                )
            }

            Button(
                onClick = { targetPosition = !targetPosition },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text("Animate")
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(Color.LightGray, RoundedCornerShape(4.dp))
            ) {
                Box(
                    modifier = Modifier
                        .offset(x = offset)
                        .size(60.dp)
                        .background(Color(0xFF4CAF50), RoundedCornerShape(4.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Spring", color = Color.White, fontSize = 10.sp)
                }
            }
        }
    }
}

// 9. Keyframe Animation - Define specific values at different time points
@Composable
fun KeyframeAnimationExample() {
    var animate by remember { mutableStateOf(false) }

    val offset by animateDpAsState(
        targetValue = if (animate) 200.dp else 0.dp,
        animationSpec = keyframes {
            // Total duration
            durationMillis = 2000
            // Define specific values at different time points
            0.dp at 0 with LinearEasing // Start
            50.dp at 500 with FastOutSlowInEasing // 25% progress
            150.dp at 1000 with LinearOutSlowInEasing // 50% progress
            100.dp at 1500 // 75% progress - bounce back effect
        },
        label = "keyframe"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("9. Keyframe Animation", fontWeight = FontWeight.Bold)
            Text("Custom animation curve with keyframes", fontSize = 12.sp, color = Color.Gray)

            Button(
                onClick = { animate = !animate },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text(if (animate) "Reset" else "Animate with Keyframes")
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(Color.LightGray, RoundedCornerShape(4.dp))
            ) {
                Box(
                    modifier = Modifier
                        .offset(x = offset)
                        .size(60.dp)
                        .background(Color(0xFFFF9800), RoundedCornerShape(4.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("KF", color = Color.White)
                }
            }

            Text(
                "Timeline: 0ms → 500ms → 1000ms → 1500ms → 2000ms",
                fontSize = 10.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

// 10. Gesture-based Animation - Animations triggered by user gestures
@Composable
fun GestureAnimationExample() {
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    // Animated values that follow the gesture
    val animatedX by animateFloatAsState(
        targetValue = offsetX,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "x"
    )

    val animatedY by animateFloatAsState(
        targetValue = offsetY,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "y"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("10. Gesture-based Animation", fontWeight = FontWeight.Bold)
            Text("Tap anywhere to animate the circle", fontSize = 12.sp, color = Color.Gray)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->
                            // Update target position based on tap
                            offsetX = offset.x - size.width / 2
                            offsetY = offset.y - size.height / 2
                        }
                    }
            ) {
                // Animated circle that follows taps
                Box(
                    modifier = Modifier
                        .offset(x = animatedX.dp, y = animatedY.dp)
                        .size(40.dp)
                        .background(Color(0xFF9C27B0), CircleShape)
                        .align(Alignment.Center)
                )

                Text(
                    "Tap to move",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun SimpleAlphaAnimation() {
    var visible by remember { mutableStateOf(false) }
    // Animate alpha from 0f to 1f based on visible state
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 500), // 500ms duration
        label = "alpha"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().padding(32.dp)
    ) {
        Button(onClick = { visible = !visible }) {
            Text(if (visible) "Hide Box" else "Show Box")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .size(100.dp)
                .graphicsLayer { this.alpha = alpha }
                .background(Color.Blue, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("Hello", color = Color.White)
        }
    }
}


@Composable
fun TextRevealAnimation() {
    val fullText = "Hello"
    var displayedText by remember { mutableStateOf("") }
    var animationComplete by remember { mutableStateOf(false) }

    val alpha by animateFloatAsState(
        targetValue = if (displayedText.isNotEmpty()) 1f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "textAlpha"
    )

    LaunchedEffect(Unit) {
        for (i in fullText.indices) {
            displayedText = fullText.substring(0, i + 1)
            kotlinx.coroutines.delay(300) // 300ms delay between letters
        }
        animationComplete = true
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Text Reveal Animation", fontWeight = FontWeight.Bold)
            Text("Letters appear one by one", fontSize = 12.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color(0xFF2196F3), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = displayedText,
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.graphicsLayer { this.alpha = alpha }
                )
            }

            if (animationComplete) {
                Text(
                    "Animation Complete!",
                    fontSize = 12.sp,
                    color = Color.Green,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }


}

@Composable
fun TextRevealAnimationContinous() {
    val fullText = "Hello"
    var displayedText by remember { mutableStateOf("") }
    var currentIndex by remember { mutableStateOf(0) }

    val alpha by animateFloatAsState(
        targetValue = if (displayedText.isNotEmpty()) 1f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "textAlpha"
    )

    LaunchedEffect(Unit) {
        while (true) {
            // Reveal letters one by one
            for (i in fullText.indices) {
                displayedText = fullText.substring(0, i + 1)
                currentIndex = i
                kotlinx.coroutines.delay(300)
            }
            // Show complete text for 1 second
            kotlinx.coroutines.delay(1000)
            // Reset and start over
            displayedText = ""
            kotlinx.coroutines.delay(500)
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Text Reveal Animation", fontWeight = FontWeight.Bold)
            Text("Continuous letter-by-letter reveal", fontSize = 12.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color(0xFF2196F3), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = displayedText,
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.graphicsLayer { this.alpha = alpha }
                )
            }

            Text(
                "Animation loops continuously",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}




