package com.example.nexess_frontend

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun Log(navController: NavController, modifier: Modifier = Modifier) {
    val currentUser by UserStore.currentUser.collectAsState()
    Column (
        modifier = modifier
    ) {
        Text(
            text = "Entry Logs",
            fontSize = 40.sp,
            textAlign = TextAlign.Left,
            color = Color.White,
            modifier = Modifier
                .background(Color(android.graphics.Color.rgb(123, 60, 208)))
                .fillMaxWidth()
                .padding(start = 20.dp, top = 10.dp, bottom = 10.dp)
        )
        LazyColumn (
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if ((currentUser?.logs ?: listOf()).isNotEmpty()) {
                items(currentUser?.logs ?: listOf()) { currentLog ->
                    Column (Modifier.padding(10.dp)){
                        Text(text = currentLog.door_desc + " (ID: ${currentLog.door_id})")
                        Text(text = currentLog.accessed, fontStyle = FontStyle.Italic)
                    }
                    HorizontalDivider()
                }
            } else {
                items(count = 1, key = null) {
                    Text(
                        text = "No logs to display",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                    )
                }
            }
        }
        BotNavBar(navController = navController, modifier = modifier.height(60.dp))
    }
}