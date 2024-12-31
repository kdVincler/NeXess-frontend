package com.example.nexess_frontend

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun Prof(navController: NavController, modifier: Modifier = Modifier) {

    data class User(val name: String, val permissionLevel: Int)
    // TODO: Populate this with actual data from backend
    val currentUser = User("John User", 5)
    Column (
        modifier = modifier
    ) {
        Text(
            text = "Profile",
            fontSize = 40.sp,
            textAlign = TextAlign.Left,
            color = Color.White,
            modifier = Modifier
                .background(Color(android.graphics.Color.rgb(123, 60, 208)))
                .fillMaxWidth()
                .padding(start = 20.dp, top = 10.dp, bottom = 10.dp)
        )
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.AccountBox,
                contentDescription = Icons.Default.AccountBox.name,
                modifier = Modifier
                    .size(200.dp)
            )
            Text(
                text = "Name",
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                fontSize = 30.sp,
                modifier = Modifier.padding(top = 15.dp)
            )
            Text(
                fontSize = 30.sp,
                text = currentUser.name
            )

            Text(
                text = "Permission level",
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                fontSize = 30.sp,
                modifier = Modifier.padding(top = 15.dp)
            )
            Text(
                fontSize = 30.sp,
                text = "Level " + currentUser.permissionLevel.toString()
            )
        }
        BotNavBar(navController = navController, modifier = modifier.height(60.dp))
    }
}