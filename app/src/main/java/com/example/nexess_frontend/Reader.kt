package com.example.nexess_frontend

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun Reader(navController: NavController, modifier: Modifier = Modifier) {
    Column (
        modifier = modifier
    ) {
        Text(
            text = "Reader",
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
        ){
            Image(
                painter = painterResource(id = R.drawable.nfc),
                contentDescription = "an image of an NFC coil",
                modifier = Modifier
                    .size(200.dp, 200.dp)
            )
            Text(
                text = "Searching for Door ID tags...",
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )
            // TODO: NFC Reader functionality implementation, and call to the backend with read data
        }
        BotNavBar(navController = navController, modifier = modifier.height(60.dp))
    }
}