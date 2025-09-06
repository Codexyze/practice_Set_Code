package com.nutrino.widgets_in_android.Widgets

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.*
import androidx.glance.text.Text
import com.nutrino.widgets_in_android.MainActivity // example activity to launch


// 1️⃣ Receiver class (this connects widget to Android system)
class MyAppWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = MyAppWidget()
}

// 2️⃣ The actual widget definition
class MyAppWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            MyContent()
        }
    }

    @Composable
    private fun MyContent() {
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Where to?" )

            Row(
                modifier = GlanceModifier.padding(top = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    text = "Home",
                    onClick = actionStartActivity<MainActivity>() // opens app
                )
                Spacer(GlanceModifier.width(8.dp))
                Button(
                    text = "Work",
                    onClick = actionStartActivity<MainActivity>() // or another activity
                )
            }
        }
    }
}
