package com.example.nexess_frontend

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@Composable
fun Login_(navController: NavController, modifier: Modifier = Modifier) {
    var un by remember { mutableStateOf("") }
    var pw by remember { mutableStateOf("") }
    var popUpText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var showAlert by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // if user is logged in, skip login screen
    // ensures that every time this composable is shows, the LaunchedEffect runs
    val timeMillisecond = remember { System.currentTimeMillis() }
    LaunchedEffect(timeMillisecond) {
        scope.launch {
            isLoading = true
            try {
                if (KtorClient.checkAuthStat()) {
                    navController.navigate(Routes.RDR)
                }
            } catch (e: Exception) {
                popUpText = "${e.message}"
                showAlert = true
            } finally {
                isLoading = false
            }
        }
    }

    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(Color(android.graphics.Color.rgb(123, 60, 208)))
            .fillMaxSize()
    ) {
        Text(
            text = "Log in to your account",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = modifier.padding(5.dp)
        )
        Row (
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = Icons.Default.AccountCircle.name,
                tint = if (isSystemInDarkTheme()) Color.Black else Color.White,
                modifier = modifier
                    .size(40.dp)
                    .padding(end = 2.dp)
            )
            TextField(
                value = un,
                onValueChange = { input ->
                    un = input
                },
                label = { Text("Username") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = modifier
                    .width(300.dp)
            )
        }
        Row (
            verticalAlignment = Alignment.CenterVertically,
        ){
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = Icons.Default.Lock.name,
                tint = if (isSystemInDarkTheme()) Color.Black else Color.White,
                modifier = modifier
                    .size(40.dp)
                    .padding(end = 2.dp)
            )
            TextField(
                value = pw,
                onValueChange = { input ->
                    pw = input
                },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                modifier = modifier
                    .width(300.dp)
            )
        }
        Button(
            onClick = {
                if (un.isNotBlank() && pw.isNotBlank()) {
                    scope.launch {
                        isLoading = true
                        try {
                            KtorClient.login(un, pw)
                            navController.navigate(Routes.RDR)
                        } catch (e: Exception) {
                            popUpText = "${e.message}"
                            showAlert = true
                        } finally {
                            isLoading = false
                        }
                    }
                } else {
                    popUpText = "Please fill in both username and password"
                    showAlert = true
                }
            },
            modifier = modifier.padding(5.dp)
        ) {
            Text(text = "Log in", fontSize = 20.sp)
        }
    }

    AlertPopup(showAlert, "Cannot Submit", popUpText, onDismiss = {popUpText = ""; showAlert = false })
    LoadingOverlay(isLoading)
}