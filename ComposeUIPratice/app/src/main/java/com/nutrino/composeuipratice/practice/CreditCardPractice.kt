package com.nutrino.composeuipratice.practice

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
//
//@Composable
//fun CreditCardPratice() {
//
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        val cornerRadius = 26.dp
//
//        Card(
//            modifier = Modifier
//                .fillMaxWidth()
//                .aspectRatio(1.6f),
//            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
//            shape = RoundedCornerShape(cornerRadius)
//
//        ) {
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(
//                        brush = Brush.linearGradient(
//                            arrayListOf(
//                                Color(0xFF1F2A67),
//                                Color(0xFF4F46E5),
//                                Color(0xFF7C3AED)
//
//                            )
//                        )
//                    )
//
//            ){
//                Column(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(25.dp)
//                ) {
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                    ) {
//                        Text("NUTRINO BANK",
//                            color = Color.White,
//                            fontSize = 25.sp,
//                            fontWeight = FontWeight.Bold
//
//                        )
//                        Spacer(modifier = Modifier.width(40.dp))
//                        CirclesShapeForCard(color =Color(0xFFFFC107))
//                        Spacer(modifier = Modifier.width(2.dp))
//                        CirclesShapeForCard(color =Color(0xFFFF0057))
//
//                    }
//                    Spacer(modifier = Modifier.height(16.dp))
//                    Row(
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        Card(
//                            modifier = Modifier.width(70.dp).height(45.dp).background(
//                                brush = Brush.linearGradient(
//                                    arrayListOf(
//                                        Color(0xFFF7D772),
//                                        Color(0xFFF4BA0E)
//
//                                    )
//                                )
//                            ),
//                            colors = CardDefaults.cardColors(
//                                containerColor = Color.Transparent
//                            )
//                        ) {
//
//                        }
//
//                    }
//                    Spacer(modifier = Modifier.height(16.dp))
//                    Row(modifier = Modifier.fillMaxWidth()) {
//                        Text(
//                            text = "CARD NUMBER",
//                            color = Color.White,
//                            fontWeight = FontWeight.Bold
//                        )
//                    }
//                    Row(
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        Text(
//                            text = "XXXX XXXX XXXX XXXX",
//                            color = Color.White,
//                            fontWeight = FontWeight.Bold,
//                            fontSize = 25.sp,
//                        )
//
//                    }
//                    Spacer(modifier = Modifier.height(16.dp))
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.SpaceBetween
//                    ) {
//
//                        Column(
//                            modifier = Modifier.weight(1f),
//                            horizontalAlignment = Alignment.Start
//                        ) {
//                            Text("CARD HOLDER", color = Color.White, fontSize = 12.sp)
//                            Spacer(modifier = Modifier.height(5.dp))
//                            Text("AKSHAY SARAPURE", color = Color.White, fontSize = 12.sp)
//                        }
//
//                        Column(
//                            modifier = Modifier.weight(1f),
//                            horizontalAlignment = Alignment.CenterHorizontally
//                        ) {
//                            Text("EXPIRES", color = Color.White, fontSize = 12.sp)
//                            Spacer(modifier = Modifier.height(5.dp))
//                            Text("08/31", color = Color.White, fontSize = 12.sp)
//                        }
//
//                        Column(
//                            modifier = Modifier.weight(1f),
//                            horizontalAlignment = Alignment.End
//                        ) {
//                            Text("CVV", color = Color.White, fontSize = 12.sp)
//                            Spacer(modifier = Modifier.height(5.dp))
//                            Text("122", color = Color.White, fontSize = 12.sp)
//                        }
//                    }
//
//                }
//            }
//
//        }
//    }
//
//}
//
//@Composable
//fun CirclesShapeForCard(color: Color) {
//    Box(
//        modifier = Modifier
//            .size(27.dp)
//            .background(
//                color = color,
//                shape = CircleShape
//            )
//
//    )
//}



@Composable
fun CreditCardPractice() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp).clickable{
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.6f),
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            listOf(
                                Color(0xFF1F2A67),
                                Color(0xFF4F46E5),
                                Color(0xFF7C3AED)
                            )
                        )
                    )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {

                    CreditCardHeader()

                    CreditCardMiddleSection()

                    CreditCardFooter()
                }
            }
        }
    }
}

@Composable
fun CreditCardHeader() {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = "NUTRINO BANK",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Row {
            CircleShapeCard(Color(0xFFFFC107))
            Spacer(modifier = Modifier.width(4.dp))
            CircleShapeCard(Color(0xFFFF0057))
        }
    }
}

@Composable
fun CreditCardMiddleSection() {

    Column {

        CreditCardChip()

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "CARD NUMBER",
            color = Color.White.copy(alpha = 0.75f),
            fontSize = 10.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "XXXX XXXX XXXX XXXX",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CreditCardChip() {

    Box(
        modifier = Modifier
            .width(60.dp)
            .height(42.dp)
            .background(
                Brush.linearGradient(
                    listOf(
                        Color(0xFFF7D772),
                        Color(0xFFF4BA0E)
                    )
                ),
                shape = RoundedCornerShape(8.dp)
            )
    )
}

@Composable
fun CreditCardFooter() {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        CardInfo(
            title = "CARD HOLDER",
            value = "AKSHAY SARAPURE",
            modifier = Modifier.weight(1f),
            alignment = Alignment.Start
        )

        CardInfo(
            title = "EXPIRES",
            value = "08/31",
            modifier = Modifier.weight(1f),
            alignment = Alignment.CenterHorizontally
        )

        CardInfo(
            title = "CVV",
            value = "122",
            modifier = Modifier.weight(1f),
            alignment = Alignment.End
        )
    }
}

@Composable
fun CardInfo(
    title: String,
    value: String,
    modifier: Modifier,
    alignment: Alignment.Horizontal
) {

    Column(
        modifier = modifier,
        horizontalAlignment = alignment
    ) {

        Text(
            text = title,
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 9.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = value,
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun CircleShapeCard(color: Color) {
    Box(
        modifier = Modifier
            .size(24.dp)
            .background(color, CircleShape)
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CreditPracticeDemoPrev() {
    CreditCardPractice()
}

