package com.example.nexess_frontend

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@Composable
fun SplashScreen_(navController: NavController, modifier: Modifier = Modifier) {
    var popUpText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var showAlert by remember { mutableStateOf(false) }
    var redirecting by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    fun checkerFunction() {
        scope.launch {
            isLoading = true
            try {
                if (KtorClient.checkAuthStat()) {
                    navController.navigate(Routes.RDR)
                } else {
                    navController.navigate(Routes.LGN)
                }
            } catch (e: Exception) {
                popUpText = "${e.message}"
                redirecting = false
                showAlert = true
            } finally {
                isLoading = false
            }
        }
    }

    LaunchedEffect(Unit) {
        checkerFunction()
    }

    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(Color(android.graphics.Color.rgb(123, 60, 208)))
            .fillMaxSize()
    ) {
        Text(
            text = "NeXess",
            fontSize = 70.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
        )
        if(isLoading && redirecting) {
            LinearProgressIndicator(
                color = Color.White,
                trackColor = Color(android.graphics.Color.rgb(123, 60, 208)),
                strokeCap = StrokeCap.Square
            )
        }
        if (!isLoading && redirecting) {
            Text(
                text = "Navigating...",
                color = Color.White
            )
        }
        if (!isLoading && !redirecting) {
            Button(
                onClick = {
                    checkerFunction()
                },
            ) {
                Text(
                    text = "Try again",
                    fontSize = 20.sp
                )
            }
        }
    }
    AlertPopup(showAlert, "Auth. check failed", popUpText, onDismiss = {popUpText = ""; showAlert = false; redirecting = true; })
}