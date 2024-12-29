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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun Log(navController: NavController, modifier: Modifier = Modifier) {

    data class Log(val loc: String, val time: String)
    // TODO: Call to backend to get current user's entry logs
    val placeholderLogs = listOf(
        Log("Door 1", "2024-01-22 12:33"),
        Log("Door 1", "2024-01-22 13:23"),
        Log("Door 2", "2024-01-25 10:05"),
        Log("Door 3", "2024-01-25 11:06"),
        Log("Door 5", "2024-01-26 12:43"),
        Log("Door 4", "2024-01-26 16:33"),
        Log("Door 3", "2024-01-27 09:09"),
        Log("Door 2", "2024-01-27 17:21"),
        Log("Door 1", "2024-01-28 09:12"),
        Log("Door 2", "2024-01-28 10:21"),
        Log("Door 2", "2024-01-28 10:28"),
        Log("Door 1", "2024-01-28 17:43"),
    )
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
            items(placeholderLogs) { currentLog ->
                Column (Modifier.padding(10.dp)){
                    Text(text = currentLog.loc)
                    Text(text = currentLog.time, fontStyle = FontStyle.Italic)
                }
                HorizontalDivider()
            }
        }
        BotNavBar(navController = navController, modifier = modifier.height(60.dp))
    }
}