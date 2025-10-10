package com.nutrino.animination_in_compose.pratice

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AniminatevisiblityDemo(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()
    ) {
        val visible = remember { mutableStateOf(true) }
        AnimatedVisibility(
            visible = visible.value,
            exit = fadeOut(),
            enter = fadeIn()

        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ){
                // Your content goes here
                Button(
                    onClick = { visible.value = !visible.value }
                ) {
                    Text("Hide")
                }
            }

        }

        Button(
            onClick = { visible.value = !visible.value }
        ) {
            Text("Hide")
        }
    }

}


@Composable
fun FaqExpandableCard(
    question: String,
    answer: String,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Question header - always visible
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = question,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = Color.Gray
                )
            }

            // Answer - animated visibility
            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Text(
                    text = answer,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
        }
    }
}

@Composable
fun FaqScreen() {
    val faqList = listOf(
        "How do I reset my password?" to "Go to Settings > Security > Reset Password. Enter your email and follow the instructions sent to your inbox.",
        "What payment methods do you accept?" to "We accept all major credit cards, PayPal, Google Pay, and Apple Pay.",
        "How long does shipping take?" to "Standard shipping takes 3-5 business days. Express shipping is 1-2 business days.",
        "Can I cancel my order?" to "Yes, you can cancel within 24 hours of placing the order. Go to Orders > Select Order > Cancel.",
        "Do you offer refunds?" to "Yes, we offer a 30-day money-back guarantee on all products."
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                "Frequently Asked Questions",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        items(faqList) { (question, answer) ->
            FaqExpandableCard(
                question = question,
                answer = answer
            )
        }
    }
}
