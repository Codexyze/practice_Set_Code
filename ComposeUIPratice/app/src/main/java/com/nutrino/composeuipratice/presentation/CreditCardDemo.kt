package com.nutrino.composeuipratice.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nutrino.composeuipratice.ui.theme.ComposeUIPraticeTheme


@Composable
fun CreditCardDemo(modifier: Modifier = Modifier) {

    val paddingLarge = 20.dp
    val paddingMedium = 22.dp
    val cornerRadius = 26.dp
    val cardElevation = 14.dp

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingLarge),
        contentAlignment = Alignment.Center
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.6f),
            shape = RoundedCornerShape(cornerRadius),
            elevation = CardDefaults.cardElevation(cardElevation),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF1F2A67),
                                Color(0xFF4F46E5),
                                Color(0xFF7C3AED)
                            )
                        )
                    )
                    .padding(paddingMedium)
            ) {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopEnd
                ) {
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .background(
                                Color.White.copy(alpha = 0.12f),
                                CircleShape
                            )
                    )
                }

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Box(
                        modifier = Modifier
                            .size(82.dp)
                            .background(
                                Color.White.copy(alpha = 0.10f),
                                CircleShape
                            )
                    )
                }

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "NUTRINO BANK",
                            color = Color.White.copy(alpha = 0.95f),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(Color(0xFFFF6B6B), CircleShape)
                            )

                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(Color(0xFFFFD166), CircleShape)
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .size(width = 56.dp, height = 40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFFFFD36B),
                                        Color(0xFFF2B84A)
                                    )
                                )
                            )
                            .border(
                                1.dp,
                                Color.White.copy(alpha = 0.45f),
                                RoundedCornerShape(10.dp)
                            )
                    )

                    Column {
                        Text(
                            text = "CARD NUMBER",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 10.sp,
                            letterSpacing = 1.3.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "5264 7631 9088 2741",
                            color = Color.White,
                            fontSize = 23.sp,
                            letterSpacing = 1.1.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        CardInfo("CARD HOLDER", "NUTRINO DEV")
                        CardInfo("EXPIRES", "08/31")
                        CardInfo("CVV", "726")
                    }
                }
            }
        }
    }
}

@Composable
fun CardInfo(label: String, value: String) {
    Column {
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.72f),
            fontSize = 9.sp,
            letterSpacing = 1.1.sp
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = value,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CreditCardDemoPreview() {
    ComposeUIPraticeTheme {
        CreditCardDemo()
    }
}