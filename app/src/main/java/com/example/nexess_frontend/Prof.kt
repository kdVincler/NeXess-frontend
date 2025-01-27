package com.example.nexess_frontend

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@Composable
fun Prof(navController: NavController, modifier: Modifier = Modifier) {
    val currentUser by UserStore.currentUser.collectAsState()
    var popUpText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var showAlert by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
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
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .background(if (isSystemInDarkTheme()) Color.White else Color.Black)
            ) {
                Text(
                    text = currentUser?.initials ?: "-",
                    fontSize = 90.sp,
                    color = if (isSystemInDarkTheme()) Color.Black else Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = "Name",
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                fontSize = 23.sp,
                modifier = Modifier.padding(top = 25.dp)
            )
            Text(
                fontSize = 25.sp,
                text = currentUser?.name ?: "-"
            )

            Text(
                text = "Permission level",
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                fontSize = 23.sp,
                modifier = Modifier.padding(top = 25.dp)
            )
            Text(
                fontSize = 25.sp,
                text = (currentUser?.perm ?: "Level 0 - Guest")
            )
            Button(
                onClick = {
                    scope.launch {
                        isLoading = true
                        try {
                           KtorClient.logout()
                            navController.navigate(Routes.LGN)
                        } catch (e: Exception) {
                            popUpText = "${e.message}"
                            showAlert = true
                        } finally {
                            isLoading = false
                        }
                    }
                },
                colors = ButtonDefaults
                    .buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    ),
                modifier = Modifier.padding(top = 25.dp)
            ) {
                Text(
                    fontSize = 25.sp,
                    text = "Log out"
                )
            }
        }
        BotNavBar(navController = navController, modifier = modifier.height(60.dp))
    }
    AlertPopup(showAlert, "Logout failed", popUpText, onDismiss = {popUpText = ""; showAlert = false })
    LoadingOverlay(isLoading)
}